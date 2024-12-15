package com.github.budgerigar.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.experimental.UtilityClass;

/**
 * 
 * @Description: ExcelReaderUtils
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
@UtilityClass
public class ExcelReaderUtils {

    public String readCell(Cell cell, CellValueExtractor cellValueExtractor) {
        if (cell == null) {
            return "";
        }
        try {
            CellType cellType = cell.getCellType();
            switch (cellType) {
                case BLANK: {
                    return "";
                }
                case ERROR:
                    return cellValueExtractor.getErrorValue(cell, null);
                case FORMULA:
                    if (cellValueExtractor.supportFormula(cell)) {
                        FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                        CellValue cellValue = evaluator.evaluate(cell);
                        return cellValue.formatAsString();
                    } else {
                        return cell.getCellFormula();
                    }
                case STRING:
                    return cellValueExtractor.getStringValue(cell);
                case NUMERIC:
                    return cellValueExtractor.getNumericValue(cell, cell.getCellStyle().getDataFormat());
                case BOOLEAN:
                    return cellValueExtractor.getBooleanValue(cell);
                default:
                    return cellValueExtractor.getOtherValue(cell);
            }
        } catch (Exception e) {
            return cellValueExtractor.getErrorValue(cell, e);
        }
    }

    public void readExcel(File file, ExcelRowHandler rowHandler) throws IOException {
        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                for (Row row : sheet) {
                    rowHandler.handleRow(sheet, row, row.cellIterator());
                }
            }
        }
    }

}
