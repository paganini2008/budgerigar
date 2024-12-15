package com.github.budgerigar.doc;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * 
 * @Description: ExcelRowHandler
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public interface ExcelRowHandler {

    void handleRow(Sheet sheet, Row row, Iterator<Cell> cellIter);

}
