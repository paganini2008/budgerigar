package com.github.budgerigar.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.doodler.common.ApiResult;

/**
 * 
 * @Description: IndexController
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
@RestController
public class IndexController {

    @GetMapping("/test")
    public ApiResult<String> test() {
        return ApiResult.ok();
    }

}
