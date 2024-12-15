package com.github.budgerigar.doc;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import com.github.doodler.common.context.ManagedBeanLifeCycle;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: FileMonitor
 * @Author: Fred Feng
 * @Date: 15/12/2024
 * @Version 1.0.0
 */
@Slf4j
public class FileMonitor implements FileAlterationListener, ManagedBeanLifeCycle {

    private String[] baseDirs;
    private int intervalSeconds = 60;

    public void setBaseDirs(String... baseDirs) {
        this.baseDirs = baseDirs;
    }

    public void setIntervalSeconds(int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    private List<FileMonitorHandler> fileMonitorHandlers;

    public void setFileMonitorHandlers(List<FileMonitorHandler> fileMonitorHandlers) {
        this.fileMonitorHandlers = fileMonitorHandlers;
    }

    @Override
    public void onDirectoryChange(File directory) {
        if (log.isInfoEnabled()) {
            log.info("Directory '{}' is changed", directory);
        }
        Optional.ofNullable(fileMonitorHandlers)
                .ifPresent(hs -> hs.forEach(h -> h.onDirectoryChange(directory)));
    }

    @Override
    public void onDirectoryCreate(File directory) {
        if (log.isInfoEnabled()) {
            log.info("Directory '{}' is created", directory);
        }
        Optional.ofNullable(fileMonitorHandlers)
                .ifPresent(hs -> hs.forEach(h -> h.onDirectoryCreate(directory)));
    }

    @Override
    public void onDirectoryDelete(File directory) {
        if (log.isInfoEnabled()) {
            log.info("Directory '{}' is deleted", directory);
        }
        Optional.ofNullable(fileMonitorHandlers)
                .ifPresent(hs -> hs.forEach(h -> h.onDirectoryDelete(directory)));
    }

    @Override
    public void onFileChange(File file) {
        if (log.isInfoEnabled()) {
            log.info("File '{}' is changed", file);
        }
        Optional.ofNullable(fileMonitorHandlers)
                .ifPresent(hs -> hs.forEach(h -> h.onFileChange(file)));
    }

    @Override
    public void onFileCreate(File file) {
        if (log.isInfoEnabled()) {
            log.info("File '{}' is created", file);
        }
        Optional.ofNullable(fileMonitorHandlers)
                .ifPresent(hs -> hs.forEach(h -> h.onFileCreate(file)));
    }

    @Override
    public void onFileDelete(File file) {
        if (log.isInfoEnabled()) {
            log.info("File '{}' is deleted", file);
        }
        Optional.ofNullable(fileMonitorHandlers)
                .ifPresent(hs -> hs.forEach(h -> h.onFileDelete(file)));
    }

    @Override
    public void onStart(FileAlterationObserver observer) {
        if (log.isInfoEnabled()) {
            log.info("FileAlterationObserver start checking");
        }
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        if (log.isInfoEnabled()) {
            log.info("FileAlterationObserver end checking");
        }
    }

    private FileAlterationMonitor fileAlterationMonitor;

    public void start() throws Exception {
        if (fileAlterationMonitor != null) {
            return;
        }
        int len = baseDirs != null ? baseDirs.length : 0;
        if (len == 0) {
            return;
        }
        FileAlterationObserver[] observers = new FileAlterationObserver[len];
        for (int i = 0; i < len; i++) {
            File baseDir = new File(baseDirs[i]);
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            observers[i] = FileAlterationObserver.builder().setFile(baseDir)
                    .setIOCase(IOCase.SENSITIVE).get();
            observers[i].addListener(this);
        }
        long interval = TimeUnit.SECONDS.toMillis(intervalSeconds);
        fileAlterationMonitor = new FileAlterationMonitor(interval, observers);
        fileAlterationMonitor.start();
        if (log.isInfoEnabled()) {
            log.info("File monitor is started.");
        }
    }

    public void stop() throws Exception {
        if (fileAlterationMonitor != null) {
            fileAlterationMonitor.stop();
            fileAlterationMonitor = null;
            if (log.isInfoEnabled()) {
                log.info("File monitor is stoped.");
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @Override
    public void destroy() throws Exception {
        stop();
    }

    public static void main(String[] args) throws Exception {
        FileMonitor fileMonitor = new FileMonitor();
        fileMonitor.setBaseDirs("d:/sql/test");
        fileMonitor.afterPropertiesSet();
        System.out.println("Begin...");
        System.in.read();
        fileMonitor.destroy();
        System.out.println("FileMonitor.main()");
    }

}
