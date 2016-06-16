package com.xyz.mvpdemo.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xy_z on 2016/5/21 13:20
 * 邮箱：xyz@163.com
 */
public class MyApplication extends Application {
    private static List<Activity> mActivities = new ArrayList<>();
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        mActivities.remove(activity);
    }

    public static void exitAll() {
        for (Activity activity : mActivities) {
            activity.finish();
        }
        System.exit(0);
    }
}
