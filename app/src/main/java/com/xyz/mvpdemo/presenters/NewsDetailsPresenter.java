package com.xyz.mvpdemo.presenters;

import android.support.annotation.NonNull;

import com.android.volley.VolleyError;
import com.xyz.mvpdemo.contracts.NewsdetailsContract;
import com.xyz.mvpdemo.model.bean.NewsDetail;
import com.xyz.mvpdemo.model.http.RequestManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作者：xy_z on 2016/6/1 09:39
 * 邮箱：xyz@163.com
 */
public class NewsDetailsPresenter implements NewsdetailsContract.Presenter {
    private NewsdetailsContract.View mView;

    public NewsDetailsPresenter(@NonNull NewsdetailsContract.View view) {
        mView = view;
        EventBus.getDefault().register(this);
    }

    @Override
    public void start(String id, Object tag) {
        RequestManager.getNewsDetails(id, tag);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void success(@NonNull NewsDetail newsDetail) {
        mView.showDetails(newsDetail);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void error(VolleyError error) {
    }

    @Override
    public void finish() {
        if (mView != null) {
            mView = null;
        }
        EventBus.getDefault().unregister(this);
    }
}
