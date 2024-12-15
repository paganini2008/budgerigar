package com.github.budgerigar.doc;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 
 * @Description: BgDocumentStore
 * @Author: Fred Feng
 * @Date: 15/12/2024
 * @Version 1.0.0
 */
public interface BgDocumentStore {

    Path save(Path path) throws IOException;

    boolean delete(Path path) throws IOException;

}
