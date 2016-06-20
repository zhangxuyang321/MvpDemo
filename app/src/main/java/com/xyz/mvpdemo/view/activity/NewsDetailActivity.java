package com.xyz.mvpdemo.view.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xyz.mvpdemo.R;
import com.xyz.mvpdemo.Util.Port;
import com.xyz.mvpdemo.base.BaseActivity;
import com.xyz.mvpdemo.base.MyApplication;
import com.xyz.mvpdemo.contracts.NewsdetailsContract;
import com.xyz.mvpdemo.model.bean.NewsDetail;
import com.xyz.mvpdemo.model.http.RequestManager;
import com.xyz.mvpdemo.presenters.NewsDetailsPresenter;

/**
 * 作者：xy_z on 2016/5/31 11:14
 * 邮箱：xyz@163.com
 * 描述：新闻详情采用是webview解析已下载好的html文件，相对于电影详情webView请求网络加载页面要流畅很多
 *      这个界面存在内存泄漏的问题，目前还没有解决
 */
public class NewsDetailActivity extends BaseActivity implements NewsdetailsContract.View {

    private ImageView news_heard;
    private WebView wb;
    private NewsDetailsPresenter presenter;
    private FrameLayout web_fl;
    private WebSettings settings;
    private ProgressBar progressBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private String id;
    private boolean isFirst = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.addActivity(this);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        news_heard = (ImageView) findViewById(R.id.news_header);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        web_fl = (FrameLayout) findViewById(R.id.web_fl);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        id = getIntent().getStringExtra("newsId");
        presenter = new NewsDetailsPresenter(this);
        presenter.start(id, this);

        assert toolbar != null;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.news_detail;
    }

    @Override
    public void showDetails(final NewsDetail newsDetail) {
        collapsingToolbarLayout.setTitle(newsDetail.title);
        Glide.with(this)
                .load(newsDetail.image)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(news_heard);
        news_heard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //这个方法会调用多次，所以价格判断进行控制
                if (isFirst) {
                    isFirst = false;
                    initWebView(newsDetail.body);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wb != null) {
            wb.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wb != null) {
            wb.onPause();
        }
    }

    private void initWebView(String body) {
        wb = new WebView(this);
        web_fl.addView(wb);
        settings = wb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        // 设置webview的缩放级别
        //settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        wb.setWebChromeClient(new WebChromeClient() {
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
        wb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) view.loadUrl(url);
                return true;
            }
        });
        wb.loadDataWithBaseURL(Port.newsdetails, body, "text/html", "uft-8", null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.finish();
            presenter = null;
        }
        if (wb != null) {
            wb.removeAllViews();
            wb.destroy();
        }
        if (web_fl != null) {
            web_fl.clearAnimation();
        }
        RequestManager.cancelAll(this);
        MyApplication.removeActivity(this);
    }
}
