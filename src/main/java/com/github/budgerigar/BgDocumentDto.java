package com.github.budgerigar;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @Description: BgDocumentDto
 * @Author: Fred Feng
 * @Date: 01/12/2024
 * @Version 1.0.0
 */
@Data
public class BgDocumentDto {

    private Serializable id;
    private String name;
    private String title;
    private String extention;
    private String path;
    private LocalDateTime lastModified;
    private String content;

}
