package com.xyz.mvpdemo.model.http;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * 作者：xy_z on 2016/6/16 09:28
 * 邮箱：xyz@163.com
 */
public class Request4downloadImg extends Request {


    public Request4downloadImg(String url, Response.ErrorListener listener) {
        super(Method.GET, url, listener);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        byte[] data = response.data;
        return null;
    }

    @Override
    protected void deliverResponse(Object response) {

    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
