package com.xyz.mvpdemo.model.http;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;
import com.xyz.mvpdemo.Util.GsonUtil;
import com.xyz.mvpdemo.model.bean.ErrorBean;
import com.xyz.mvpdemo.model.bean.ImageBean;
import java.io.UnsupportedEncodingException;

/**
 * 作者：xy_z on 2016/6/14 09:33
 * 邮箱：xyz@163.com
 */
public class Request4Img extends Request<ImageBean>{
    private Response.Listener<ImageBean> mListener;

    public Request4Img(String url, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<ImageBean> parseNetworkResponse(NetworkResponse response) {
        String parsed = null;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            ImageBean imageBean = GsonUtil.instance().fromJson(parsed, ImageBean.class);
            return Response.success(imageBean, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonSyntaxException e) {
            System.out.println(parsed);
            ErrorBean errorBean = GsonUtil.instance().fromJson(parsed, ErrorBean.class);
            return Response.error(new ResponseError(errorBean.reason));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ImageBean response) {
        if (null != mListener) {
            mListener.onResponse(response);
        }
    }
}
