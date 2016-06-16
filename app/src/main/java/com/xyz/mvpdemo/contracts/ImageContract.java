package com.xyz.mvpdemo.contracts;

import com.android.volley.VolleyError;
import com.xyz.mvpdemo.base.BasePresenter;
import com.xyz.mvpdemo.base.BaseView;
import com.xyz.mvpdemo.model.bean.ImageBean;

/**
 * 作者：xy_z on 2016/5/28 15:11
 * 邮箱：xyz@163.com
 */
public interface ImageContract {
    interface View extends BaseView<Presenter> {

        void showMovies(ImageBean data);

        void setLoadingIndicator(boolean flag);

        void showNoMovies();

        void isMoreLoading(boolean flag);
    }

    interface Presenter extends BasePresenter {

        void loadMore(int max, Object tag);

        void success(ImageBean movie);

        void error(VolleyError error);
    }
}
