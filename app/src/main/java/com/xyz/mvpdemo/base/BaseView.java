package com.xyz.mvpdemo.base;

/**
 * 作者：xy_z on 2016/5/28 15:08
 * 邮箱：xyz@163.com
 * 描述：所有view的父类，将公共的方法提取到父类
 */
public interface BaseView<T> {

    void setPresenter(T presenter);
}
