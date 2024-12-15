package com.github.budgerigar.doc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * 
 * @Description: Word2007ContentReader
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public class Word2007ContentReader implements FileContentReader {

    @Override
    public Iterator<FileContent> readContent(Path path, Context context) throws IOException {
        try (InputStream fis = Files.newInputStream(path);
                XWPFDocument document = new XWPFDocument(fis);
                XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            String content = extractor.getText();
            return new SingleElementIterator<FileContent>(new FileContent(path, content));
        }
    }

    @Override
    public boolean supportExtensions(Path path, Context context) {
        String ext = FilenameUtils.getExtension(path.getFileName().toString());
        return ext.equalsIgnoreCase("docx");
    }

}
