package com.github.budgerigar.doc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 
 * @Description: HtmlContentReader
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public class HtmlContentReader implements FileContentReader {

    @Override
    public Iterator<FileContent> readContent(Path path, Context context) throws IOException {
        Charset charset = (Charset) context.getAttr(Context.FILE_CHARSET, defaultCharset);
        Document document = Jsoup.parse(path, charset.displayName());
        String fileName = path.getFileName().toString();
        String title = document.title();
        if (StringUtils.isBlank(title)) {
            title = FilenameUtils.getBaseName(fileName);
        }
        String bodyText = document.body().text();
        return new SingleElementIterator<FileContent>(new FileContent(fileName, title,
                FilenameUtils.getExtension(fileName), path.toUri().toString(),
                Files.getLastModifiedTime(path).toMillis(), bodyText));
    }

    @Override
    public boolean supportExtensions(Path path, Context context) {
        String ext = FilenameUtils.getExtension(path.getFileName().toString());
        return "html".equalsIgnoreCase(ext) || "htm".equalsIgnoreCase(ext);
    }

}
