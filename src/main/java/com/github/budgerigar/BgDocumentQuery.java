package com.github.budgerigar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @Description: BgDocumentQuery
 * @Author: Fred Feng
 * @Date: 14/12/2024
 * @Version 1.0.0
 */
@Getter
@Setter
@ToString
public class BgDocumentQuery {

    private String keyword;
    private boolean file;
    private String extension;
    private int from;
    private int maxResults;

}
