package com.github.budgerigar;

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
@ToString(callSuper = true)
public class BgDocumentPageQuery extends BgDocumentQuery {

    private int page;

}
