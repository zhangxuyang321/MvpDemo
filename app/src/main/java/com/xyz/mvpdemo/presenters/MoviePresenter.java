package com.xyz.mvpdemo.presenters;

import android.support.annotation.NonNull;

import com.android.volley.VolleyError;
import com.xyz.mvpdemo.contracts.MovieContract;
import com.xyz.mvpdemo.model.bean.MovieBean;
import com.xyz.mvpdemo.model.http.RequestManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作者：xy_z on 2016/5/29 18:59
 * 邮箱：xyz@163.com
 */
public class MoviePresenter implements MovieContract.Presenter {
    private MovieContract.View mMovieView;

    public MoviePresenter(@NonNull MovieContract.View movieView) {
        mMovieView = movieView;
        mMovieView.setPresenter(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void start(Object tag) {
        RequestManager.getMovies(tag);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void success(MovieBean movie) {
        mMovieView.setLoadingIndicator(false);
        if (movie != null) {
            mMovieView.showMovies(movie.result.data);
        } else {
            mMovieView.showNoMovies();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void error(VolleyError error) {
        mMovieView.setLoadingIndicator(false);
    }
}
