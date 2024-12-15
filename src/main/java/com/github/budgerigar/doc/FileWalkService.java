package com.github.budgerigar.doc;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.github.budgerigar.BgDocumentIndexer;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @Description: FileWalkService
 * @Author: Fred Feng
 * @Date: 15/12/2024
 * @Version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class FileWalkService {

    private final List<FileContentReader> fileContentReaders;
    private final BgDocumentIndexer bgDocumentIndexer;

    @Async
    public void runFileWalk(File directory) throws IOException {
        FileWalker fileWalker =
                new FileWalker(directory, new Context(), fileContentReaders, bgDocumentIndexer);
        fileWalker.walk();
    }

}
