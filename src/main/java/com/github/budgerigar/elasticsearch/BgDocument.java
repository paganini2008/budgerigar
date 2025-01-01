package com.github.budgerigar.elasticsearch;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @Description: BgDocument
 * @Author: Fred Feng
 * @Date: 22/12/2024
 * @Version 1.0.0
 */
@Getter
@Setter
@Setting(refreshInterval = "10s")
@Document(indexName = BgDocument.INDEX_NAME)
public class BgDocument {

    public static final String INDEX_NAME = "bg_document_0";

    @Id
    @Field(type = FieldType.Text, store = true)
    private String id;

    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String name;

    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Keyword, store = true)
    private String extention;

    @Field(type = FieldType.Keyword, store = true)
    private String path;

    @Field(type = FieldType.Date, store = true, format = {DateFormat.date_hour_minute_second})
    private LocalDateTime lastModified;

    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String content;

}
