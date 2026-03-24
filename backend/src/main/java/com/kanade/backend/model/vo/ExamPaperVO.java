package com.kanade.backend.model.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExamPaperVO {
    private Long id;
    private String paperName;
    private String subject;
    private Integer totalScore;
    private Long creatorId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<PaperQuestionVO> questions; // 可选，用于详情返回关联的试题信息
}