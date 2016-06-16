package com.xyz.mvpdemo.Util;

import android.content.Context;
import android.widget.Toast;

import com.xyz.mvpdemo.base.MyApplication;

/**
 * 作者：xy_z on 2016/5/4 11:01
 * 邮箱：xyz@163.com
 */
public class MyToast {
    private static Toast mToast;

    public static void showToast(String text) {

        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void showToast(int id) {

        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getContext(), id, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(id);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

}
