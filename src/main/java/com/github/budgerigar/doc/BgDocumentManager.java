package com.github.budgerigar.doc;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Component;
import com.github.budgerigar.BgDocument;
import com.github.budgerigar.BgDocumentIndexer;
import com.github.doodler.common.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: BgDocumentManager
 * @Author: Fred Feng
 * @Date: 15/12/2024
 * @Version 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BgDocumentManager {

    private final List<FileContentReader> fileContentReaders;
    private final BgDocumentIndexer bgDocumentIndexer;

    public void saveDocument(Path path, Context context) {
        fileContentReaders.stream().filter(fcr -> fcr.supportExtensions(path, context)).findFirst()
                .ifPresent(fcr -> handleFileContents(fcr, path, context));
    }

    private void handleFileContents(FileContentReader fcr, Path path, Context context) {
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
                BgDocument bgDocument = BeanCopyUtils.copyBean(fileContent, BgDocument.class);
                int rows = bgDocumentIndexer.saveDocument(bgDocument);
                log.info("Handled File: {}, Effected rows: {}", fileContent, rows);
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

}
