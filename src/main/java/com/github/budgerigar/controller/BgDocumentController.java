package com.github.budgerigar.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.github.budgerigar.BgDocumentIndexer;
import com.github.budgerigar.doc.BgDocumentProperties;
import com.github.budgerigar.pojo.BgDocumentPageQuery;
import com.github.budgerigar.pojo.BgDocumentVo;
import com.github.doodler.common.ApiResult;
import com.github.doodler.common.page.PageVo;

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

    @Autowired
    private BgDocumentIndexer bgDocumentIndexer;

    @PostMapping("/page")
    public ApiResult<PageVo<BgDocumentVo>> pageForDocument(
            @RequestBody BgDocumentPageQuery pageQuery) {
        PageVo<BgDocumentVo> pageVo = bgDocumentIndexer.pageForDocument(pageQuery);
        return ApiResult.ok(pageVo);
    }

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
        if (Files.exists(actualFile)) {
            Files.deleteIfExists(actualFile);
        }
        Files.copy(tempFile, actualFile);
        Files.delete(tempFile);
        return ApiResult.ok(actualFile.toString());
    }

}
