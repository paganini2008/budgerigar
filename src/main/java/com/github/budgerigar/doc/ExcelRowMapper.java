package com.github.budgerigar.doc;

/**
 * 
 * @Description: ExcelRowMapper
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public interface ExcelRowMapper {

    void handleHeader();

    void mapCell();

}
