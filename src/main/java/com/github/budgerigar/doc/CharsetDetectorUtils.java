package com.github.budgerigar.doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.mozilla.universalchardet.UniversalDetector;

import lombok.experimental.UtilityClass;

/**
 * 
 * @Description: CharsetDetector
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
@UtilityClass
public class CharsetDetectorUtils {

    public String detectCharset(File file) throws IOException {
        byte[] buf = new byte[4096];
        FileInputStream fis = new FileInputStream(file);
        try {
            UniversalDetector detector = new UniversalDetector(null);
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            detector.dataEnd();
            String encoding = detector.getDetectedCharset();
            detector.reset();
            return encoding;
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(detectCharset(new File("G:/nexus_zh.xlsx")));
    }

}
