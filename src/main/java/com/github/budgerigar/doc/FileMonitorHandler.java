package com.github.budgerigar.doc;

import java.io.File;

/**
 * 
 * @Description: FileMonitorHandler
 * @Author: Fred Feng
 * @Date: 15/12/2024
 * @Version 1.0.0
 */
public interface FileMonitorHandler {

    default void onDirectoryChange(File directory) {

    }

    default void onDirectoryCreate(File directory) {

    }

    default void onDirectoryDelete(File directory) {

    }

    default void onFileChange(File file) {

    }

    default void onFileCreate(File file) {

    }

    default void onFileDelete(File file) {

    }
}
