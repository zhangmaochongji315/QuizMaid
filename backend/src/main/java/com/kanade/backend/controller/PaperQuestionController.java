package com.kanade.backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.kanade.backend.common.BaseResponse;
import com.kanade.backend.common.DeleteRequest;
import com.kanade.backend.common.ResultUtils;
import com.kanade.backend.model.dto.PaperQuestionAddDTO;
import com.kanade.backend.model.dto.PaperQuestionUpdateDTO;
import com.kanade.backend.service.PaperQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paperQuestion")
@RequiredArgsConstructor
@Tag(name = "试卷试题关联管理")
public class PaperQuestionController {

    private final PaperQuestionService paperQuestionService;

    @PostMapping("/add")
    @SaCheckLogin
    @Operation(summary = "添加试题到试卷")
    public BaseResponse<Long> addQuestionToPaper(@RequestBody PaperQuestionAddDTO addDTO) {
        Long id = paperQuestionService.addQuestionToPaper(addDTO);
        return ResultUtils.success(id);
    }

    @PostMapping("/update")
    @SaCheckLogin
    @Operation(summary = "更新试卷中试题的分值或排序")
    public BaseResponse<Boolean> updatePaperQuestion(@RequestBody PaperQuestionUpdateDTO updateDTO) {
        boolean result = paperQuestionService.updatePaperQuestion(updateDTO);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    @SaCheckLogin
    @Operation(summary = "从试卷中移除试题")
    public BaseResponse<Boolean> removeQuestionFromPaper(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest.getId() == null) {
            throw new RuntimeException("关联ID不能为空");
        }
        boolean result = paperQuestionService.removeQuestionFromPaper(deleteRequest.getId());
        return ResultUtils.success(result);
    }
}