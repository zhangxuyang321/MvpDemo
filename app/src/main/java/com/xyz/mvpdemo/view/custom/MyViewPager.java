package com.xyz.mvpdemo.view.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * 作者：xy_z on 2016/6/3 11:22
 * 邮箱：xyz@163.com
 * 自定义Viewpage,宽度进行重绘
 */
public class MyViewPager extends ViewPager {

    private DisplayMetrics displayMetrics;

    public MyViewPager(Context context) {
        super(context);
        init();
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        displayMetrics = getContext().getResources().getDisplayMetrics();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                displayMetrics.widthPixels - getPageMargin() * 16,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
