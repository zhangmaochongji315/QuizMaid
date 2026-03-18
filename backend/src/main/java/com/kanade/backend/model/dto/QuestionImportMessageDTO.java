package com.kanade.backend.model.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class QuestionImportMessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String taskId;          // 任务ID（UUID）
    private String filePath;         // 文件绝对路径
    private Long creatorId;          // 创建人ID
}