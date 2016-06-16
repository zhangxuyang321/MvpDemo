package com.xyz.mvpdemo.presenters;

import android.support.annotation.NonNull;

import com.android.volley.VolleyError;
import com.xyz.mvpdemo.contracts.ImageContract;
import com.xyz.mvpdemo.model.bean.ImageBean;
import com.xyz.mvpdemo.model.http.RequestManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作者：xy_z on 2016/5/29 18:59
 * 邮箱：xyz@163.com
 */
public class ImagePresenter implements ImageContract.Presenter {
    private ImageContract.View mImageView;

    public ImagePresenter(@NonNull ImageContract.View newsView) {
        mImageView = newsView;
        mImageView.setPresenter(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void start(Object tag) {
        loadMore(-1, tag);
    }

    @Override
    public void loadMore(int max, Object tag) {
        mImageView.isMoreLoading(true);
        RequestManager.getImages(max, tag);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void success(ImageBean img) {
        mImageView.isMoreLoading(false);
        mImageView.setLoadingIndicator(false);
        if (img != null) {
            mImageView.showMovies(img);
        } else {
            mImageView.showNoMovies();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void error(VolleyError error) {
        mImageView.isMoreLoading(false);
        mImageView.setLoadingIndicator(false);
    }
}
