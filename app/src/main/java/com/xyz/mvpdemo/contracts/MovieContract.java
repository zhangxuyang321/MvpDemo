package com.xyz.mvpdemo.contracts;

import com.android.volley.VolleyError;
import com.xyz.mvpdemo.base.BasePresenter;
import com.xyz.mvpdemo.base.BaseView;
import com.xyz.mvpdemo.model.bean.MovieBean;

import java.util.List;

/**
 * 作者：xy_z on 2016/5/28 15:11
 * 邮箱：xyz@163.com
 */
public interface MovieContract {
    interface View extends BaseView<Presenter> {
        void showMovies(List<MovieBean.ResultData> data);

        void setLoadingIndicator(boolean flag);

        void showNoMovies();
    }

    interface Presenter extends BasePresenter {

        void success(MovieBean movie);

        void error(VolleyError error);
    }
}
