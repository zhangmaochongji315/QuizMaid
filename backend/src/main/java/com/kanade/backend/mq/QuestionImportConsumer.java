package com.kanade.backend.mq;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.kanade.backend.config.RabbitMQConfig;
import com.kanade.backend.exception.BusinessException;
import com.kanade.backend.exception.ErrorCode;
import com.kanade.backend.model.dto.QuestionImportMessageDTO;
import com.kanade.backend.model.entity.Question;
import com.kanade.backend.model.excel.QuestionExcelData;
import com.kanade.backend.service.ImportTaskRedisService;
import com.kanade.backend.service.QuestionService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class QuestionImportConsumer {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ImportTaskRedisService importTaskRedisService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_IMPORT)
    public void handleImport(QuestionImportMessageDTO message, Channel channel, Message amqpMessage) throws IOException {
        long deliveryTag = amqpMessage.getMessageProperties().getDeliveryTag();
        String taskId = message.getTaskId();
        String filePath = message.getFilePath();
        Long creatorId = message.getCreatorId();

        try {
            // 1. 检查文件是否存在
            if (!Files.exists(Paths.get(filePath))) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件不存在");
            }

            // 2. 使用 EasyExcel 监听器逐行处理，同时更新 Redis 进度
            List<String> errorList = new ArrayList<>();
            int[] total = {0};
            int[] success = {0};

            EasyExcel.read(filePath, QuestionExcelData.class, new AnalysisEventListener<QuestionExcelData>() {
                @Override
                public void invoke(QuestionExcelData data, AnalysisContext context) {
                    total[0]++;
                    try {
                        // 数据校验
                        validateExcelData(data);
                        // 转换为 Question 实体
                        Question question = convertToQuestion(data, creatorId);
                        // 调用原有添加逻辑（包含MD5查重）
                        questionService.addQuestion(question);
                        success[0]++;
                    } catch (Exception e) {
                        log.error("第{}行数据导入失败: {}", total[0], e.getMessage());
                        errorList.add("第" + total[0] + "行: " + e.getMessage());
                    }

                    // 每处理10行更新一次 Redis（减少IO）
                    if (total[0] % 10 == 0) {
                        List<String> batchErrors = new ArrayList<>(errorList);
                        importTaskRedisService.updateProgress(taskId, 10, success[0] % 10, (10 - success[0] % 10) % 10, batchErrors);
                        errorList.clear();
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    log.info("Excel解析完成，共{}行", total[0]);
                    // 最后更新剩余数据
                    if (!errorList.isEmpty() || total[0] % 10 != 0) {
                        int lastBatchCount = total[0] % 10;
                        if (lastBatchCount == 0) lastBatchCount = 10;
                        importTaskRedisService.updateProgress(taskId, lastBatchCount,
                                success[0] % 10, (lastBatchCount - success[0] % 10) % 10, errorList);
                    }
                    // 标记任务完成（成功或失败）
                    boolean allSuccess = errorList.isEmpty();
                    importTaskRedisService.finishTask(taskId, allSuccess);
                }
            }).sheet().doRead();

            // 3. 处理完成后手动确认消息
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("导入处理失败", e);
            // 记录失败状态到 Redis
            importTaskRedisService.finishTask(taskId, false);
            importTaskRedisService.updateProgress(taskId, 0, 0, 0, List.of("系统错误: " + e.getMessage()));
            // 拒绝消息，不重新入队（可根据策略调整）
            channel.basicNack(deliveryTag, false, false);
        }
    }

    private void validateExcelData(QuestionExcelData data) {
        if (data.getType() == null || data.getType() < 1 || data.getType() > 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题型必须是1-4之间的数字");
        }
        if (StrUtil.isBlank(data.getSubject())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学科不能为空");
        }
        if (data.getDifficulty() == null || data.getDifficulty() < 1 || data.getDifficulty() > 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "难度必须是1-3之间的数字");
        }
        if (StrUtil.isBlank(data.getContent())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题干不能为空");
        }
        if (StrUtil.isBlank(data.getAnswer())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案不能为空");
        }
    }

    private Question convertToQuestion(QuestionExcelData data, Long creatorId) {
        Question question = new Question();
        BeanUtils.copyProperties(data, question);
        question.setCreatorId(creatorId);
        if (question.getStatus() == null) {
            question.setStatus(1); // 默认为草稿
        }
        return question;
    }
}