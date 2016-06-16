package com.xyz.mvpdemo.contracts;

import com.android.volley.VolleyError;
import com.xyz.mvpdemo.base.BasePresenter;
import com.xyz.mvpdemo.base.BaseView;
import com.xyz.mvpdemo.model.bean.News;

import java.util.List;

/**
 * 作者：xy_z on 2016/5/28 15:11
 * 邮箱：xyz@163.com
 * 描述：View与Preshenter的契约类，参考了谷歌官方MVP架构的写法
 *       这里有详细的讲解http://mp.weixin.qq.com/s?__biz=MzA3ODg4MDk0Ng==&mid=403539764&idx=1&sn=d30d89e6848a8e13d4da0f5639100e5f#rd
 *
 *       一个模块到底需要哪些接口？ 建议去看鸿样大神的博客http://blog.csdn.net/lmj623565791/article/details/46596109
 *
 */
public interface NewsContract {

    /**
     * 新闻模块View对应的接口
     */
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean flag);  //是否正在加载数据

        void showNews(List<News.Stories> news);  //展示获取的数据

        void showNoNews();                       //请求服务器成功，但是并没有数据，用于展空界面的展现，Demo中虽然有这个接口但是并没用（懒~）

    }

    interface Presenter extends BasePresenter {

        void success(List<News.Stories> news);   //数据请求成功

        void error(VolleyError error);           //数据请求出现错误
    }
}
