package com.github.budgerigar.doc;

import lombok.Data;

/**
 * 
 * @Description: FileMonitorProperties
 * @Author: Fred Feng
 * @Date: 15/12/2024
 * @Version 1.0.0
 */
@Data
public class FileMonitorProperties {

    private String baseDir;
    private int intervalMinutes = 1;

}
