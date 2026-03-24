package com.kanade.backend.model.dto;

import com.kanade.backend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExamPaperQueryDTO extends PageRequest {
    private String paperName;
    private String subject;
    private Integer status;
    private Long creatorId; // 用于用户筛选自己的试卷
}