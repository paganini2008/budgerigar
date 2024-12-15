package com.github.budgerigar.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @Description: ExcelSheetIterator
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public class ExcelSheetIterator implements Iterator<ExcelSheetContent> {

    private final Workbook workbook;
    private final CellValueExtractor cellValueExtractor;
    private final int numberOfSheets;

    public ExcelSheetIterator(File file) throws IOException {
        this(file, new DefaultCellValueExtractor());
    }

    public ExcelSheetIterator(File file, CellValueExtractor cellValueExtractor) throws IOException {
        String ext = FilenameUtils.getExtension(file.getAbsolutePath());
        this.workbook = "xlsx".equalsIgnoreCase(ext) ? new XSSFWorkbook(new FileInputStream(file)) : new HSSFWorkbook(
                new FileInputStream(file));
        this.numberOfSheets = workbook.getNumberOfSheets();
        this.cellValueExtractor = cellValueExtractor;
    }

    private int sheetIndex = 0;

    @Override
    public boolean hasNext() {
        boolean hasNext = sheetIndex < numberOfSheets;
        if (!hasNext) {
            try {
                workbook.close();
            } catch (IOException e) {
                return false;
            }
        }
        return hasNext;
    }

    @Override
    public ExcelSheetContent next() {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        ExcelSheetContentImpl excelSheetContent = new ExcelSheetContentImpl(sheet, sheetIndex);
        List<String[]> dataList = new ArrayList<>();
        for (Row row : sheet) {
            List<String> rowDataList = new ArrayList<>();
            Iterator<Cell> cellIter = row.cellIterator();
            while (cellIter.hasNext()) {
                Cell cell = cellIter.next();
                rowDataList.add(getCellValue(sheet, row, cell));
            }
            dataList.add(rowDataList.toArray(new String[0]));
        }
        excelSheetContent.setContent(dataList);
        sheetIndex++;
        return excelSheetContent;
    }

    protected String getCellValue(Sheet sheet, Row row, Cell cell) {
        return ExcelReaderUtils.readCell(cell, cellValueExtractor);
    }

}
