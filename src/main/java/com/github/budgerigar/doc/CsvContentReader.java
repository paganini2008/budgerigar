package com.github.budgerigar.doc;

import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.io.FilenameUtils;
import com.github.doodler.common.Constants;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * 
 * @Description: CsvContentReader
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public class CsvContentReader implements FileContentReader {

    @Override
    public Iterator<FileContent> readContent(Path path, Context context) throws Exception {
        Charset charset = (Charset) context.getAttr(Context.FILE_CHARSET, defaultCharset);
        try (Reader reader = Files.newBufferedReader(path, charset)) {
            try (CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(new CSVParserBuilder().withSeparator(',').build()).build()) {
                List<String[]> dataRows = csvReader.readAll();
                if (CollectionUtils.isEmpty(dataRows)) {
                    return IteratorUtils.emptyIterator();
                }
                String content =
                        dataRows.stream().map(row -> String.join(",", row))
                                .collect(Collectors.collectingAndThen(
                                        Collectors.joining(Constants.NEWLINE), StringBuilder::new))
                                .toString();
                return new SingleElementIterator<FileContent>(new FileContent(path, content));
            }
        }
    }

    @Override
    public boolean supportExtensions(Path file, Context context) {
        String ext = FilenameUtils.getExtension(file.getFileName().toString());
        return "csv".equalsIgnoreCase(ext);
    }

    public static void main(String[] args) throws Exception {
        CsvContentReader csvContentReader = new CsvContentReader();
        Iterator<FileContent> it = csvContentReader.readContent(
                Paths.get("G:/callbacks_979654_2023-06-15_01_45_14.csv"), new Context());
        System.out.println(it);
    }

}
