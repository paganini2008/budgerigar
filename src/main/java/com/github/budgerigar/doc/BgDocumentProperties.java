package com.github.budgerigar.doc;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @Description: BgDocumentProperties
 * @Author: Fred Feng
 * @Date: 15/12/2024
 * @Version 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("budgerigar.document")
public class BgDocumentProperties {

    private FileMonitor monitor = new FileMonitor();

    @Getter
    @Setter
    public static class FileMonitor {

        private String baseDir = new File(FileUtils.getUserDirectory(), ".bdg").getAbsolutePath();
        private int intervalSeconds = 60;

    }

    public static void main(String[] args) {
        System.out.println(new File(FileUtils.getUserDirectory(), ".bdg").getAbsolutePath());
    }

}
