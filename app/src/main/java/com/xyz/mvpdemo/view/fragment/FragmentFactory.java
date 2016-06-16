package com.xyz.mvpdemo.view.fragment;

import com.xyz.mvpdemo.base.BaseFragment;

import java.util.HashMap;

/**
 * 作者：xy_z on 2016/5/28 14:20
 * 邮箱：xyz@163.com
 * 描述：Fragment工厂类，通过HashMap缓存以创建的Fragment
 */
public class FragmentFactory {

    public static final int NEWS_FG = 0;    //新闻
    public static final int MOVIES_FG = 1;  //电影
    public static final int IMG_FG = 2;     //图片
    private static HashMap<Integer, BaseFragment> mFragments = new HashMap<>();

    /**
     * 获取Fragment
     *
     * @param type Fragment类型
     * @return 返回Fragment
     */
    public static BaseFragment createFragment(int type) {
        // 从缓存中取出
        BaseFragment fragment = mFragments.get(type);
        if (null == fragment) {
            switch (type) {
                case NEWS_FG:
                    fragment = new NewsFragmnet();
                    break;
                case MOVIES_FG:
                    fragment = new MoviesFragmnet();
                    break;
                case IMG_FG:
                    fragment = new ImagesFragmnet();
                    break;
            }
            // 把frament加入到缓存中
            mFragments.put(type, fragment);
        }
        return fragment;
    }
}
