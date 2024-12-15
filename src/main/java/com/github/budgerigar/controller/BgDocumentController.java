package com.github.budgerigar.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.github.budgerigar.doc.BgDocumentProperties;
import com.github.doodler.common.ApiResult;

/**
 * 
 * @Description: BgDocumentController
 * @Author: Fred Feng
 * @Date: 15/12/2024
 * @Version 1.0.0
 */
@RequestMapping("/bgd")
@RestController
public class BgDocumentController {

    @Autowired
    private BgDocumentProperties bgDocumentProperties;

    @PostMapping("/save")
    public ApiResult<String> save(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.getSize() == 0) {
            throw new IllegalStateException("Empty file");
        }
        String fileName = file.getOriginalFilename();
        InputStream in = file.getInputStream();
        Path tempFile = Files.createTempFile(fileName, "");
        try (OutputStream output = Files.newOutputStream(tempFile)) {
            StreamUtils.copy(in, output);
        }
        Path actualPath = Paths.get(bgDocumentProperties.getMonitor().getBaseDir());
        Path actualFile = actualPath.resolve(fileName);
        Files.copy(tempFile, actualFile);
        Files.delete(tempFile);
        return ApiResult.ok(actualFile.toString());
    }

}
