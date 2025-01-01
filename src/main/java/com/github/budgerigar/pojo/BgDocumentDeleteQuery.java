package com.github.budgerigar.pojo;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @Description: BgDocumentDeleteQuery
 * @Author: Fred Feng
 * @Date: 14/12/2024
 * @Version 1.0.0
 */
@Data
public class BgDocumentDeleteQuery {

    private String name;
    private String title;
    private String extention;
    private String path;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
