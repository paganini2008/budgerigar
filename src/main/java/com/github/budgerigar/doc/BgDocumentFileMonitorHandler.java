package com.github.budgerigar.doc;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @Description: BgDocumentFileMonitorHandler
 * @Author: Fred Feng
 * @Date: 15/12/2024
 * @Version 1.0.0
 */
@Component
public class BgDocumentFileMonitorHandler implements FileMonitorHandler {

    @Autowired
    private BgDocumentManager documentManager;

    @Override
    public void onFileCreate(File file) {
        documentManager.saveDocument(file.toPath(), new Context());
    }

}
