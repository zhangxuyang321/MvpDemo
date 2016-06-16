package com.xyz.mvpdemo.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 作者：xy_z on 2016/5/31 20:45
 * 邮箱：xyz@163.com
 * 手势右滑返回
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        initPresenter();
    }

    protected void initPresenter(){
        switch (getLayoutResource()){

        }
    }

    public abstract int getLayoutResource();
}
