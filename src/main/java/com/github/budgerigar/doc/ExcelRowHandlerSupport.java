package com.github.budgerigar.doc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * 
 * @Description: ExcelRowHandlerSupport
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public abstract class ExcelRowHandlerSupport implements ExcelRowHandler {

    private CellValueExtractor cellValueExtractor = new DefaultCellValueExtractor();

    public void setCellValueExtractor(CellValueExtractor cellValueExtractor) {
        this.cellValueExtractor = cellValueExtractor;
    }

    @Override
    public void handleRow(Sheet sheet, Row row, Iterator<Cell> cellIter) {
        List<String> rowDataList = new ArrayList<>();
        while (cellIter.hasNext()) {
            Cell cell = cellIter.next();
            rowDataList.add(getCellValue(sheet, row, cell));
        }
        handleRowData(sheet, row, rowDataList);
    }

    protected abstract void handleRowData(Sheet sheet, Row row, List<String> rowDataList);

    protected String getCellValue(Sheet sheet, Row row, Cell cell) {
        return ExcelReaderUtils.readCell(cell, cellValueExtractor);
    }

}
