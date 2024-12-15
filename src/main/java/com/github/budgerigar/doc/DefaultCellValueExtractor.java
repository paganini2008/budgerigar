package com.github.budgerigar.doc;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 
 * @Description: DefaultCellValueExtractor
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public class DefaultCellValueExtractor implements CellValueExtractor {

    @Override
    public String getStringValue(Cell cell) {
        return cell.getRichStringCellValue().getString();
    }

    @Override
    public String getBooleanValue(Cell cell) {
        return String.valueOf(cell.getBooleanCellValue());
    }

    @Override
    public String getNumericValue(Cell cell, int dateFormatIndex) {
        return getStringValue(cell);
    }

}
