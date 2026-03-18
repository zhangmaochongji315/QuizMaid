package com.kanade.backend.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class QuestionExcelData {
    @ExcelProperty("题型")
    private Integer type;

    @ExcelProperty("学科")
    private String subject;

    @ExcelProperty("章节")
    private String chapter;

    @ExcelProperty("难度")
    private Integer difficulty;

    @ExcelProperty("知识点")
    private String knowledgePoints;

    @ExcelProperty("标签")
    private String tags;

    @ExcelProperty("题干")
    private String content;

    @ExcelProperty("选项")
    private String options;

    @ExcelProperty("答案")
    private String answer;

    @ExcelProperty("解析")
    private String analysis;

    @ExcelProperty("状态")
    private Integer status;
}