package com.kanade.backend.model.dto;

import lombok.Data;

@Data
public class PaperQuestionRemoveDTO {
    private Long id;               // 关联表ID
    // 或者通过 paperId + questionId 删除，这里简化用关联ID
}