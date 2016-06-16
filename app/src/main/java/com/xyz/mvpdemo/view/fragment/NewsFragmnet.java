package com.xyz.mvpdemo.view.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.xyz.mvpdemo.MainActivity;
import com.xyz.mvpdemo.R;
import com.xyz.mvpdemo.base.BaseFragment;
import com.xyz.mvpdemo.contracts.NewsContract;
import com.xyz.mvpdemo.model.bean.News;
import com.xyz.mvpdemo.model.http.RequestManager;
import com.xyz.mvpdemo.view.activity.NewsDetailActivity;
import com.xyz.mvpdemo.view.adapters.NewsApapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xy_z on 2016/5/28 14:23
 * 邮箱：xyz@163.com
 * 描述：新闻模块 很好理解 没有什么特殊的地方，动画什么的都是在adapter中做的
 */
public class NewsFragmnet extends BaseFragment implements NewsContract.View, SwipeRefreshLayout.OnRefreshListener {


    private NewsContract.Presenter mPresenter;
    private SwipeRefreshLayout refreshLayout;
    private List<News.Stories> newsData = new ArrayList<>();
    private NewsApapter mAdapter;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.news_fg, null);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        RecyclerView news_list = (RecyclerView) view.findViewById(R.id.news_list);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorScheme(R.color.google_blue, R.color.google_green, R.color.google_red, R.color.google_yellow);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        news_list.setLayoutManager(mLayoutManager);
        news_list.setHasFixedSize(true);
        mAdapter = new NewsApapter(context, newsData);
        news_list.setAdapter(mAdapter);

        //设置RecyclerView行间距
        news_list.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.base8dp)));
        return view;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
        mAdapter.setOnItemClickListener(new NewsApapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                screenTransitAnim(position, view);
            }

        });
    }

    /**
     * Activity跳转动画
     * @param position 点击条目的position
     * @param view     点击条目的View
     *
     * 跳转动画没有做兼容 想要了解更多请看
     *                 http://www.bubuko.com/infodetail-460163.html
     *                 http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2014/1028/1856.html
     */
    private void screenTransitAnim(int position, View view) {

        ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(view.findViewById(R.id.news_img),
                0,0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("newsId", newsData.get(position).id);
        ActivityCompat.startActivity((MainActivity)context, intent,
                compat.toBundle());
    }

    @Override
    public void setPresenter(@NonNull NewsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean flag) {
        refreshLayout.setRefreshing(flag);
    }

    @Override
    public void showNews(List<News.Stories> news) {
        newsData.clear();
        newsData.addAll(0,news);
        setLoadingIndicator(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoNews() {
        setLoadingIndicator(false);
    }

    @Override
    public void onRefresh() {
        mPresenter.start(this);
    }


    /**
     * 设置RecyclerView行间距
     */
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if (parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        RequestManager.cancelAll(this);
    }
}
