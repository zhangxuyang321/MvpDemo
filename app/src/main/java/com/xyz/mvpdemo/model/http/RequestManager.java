package com.xyz.mvpdemo.model.http;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.xyz.mvpdemo.Util.MyToast;
import com.xyz.mvpdemo.Util.Port;
import com.xyz.mvpdemo.Util.Util;
import com.xyz.mvpdemo.base.MyApplication;
import com.xyz.mvpdemo.model.bean.ErrorBean;
import com.xyz.mvpdemo.model.bean.ImageBean;
import com.xyz.mvpdemo.model.bean.MovieBean;
import com.xyz.mvpdemo.model.bean.News;
import com.xyz.mvpdemo.model.bean.NewsDetail;
import com.xyz.mvpdemo.model.bean.UserBean;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：xy_z on 2016/5/21 13:11
 * 邮箱：xyz@163.com
 * 请求管理类，参考的是煎蛋的App的网络请求,并对错误处理进行了优化，这里全是get请求
 * 煎蛋github地址：https://github.com/ZhaoKaiQiang/JianDan
 */
public class RequestManager {
    public static final int OUT_TIME = 10000;   //设置超时时间
    public static final int TIME_OF_RETRY = 1;  //设置超时从新连接次数

    //获取Volley的请求队列
    public static RequestQueue mrRequestQueue = Volley.newRequestQueue(MyApplication.getContext());

    //私有化构造方法，只能通过静态方法添加Request请求，保证Volley的单例
    private RequestManager() {
    }

    /**
     * 新闻请求
     *
     * @param tag 请求标识
     */
    public static void getNews(Object tag) {
        Request4News request4News = new Request4News(Port.news, new Response.Listener<News>() {

            @Override
            public void onResponse(News news) {
                EventBus.getDefault().post(news.stories);
            }
        }, errorListener);

        addRequest(request4News, tag);
    }

    /**
     * 获取新闻详情
     *
     * @param id  新闻id
     * @param tag 标示
     */
    public static void getNewsDetails(String id, Object tag) {
        Request4NewsDetails request4NewsDetails = new Request4NewsDetails(Port.newsdetails + id, new Response.Listener<NewsDetail>() {
            @Override
            public void onResponse(NewsDetail newsDetail) {
                EventBus.getDefault().post(newsDetail);
            }
        }, errorListener);

        addRequest(request4NewsDetails, tag);
    }


    /**
     * 获取电影信息
     *
     * @param tag 标示
     */
    public static void getMovies(Object tag) {
        Request4Movie request4Movie = new Request4Movie(Port.movies, new Response.Listener<MovieBean>() {
            @Override
            public void onResponse(MovieBean response) {
                EventBus.getDefault().post(response);
            }
        }, errorListener);

        addRequest(request4Movie, tag);
    }


    /**
     * 获取美图浏览信息
     *
     * @param tag 请求标识
     */
    public static void getImages(int max, Object tag) {
        String url = "";
        if (max == -1) {
            url = Port.img;
        } else {
            url = String.format(Port.imgmore, max);
        }
        Request4Img request4Img = new Request4Img(url, new Response.Listener<ImageBean>() {
            @Override
            public void onResponse(ImageBean response) {
                EventBus.getDefault().post(response);
            }
        }, errorListener);
        addRequest(request4Img, tag);
    }


    public static void downloadImg(String url, Object tag) {
        Request4downloadImg request4downloadImg = new Request4downloadImg(url, errorListener);
        addRequest(request4downloadImg, tag);
    }


    /**
     * 错误处理，
     * 这里的错误分成了两类，
     * 一类是网络的错误或者什么原因导致请求失败的真正错误(这类错误按Volley原先那样处理)
     * 另一类是请求已经成功了，但是数据不正确，例如登录请求的用户名密码不对的错误
     * 如何区分？这个应用的网络接口返回的数据有两种，一种是对应请求的数据，一种是错误的数据，我通过捕捉Gson的解析的错误来进行区分的
     * 这里其实是对处理结果的展示，真正的错误处理请参考每一个Request的parseNetworkResponse方法
     * <p>
     * ps:因为接口用了好多家的错误返回的字段不一样，可以在errorBean里添加对应地字段
     */
    private static Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            MyToast.showToast(error.getMessage());
            EventBus.getDefault().post(error);
        }
    };


    /**
     * 添加网络请求
     *
     * @param request 网络请求
     * @param tag     网络请求标记
     */
    private static void addRequest(Request<?> request, Object tag) {
        if (null != tag) {
            request.setTag(tag);
        }
        //重新设置超时时间，以及重试次数
        request.setRetryPolicy(new DefaultRetryPolicy(OUT_TIME, TIME_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //检测网络
        if (!Util.hasConnectedNetwork(MyApplication.getContext())) {
            MyToast.showToast("当前没有可用网络~");
            EventBus.getDefault().post(new VolleyError());
            return;
        }

        //将请求添加到队列中
        mrRequestQueue.add(request);

    }

    /**
     * 取消 某一标记的所有请求
     *
     * @param tag 请求标记
     */
    public static void cancelAll(Object tag) {
        mrRequestQueue.cancelAll(tag);
    }
}
