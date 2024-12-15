package com.github.budgerigar.doc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import org.apache.commons.io.FilenameUtils;
import com.github.doodler.common.utils.DebugUtils;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @Description: ExcelContentReader
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
@RequiredArgsConstructor
public class ExcelContentReader implements FileContentReader {

    private final CellValueExtractor cellValueExtractor;

    @Override
    public Iterator<FileContent> readContent(Path path, Context context) throws IOException {
        final File file = path.toFile();
        final ExcelSheetIterator excelSheetIterator =
                new ExcelSheetIterator(file, cellValueExtractor);
        return new Iterator<FileContent>() {

            @Override
            public boolean hasNext() {
                return excelSheetIterator.hasNext();
            }

            @Override
            public FileContent next() {
                StringBuilder str = new StringBuilder();
                ExcelSheetContent excelSheetContent = excelSheetIterator.next();
                for (String[] data : excelSheetContent.getContent()) {
                    str.append(String.join(",", data));
                }
                String content = str.toString();
                return new FileContent(file.getName(), excelSheetContent.getSheetName(),
                        FilenameUtils.getExtension(file.getName()), file.getAbsolutePath(),
                        file.lastModified(), content);
            }

        };
    }

    @Override
    public boolean supportExtensions(Path path, Context context) {
        String ext = FilenameUtils.getExtension(path.getFileName().toString());
        return "xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext);
    }

    public static void main(String[] args) throws IOException {
        Path file = Paths.get("G:/Reimbursement.xlsx");
        ExcelContentReader contentReader = new ExcelContentReader(new DefaultCellValueExtractor());
        Iterator<FileContent> iter = contentReader.readContent(file, new Context());
        DebugUtils.info(iter);
    }

}
