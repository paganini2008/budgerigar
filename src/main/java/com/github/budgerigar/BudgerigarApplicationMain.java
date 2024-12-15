package com.github.budgerigar;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 
 * @Description: BudgerigarApplicationMain
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
@EnableAsync
@SpringBootApplication
public class BudgerigarApplicationMain {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    public static void main(String[] args) {
        SpringApplication.run(BudgerigarApplicationMain.class, args);
    }

}
