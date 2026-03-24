package com.kanade.backend.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Table("examPaper")
public class ExamPaper implements Serializable {

    @Id(keyType = KeyType.Auto)
    private Long id;

    private String paperName;

    private String subject;

    private Integer totalScore;

    @Column("creatorId")
    private Long creatorId;

    private Integer status; // 0-草稿 1-发布 2-归档 3-停用

    @Column("createTime")
    private LocalDateTime createTime;

    @Column("updateTime")
    private LocalDateTime updateTime;

    @Column(value = "isDeleted", isLogicDelete = true)
    private Integer isDeleted;
}