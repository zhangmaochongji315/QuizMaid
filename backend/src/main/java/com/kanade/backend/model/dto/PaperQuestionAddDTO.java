package com.kanade.backend.model.dto;

import lombok.Data;

@Data
public class PaperQuestionAddDTO {
    private Long paperId;
    private Long questionId;
    private Integer questionScore; // 该题分值
    private Integer sort;          // 排序，可选
}