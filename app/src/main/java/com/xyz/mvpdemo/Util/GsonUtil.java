package com.xyz.mvpdemo.Util;

import com.google.gson.Gson;

/**
 * 作者：xy_z on 2016/5/30 16:06
 * 邮箱：xyz@163.com
 */
public class GsonUtil {
    private static Gson gson;

    private GsonUtil() {
    }

    public static Gson instance() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }
}
