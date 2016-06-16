package com.xyz.mvpdemo.presenters;

import android.support.annotation.NonNull;

import com.android.volley.VolleyError;
import com.xyz.mvpdemo.Util.MyToast;
import com.xyz.mvpdemo.contracts.NewsContract;
import com.xyz.mvpdemo.model.bean.News;
import com.xyz.mvpdemo.model.http.RequestManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 作者：xy_z on 2016/5/29 18:59
 * 邮箱：xyz@163.com
 * 描述：新闻对应的presenter，这里与Module之间用EventBus做了解耦
 *       EventBus详解请看https://segmentfault.com/a/1190000005089229
 */
public class NewsPresenter implements NewsContract.Presenter {
    private NewsContract.View mNewsView;

    public NewsPresenter(@NonNull NewsContract.View newsView) {
        mNewsView = newsView;
        mNewsView.setPresenter(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void start(Object tag) {
        RequestManager.getNews(tag);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void success(List<News.Stories> news) {
        mNewsView.setLoadingIndicator(false);
        if (news != null && news.size() > 0) {
            mNewsView.showNews(news);
        } else {
            mNewsView.showNoNews();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void error(VolleyError error) {
        mNewsView.setLoadingIndicator(false);
    }
}
