package com.github.budgerigar.doc;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * 
 * @Description: ExcelSheetContent
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public interface ExcelSheetContent {

    default String getSheetName() {
        return getSheet().getSheetName();
    }

    Sheet getSheet();

    int getSheetIndex();

    List<String[]> getContent();

}
