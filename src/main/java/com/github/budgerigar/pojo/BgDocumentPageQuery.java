package com.github.budgerigar.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @Description: BgDocumentPageQuery
 * @Author: Fred Feng
 * @Date: 14/12/2024
 * @Version 1.0.0
 */
@Getter
@Setter
@ToString
public class BgDocumentPageQuery extends BgDocumentQuery {

    private int page = 1;
    private Object nextToken;

}
