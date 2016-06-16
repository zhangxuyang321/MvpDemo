package com.xyz.mvpdemo.model.http;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;
import com.xyz.mvpdemo.Util.GsonUtil;
import com.xyz.mvpdemo.Util.Util;
import com.xyz.mvpdemo.model.bean.ErrorBean;
import com.xyz.mvpdemo.model.bean.News;
import java.io.UnsupportedEncodingException;

/**
 * 作者：xy_z on 2016/5/30 16:02
 * 邮箱：xyz@163.com
 */
public class Request4News extends Request<News> {

    private Response.Listener<News> mListener;

    public Request4News(String url, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<News> parseNetworkResponse(NetworkResponse response) {
        String parsed = null;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Util.LogUtil("-=-=-=-=-=-=-=-=-=-=-=-",parsed);
            News news = GsonUtil.instance().fromJson(parsed, News.class);
            return Response.success(news, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonSyntaxException e) {
            ErrorBean errorBean = GsonUtil.instance().fromJson(parsed, ErrorBean.class);
            return Response.error(new ResponseError(errorBean.reason));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(News response) {
        if (null != mListener) {
            mListener.onResponse(response);
        }
    }
}
