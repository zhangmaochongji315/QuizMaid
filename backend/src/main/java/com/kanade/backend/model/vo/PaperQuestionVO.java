package com.kanade.backend.model.vo;

import lombok.Data;

@Data
public class PaperQuestionVO {
    private Long id;               // 关联ID
    private Long questionId;
    private String questionContent; // 可选，题目题干
    private Integer questionScore;
    private Integer sort;
    private Integer type;           // 题型，方便前端显示
}