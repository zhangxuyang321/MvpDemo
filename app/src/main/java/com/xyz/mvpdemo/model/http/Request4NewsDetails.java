package com.xyz.mvpdemo.model.http;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;
import com.xyz.mvpdemo.Util.GsonUtil;
import com.xyz.mvpdemo.model.bean.ErrorBean;
import com.xyz.mvpdemo.model.bean.NewsDetail;

import java.io.UnsupportedEncodingException;

/**
 * 作者：xy_z on 2016/6/1 10:31
 * 邮箱：xyz@163.com
 */
public class Request4NewsDetails extends Request<NewsDetail> {
    private Response.Listener<NewsDetail> mListener;

    public Request4NewsDetails(String url, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<NewsDetail> parseNetworkResponse(NetworkResponse response) {
        String parsed = null;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            NewsDetail newsDetail = GsonUtil.instance().fromJson(parsed, NewsDetail.class);
            return Response.success(newsDetail, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonSyntaxException e) {
            ErrorBean errorBean = GsonUtil.instance().fromJson(parsed, ErrorBean.class);
            return Response.error(new ResponseError(errorBean.reason));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(NewsDetail response) {
        if (null != mListener) {
            mListener.onResponse(response);
        }
    }
}
