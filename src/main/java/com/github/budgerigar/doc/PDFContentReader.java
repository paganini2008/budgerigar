package com.github.budgerigar.doc;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * 
 * @Description: PDFContentReader
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public class PDFContentReader implements FileContentReader {

    @Override
    public Iterator<FileContent> readContent(Path path, Context context) throws IOException {
        try (PDDocument document = Loader.loadPDF(path.toFile())) {
            if (document.isEncrypted()) {
                return IteratorUtils.emptyIterator();
            }
            PDFTextStripper textStripper = new PDFTextStripper();
            String pdfText = textStripper.getText(document);
            return new SingleElementIterator<FileContent>(new FileContent(path, pdfText));
        }
    }

    @Override
    public boolean supportExtensions(Path path, Context context) {
        String ext = FilenameUtils.getExtension(path.getFileName().toString());
        return "pdf".equalsIgnoreCase(ext);
    }

    public static void main(String[] args) throws IOException {
        PDFContentReader contentReader = new PDFContentReader();
        System.out.println(contentReader
                .readContent(Paths.get("F:\\work\\findNewJob\\forJob\\YanFengResume.pdf"), null));
    }

}
