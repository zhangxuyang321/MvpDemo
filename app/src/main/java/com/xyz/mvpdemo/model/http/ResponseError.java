package com.xyz.mvpdemo.model.http;

import com.android.volley.VolleyError;

/**
 * 作者：xy_z on 2016/5/21 15:53
 * 邮箱：xyz@163.com
 */
public class ResponseError extends VolleyError {
    public ResponseError(String exceptionMessage) {
        super(exceptionMessage);
    }
}
