package com.xyz.mvpdemo.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.xyz.mvpdemo.R;
import com.xyz.mvpdemo.base.BaseActivity;
import com.xyz.mvpdemo.base.MyApplication;
import com.xyz.mvpdemo.view.custom.RevealBackgroundView;

/**
 * 作者：xy_z on 2016/5/31 11:14
 * 邮箱：xyz@163.com
 * 描述：通过webview加载的详情界面，与新闻详情不同个，这里只是直接加载的，我对webView 的优化还不精通，如果你有更好的想法，欢迎更改
 */
public class MovieDetailsActivity extends BaseActivity implements RevealBackgroundView.OnStateChangeListener {
    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    private ProgressBar progressBar;
    private WebView web;
    private RevealBackgroundView reveal_view;
    private LinearLayout ll;


    @Override
    public int getLayoutResource() {
        return R.layout.activity_movie_details;
    }

    public static void startUserProfileFromLocation(int[] startingLocation, Activity startingActivity, String url) {
        Intent intent = new Intent(startingActivity, MovieDetailsActivity.class);
        intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
        intent.putExtra("movieurl", url);
        startingActivity.startActivity(intent);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.addActivity(this);
        overridePendingTransition(0, 0);
        reveal_view = (RevealBackgroundView) findViewById(R.id.reveal_view);
        ll = (LinearLayout) findViewById(R.id.ll);
        web = (WebView) findViewById(R.id.movie_web);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        setupRevealBackgroundView(savedInstanceState);

    }

    private void setupRevealBackgroundView(Bundle savedInstanceState) {
        reveal_view.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);
            reveal_view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    reveal_view.getViewTreeObserver().removeOnPreDrawListener(this);
                    reveal_view.startFromLocation(startingLocation);
                    return true;
                }
            });
        } else {
            reveal_view.setToFinish();
        }
    }


    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            reveal_view.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
            Intent intent = getIntent();
            if (null != intent) {
                String movieurl = intent.getStringExtra("movieurl");
                initweb(movieurl);
            }
        }
    }


    private void initweb(String movieurl) {
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax(100);
                //设置当前的进度
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    //隐藏进度条
                    progressBar.setVisibility(View.GONE);

                }
                super.onProgressChanged(view, newProgress);
            }
        });
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) view.loadUrl(url);
                return true;
            }
        });
        web.loadUrl(movieurl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (web != null) {
            web.removeAllViews();
            web.destroy();
            MyApplication.removeActivity(this);
        }
    }
}
