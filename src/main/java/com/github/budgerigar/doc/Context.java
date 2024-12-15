package com.github.budgerigar.doc;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @Description: Context
 * @Author: Fred Feng
 * @Date: 30/11/2024
 * @Version 1.0.0
 */
public final class Context {

    public static final String FILE_CHARSET = "FILE_CHARSET";

    private final Map<String, Object> attrs = new HashMap<>();

    public void setAttr(String name, Object value) {
        if (value != null) {
            attrs.put(name, value);
        } else {
            attrs.remove(name);
        }
    }

    public Object getAttr(String name) {
        return getAttr(name, null);
    }

    public Object getAttr(String name, Object defaultValue) {
        return attrs.getOrDefault(name, defaultValue);
    }

}
