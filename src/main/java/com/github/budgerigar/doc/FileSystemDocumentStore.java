package com.github.budgerigar.doc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

/**
 * 
 * @Description: FileSystemDocumentStore
 * @Author: Fred Feng
 * @Date: 15/12/2024
 * @Version 1.0.0
 */
@ConditionalOnProperty(name = "budgerigar.document.store", havingValue = "fs",
        matchIfMissing = true)
@Component
public class FileSystemDocumentStore implements BgDocumentStore {

    private static final String DEFAULT_STORE_NAME = ".bgd";
    private String rootPath = FileUtils.getUserDirectoryPath();

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public Path save(Path path) throws IOException {
        Path parentPath = Path.of(rootPath).resolve(DEFAULT_STORE_NAME)
                .resolve(System.getProperty("user.name"));
        Path outputPath = parentPath.resolve(path.getFileName());
        try (OutputStream out = Files.newOutputStream(outputPath);
                InputStream in = Files.newInputStream(path)) {
            StreamUtils.copy(in, out);
        }
        return outputPath;
    }

    @Override
    public boolean delete(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }

    public static void main(String[] args) {
        String rootPath = FileUtils.getUserDirectoryPath();
        Path path = Paths.get("g:/a/b/abc.txt");
        System.out.println(path.getParent());
        System.out.println(Paths.get("d:/bdg"));
        System.out.println(path.getFileSystem());
        System.out.println(path.getRoot());
        System.out.println(Files.isRegularFile(path));
        System.out.println(path.toUri());
        Path outputPath = Path.of(rootPath).resolve(".bgd").resolve(path.getFileName());
        System.out.println(outputPath);
        System.out.println(Paths.get(URI.create("file:///g:/abc.txt")));
    }



}
