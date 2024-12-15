package com.github.budgerigar.doc;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 
 * @Description: CellValueExtractor
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public interface CellValueExtractor {

    String getStringValue(Cell cell);

    String getBooleanValue(Cell cell);

    String getNumericValue(Cell cell, int dateFormatIndex);

    default boolean supportFormula(Cell cell) {
        return true;
    }

    default String getErrorValue(Cell cell, Exception e) {
        return "";
    }

    default String getOtherValue(Cell cell) {
        return "";
    }

}
