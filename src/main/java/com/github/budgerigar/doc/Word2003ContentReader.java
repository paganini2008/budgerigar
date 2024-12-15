package com.github.budgerigar.doc;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

/**
 * 
 * @Description: Word2003ContentReader
 * @Author: Fred Feng
 * @Date: 01/12/2024
 * @Version 1.0.0
 */
public class Word2003ContentReader implements FileContentReader {

    @Override
    public Iterator<FileContent> readContent(Path path, Context context) throws Exception {
        try (InputStream fis = Files.newInputStream(path);
                HWPFDocument document = new HWPFDocument(fis);
                WordExtractor extractor = new WordExtractor(document)) {
            String content = extractor.getText();
            return new SingleElementIterator<FileContent>(new FileContent(path, content));
        }
    }

    @Override
    public boolean supportExtensions(Path path, Context context) {
        String ext = FilenameUtils.getExtension(path.getFileName().toString());
        return ext.equalsIgnoreCase("doc");
    }

    public static void main(String[] args) throws Exception {
        Word2003ContentReader contentReader = new Word2003ContentReader();
        Iterator<FileContent> it =
                contentReader.readContent(Paths.get("G:\\文档们\\我的简历_新.doc"), new Context());
        System.out.println(it);
    }

}
