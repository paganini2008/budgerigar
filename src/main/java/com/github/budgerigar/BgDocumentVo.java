package com.github.budgerigar;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @Description: BgDocumentVo
 * @Author: Fred Feng
 * @Date: 14/12/2024
 * @Version 1.0.0
 */
@Data
public class BgDocumentVo {

    private Serializable id;
    private String name;
    private String title;
    private String extention;
    private String path;
    private LocalDateTime lastModified;
    private String content;
}
