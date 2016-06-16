package com.xyz.mvpdemo.presenters;

import com.xyz.mvpdemo.base.BaseFragment;
import com.xyz.mvpdemo.base.BasePresenter;
import com.xyz.mvpdemo.contracts.ImageContract;
import com.xyz.mvpdemo.contracts.MovieContract;
import com.xyz.mvpdemo.contracts.NewsContract;

import java.util.HashMap;

/**
 * 作者：xy_z on 2016/5/29 19:27
 * 邮箱：xyz@163.com
 * 描述：presneter工厂类
 */
public class PresenterFactory {

    public static final int NEWS_pt = 0;    //新闻
    public static final int MOVIES_pt = 1;  //电影
    public static final int IMG_pt = 2;     //图片

    private static HashMap<Integer, BasePresenter> mPresenters = new HashMap<>();

    public static BasePresenter createPresenter(int type, BaseFragment fragment) {
        BasePresenter presenter = mPresenters.get(type);
        if (null == presenter) {
            switch (type) {
                case NEWS_pt:
                    presenter = new NewsPresenter((NewsContract.View) fragment);
                    break;
                case MOVIES_pt:
                    presenter = new MoviePresenter((MovieContract.View) fragment);
                    break;
                case IMG_pt:
                    presenter = new ImagePresenter((ImageContract.View) fragment);
                    break;
            }

            mPresenters.put(type, presenter);
        }
        return presenter;
    }
}
