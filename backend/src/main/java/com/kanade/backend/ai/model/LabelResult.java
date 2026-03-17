package com.kanade.backend.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

import java.util.List;

@Data
@Description("题库知识点标注的结果")
public class LabelResult {

    @Description("所属学科")
    private String subject;

    @Description("教材章节")
    private String chapter;

    @Description("知识点集合，逗号分隔字符串")
    private String knowledgePoints;

    @Description("试题标签，JSON数组")
    private String[] tags;

    @Description("难度等级 1=简单 2=普通 3=难")
    private Integer difficult;
}
