package com.github.budgerigar.doc;

import java.nio.file.Path;
import java.util.Iterator;

/**
 * 
 * @Description: ZipFileContentReader
 * @Author: Fred Feng
 * @Date: 16/12/2024
 * @Version 1.0.0
 */
public class ZipFileContentReader implements FileContentReader {

    @Override
    public Iterator<FileContent> readContent(Path path, Context context) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean supportExtensions(Path path, Context context) {
        // TODO Auto-generated method stub
        return false;
    }

}
