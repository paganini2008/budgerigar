package com.github.budgerigar.doc;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * 
 * @Description: FileContentReader
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public interface FileContentReader {

    static final Charset defaultCharset = StandardCharsets.UTF_8;

    Iterator<FileContent> readContent(Path path, Context context) throws Exception;

    boolean supportExtensions(Path path, Context context);

}
