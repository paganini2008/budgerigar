package com.github.budgerigar.doc;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections4.IteratorUtils;
import com.github.budgerigar.BgDocumentIndexer;
import com.github.budgerigar.pojo.BgDocumentDto;
import com.github.doodler.common.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: FileWalker
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class FileWalker extends SimpleFileVisitor<Path> {

    private final File startDirectory;
    private final Context context;
    private final List<FileContentReader> fileContentReaders;
    private final BgDocumentIndexer bgDocumentIndexer;

    public void walk() throws IOException {
        Files.walkFileTree(startDirectory.toPath(), this);
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        FileVisitResult result = super.visitFile(path, attrs);
        fileContentReaders.stream().filter(fcr -> fcr.supportExtensions(path, context)).findFirst()
                .ifPresent(fcr -> handleFileContents(fcr, path));
        return result;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
        }
        return super.visitFileFailed(file, e);
    }

    private void handleFileContents(FileContentReader fcr, Path path) {
        Iterator<FileContent> iter = IteratorUtils.emptyIterator();
        try {
            iter = fcr.readContent(path, context);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
        while (iter.hasNext()) {
            try {
                FileContent fileContent = iter.next();
                BgDocumentDto bgDocument = BeanCopyUtils.copyBean(fileContent, BgDocumentDto.class);
                int rows = bgDocumentIndexer.saveDocument(bgDocument);
                log.info("Handled File: {}, Effected rows: {}", fileContent, rows);
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // FileWalker fileProcessor = new FileWalker(new Context(), null);
        // fileProcessor.walk(new File("F:\\work\\findNewJob"));
        // System.out.println();
    }



    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            throws IOException {
        // TODO Auto-generated method stub
        return super.preVisitDirectory(dir, attrs);
    }



    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        // TODO Auto-generated method stub
        return super.postVisitDirectory(dir, exc);
    }



}
