package com.github.budgerigar.doc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import org.apache.commons.io.FilenameUtils;

/**
 * 
 * @Description: DefaultContentReader
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public class DefaultContentReader implements FileContentReader {

    @Override
    public Iterator<FileContent> readContent(Path path, Context context) throws IOException {
        Charset charset = (Charset) context.getAttr(Context.FILE_CHARSET, defaultCharset);
        String str = Files.readString(path, charset);
        return new SingleElementIterator<FileContent>(new FileContent(path, str));
    }

    @Override
    public boolean supportExtensions(Path path, Context context) {
        String ext = FilenameUtils.getExtension(path.getFileName().toString());
        return "txt".equalsIgnoreCase(ext);
    }

}
