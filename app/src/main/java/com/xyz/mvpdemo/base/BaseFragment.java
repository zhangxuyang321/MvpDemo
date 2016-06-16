package com.xyz.mvpdemo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：xy_z on 2016/5/3 11:49
 * 邮箱：xyz@163.com
 */
public abstract class BaseFragment extends Fragment {
    public Context context;
    public View view;
    public Map<String, String> map;
    public Gson gson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        map = new HashMap<>();
        gson = new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null ){
            view = initView(inflater);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initData(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
    }

    public abstract View initView(LayoutInflater inflater);

    protected abstract void initData(Bundle savedInstanceState);


}
