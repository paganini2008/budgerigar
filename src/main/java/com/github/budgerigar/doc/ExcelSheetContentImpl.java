package com.github.budgerigar.doc;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @Description: ExcelSheetContentImpl
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
@Getter
@Setter
public class ExcelSheetContentImpl implements ExcelSheetContent {

    private final Sheet sheet;
    private final int sheetIndex;
    private List<String[]> content;

    ExcelSheetContentImpl(Sheet sheet, int sheetIndex) {
        this.sheet = sheet;
        this.sheetIndex = sheetIndex;
    }

}
