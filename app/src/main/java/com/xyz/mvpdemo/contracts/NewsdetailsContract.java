package com.xyz.mvpdemo.contracts;

import com.android.volley.VolleyError;
import com.xyz.mvpdemo.model.bean.NewsDetail;

/**
 * 作者：xy_z on 2016/6/1 09:25
 * 邮箱：xyz@163.com
 * 契约类
 */
public interface NewsdetailsContract {

    interface View {
        void showDetails(NewsDetail newsDetail);
    }


    interface Presenter {
        void start(String id, Object tag);

        void success(NewsDetail newsDetail);

        void error(VolleyError error);

        void finish();
    }
}
