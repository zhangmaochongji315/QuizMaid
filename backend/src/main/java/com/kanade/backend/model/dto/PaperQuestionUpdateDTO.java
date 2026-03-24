package com.kanade.backend.model.dto;

import lombok.Data;

@Data
public class PaperQuestionUpdateDTO {
    private Long id;               // 关联表ID
    private Integer questionScore;
    private Integer sort;
}