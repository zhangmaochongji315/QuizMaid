package com.kanade.backend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.kanade.backend.exception.BusinessException;
import com.kanade.backend.exception.ErrorCode;
import com.kanade.backend.mapper.ExamPaperMapper;
import com.kanade.backend.mapper.PaperquestionMapper;
import com.kanade.backend.model.dto.ExamPaperQueryDTO;
import com.kanade.backend.model.entity.ExamPaper;
import com.kanade.backend.model.entity.Paperquestion;
import com.kanade.backend.model.entity.Question;
import com.kanade.backend.model.vo.ExamPaperVO;
import com.kanade.backend.model.vo.PaperQuestionVO;
import com.kanade.backend.service.ExamPaperService;
import com.kanade.backend.service.QuestionService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamPaperServiceImpl extends ServiceImpl<ExamPaperMapper, ExamPaper> implements ExamPaperService {

    private final PaperquestionMapper paperquestionMapper;
    private final QuestionService questionService;

    @Override
    public Long addExamPaper(ExamPaper examPaper) {
        if (StrUtil.isBlank(examPaper.getPaperName())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "试卷名称不能为空");
        }
        if (StrUtil.isBlank(examPaper.getSubject())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学科不能为空");
        }
        if (examPaper.getTotalScore() == null) {
            examPaper.setTotalScore(0);
        }
        examPaper.setCreatorId(StpUtil.getLoginIdAsLong());
        if (examPaper.getStatus() == null) {
            examPaper.setStatus(0); // 草稿
        }
        this.save(examPaper);
        return examPaper.getId();
    }

    @Override
    public boolean updateExamPaper(ExamPaper examPaper) {
        Long id = examPaper.getId();
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "试卷ID不能为空");
        }
        ExamPaper old = this.getById(id);
        if (old == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "试卷不存在");
        }
        // 校验权限：创建人才能修改
        Long currentUserId = StpUtil.getLoginIdAsLong();
        if (!old.getCreatorId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "无权修改他人试卷");
        }
        // 不允许修改创建人
        examPaper.setCreatorId(null);
        return this.updateById(examPaper);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        ExamPaper paper = new ExamPaper();
        paper.setId(id);
        paper.setStatus(status);
        return updateExamPaper(paper); // 复用权限校验
    }

    @Override
    public boolean deleteExamPaper(Long id) {
        ExamPaper paper = this.getById(id);
        if (paper == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "试卷不存在");
        }
        Long currentUserId = StpUtil.getLoginIdAsLong();
        if (!paper.getCreatorId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "无权删除他人试卷");
        }
        // 逻辑删除，MyBatis-Flex 自动处理
        return this.removeById(id);
    }

    @Override
    public ExamPaperVO getExamPaperVOById(Long id) {
        ExamPaper paper = this.getById(id);
        if (paper == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "试卷不存在");
        }
        ExamPaperVO vo = new ExamPaperVO();
        BeanUtils.copyProperties(paper, vo);
        // 获取关联的试题
        List<Paperquestion> relations = paperquestionMapper.selectList(
                QueryWrapper.create().eq("paperId", id).orderBy("sort", true)
        );
        if (!relations.isEmpty()) {
            List<Long> questionIds = relations.stream().map(Paperquestion::getQuestionId).collect(Collectors.toList());
            List<Question> questions = questionService.listByIds(questionIds);
            List<PaperQuestionVO> questionVOs = relations.stream().map(rel -> {
                PaperQuestionVO qvo = new PaperQuestionVO();
                qvo.setId(rel.getId());
                qvo.setQuestionId(rel.getQuestionId());
                qvo.setQuestionScore(rel.getQuestionScore());
                qvo.setSort(rel.getSort());
                // 补充题干和题型
                questions.stream().filter(q -> q.getId().equals(rel.getQuestionId())).findFirst().ifPresent(q -> {
                    qvo.setQuestionContent(q.getContent());
                    qvo.setType(q.getType());
                });
                return qvo;
            }).collect(Collectors.toList());
            vo.setQuestions(questionVOs);
        }
        return vo;
    }

    @Override
    public Page<ExamPaperVO> getExamPaperPage(ExamPaperQueryDTO queryDTO) {
        QueryWrapper wrapper = QueryWrapper.create();
        if (StrUtil.isNotBlank(queryDTO.getPaperName())) {
            wrapper.like("paperName", queryDTO.getPaperName());
        }
        if (StrUtil.isNotBlank(queryDTO.getSubject())) {
            wrapper.eq("subject", queryDTO.getSubject());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq("status", queryDTO.getStatus());
        }
        // 权限控制：普通用户只能查自己的，管理员查所有
        Long currentUserId = StpUtil.getLoginIdAsLong();
        boolean isAdmin = StpUtil.hasRole("admin");
        if (!isAdmin) {
            wrapper.eq("creatorId", currentUserId);
        } else if (queryDTO.getCreatorId() != null) {
            wrapper.eq("creatorId", queryDTO.getCreatorId());
        }

        if (StrUtil.isNotBlank(queryDTO.getSortField())) {
            String sortField = queryDTO.getSortField();
            boolean isAsc = "ascend".equals(queryDTO.getSortOrder());
            wrapper.orderBy(sortField, isAsc);
        } else {
            wrapper.orderBy("createTime", false);
        }

        Page<ExamPaper> page = this.page(Page.of(queryDTO.getPageNum(), queryDTO.getPageSize()), wrapper);
        Page<ExamPaperVO> voPage = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize(), page.getTotalRow());
        List<ExamPaperVO> voList = page.getRecords().stream().map(paper -> {
            ExamPaperVO vo = new ExamPaperVO();
            BeanUtils.copyProperties(paper, vo);
            return vo;
        }).collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }
}