package com.github.budgerigar.doc;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @Description: FileContent
 * @Author: Fred Feng
 * @Date: 01/12/2024
 * @Version 1.0.0
 */
@Getter
@Setter
@ToString(exclude = {"content"})
public class FileContent {

    private final String name;
    private final String title;
    private final String extention;
    private final String path;
    private final LocalDateTime lastModified;
    private final String content;

    public FileContent(String name, String title, String extention, String path, long lastModified,
            String content) {
        if (StringUtils.isBlank(name)) {
            name = "Unknown";
        }
        this.name = name;
        if (StringUtils.isBlank(title)) {
            title = FilenameUtils.getBaseName(name);
        }
        this.title = title;
        this.extention = extention;
        this.path = path;
        this.lastModified =
                Instant.ofEpochMilli(lastModified).atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.content = content;
    }

    public FileContent(Path path, String content) {
        final File file = path.toFile();
        this.name = file.getName();
        this.title = FilenameUtils.getBaseName(file.getName());
        this.extention = FilenameUtils.getExtension(file.getName());
        this.path = file.toURI().toString();
        this.lastModified = Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        this.content = content;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("F:\\work\\findNewJob\\forJob\\YanFengResume.pdf");
        URI uri = file.toURI();
        System.out.println(uri.toString());
    }



}
