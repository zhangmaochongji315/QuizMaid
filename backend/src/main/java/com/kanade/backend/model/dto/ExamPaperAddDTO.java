package com.kanade.backend.model.dto;

import lombok.Data;

@Data
public class ExamPaperAddDTO {
    private String paperName;
    private String subject;
    private Integer totalScore;
    private Integer status; // 可选，默认为0（草稿）
}