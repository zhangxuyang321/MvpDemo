package com.xyz.mvpdemo.view.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.xyz.mvpdemo.MainActivity;
import com.xyz.mvpdemo.R;
import com.xyz.mvpdemo.base.BaseFragment;
import com.xyz.mvpdemo.contracts.MovieContract;
import com.xyz.mvpdemo.model.bean.MovieBean;
import com.xyz.mvpdemo.model.http.RequestManager;
import com.xyz.mvpdemo.view.activity.MovieDetailsActivity;
import com.xyz.mvpdemo.view.adapters.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xy_z on 2016/5/28 14:23
 * 邮箱：xyz@163.com
 * 描述：电影模块，同新闻模块一样
 */
public class MoviesFragmnet extends BaseFragment implements MovieContract.View, SwipeRefreshLayout.OnRefreshListener {

    private MovieContract.Presenter mPresenter;
    private SwipeRefreshLayout refreshLayout;
    private MovieAdapter mAdapter;
    private List<MovieBean.ResultData> movieList = new ArrayList<>();
    private RecyclerView movie_list;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.movies_fg, null);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        movie_list = (RecyclerView) view.findViewById(R.id.movie_list);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorScheme(R.color.google_blue, R.color.google_green, R.color.google_red, R.color.google_yellow);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        movie_list.setLayoutManager(mLayoutManager);
        movie_list.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.base8dp)));
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
    }

    @Override
    public void setPresenter(MovieContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMovies(List<MovieBean.ResultData> resultData) {
        movieList.clear();
        movieList.addAll(resultData);
        if (mAdapter == null) {
            mAdapter = new MovieAdapter(context, movieList);
            movie_list.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        mAdapter.setmOnItemClickListener(new MovieAdapter.OnMovieItemClickListener() {
            @Override
            public void onItemClick(View view, String url, int[] startingLocation) {
                screenTransitAnim(view, url, startingLocation);
            }
        });
    }

    /**
     * 跳转Activity动画
     * @param view 点击的View
     * @param url  电影详情web连接地址
     * @param startingLocation 点击的坐标
     *  这个动画参考了http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0505/2833.html
     *   根据实际做了优化，改变了原先执行动画时不是根据点击位置开始的
     */
    private void screenTransitAnim(View view, String url, int[] startingLocation) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        startingLocation[1] += location[1];
        ((MainActivity) context).overridePendingTransition(0, 0);
        MovieDetailsActivity.startUserProfileFromLocation(startingLocation, (MainActivity) context, url);
    }



    @Override
    public void setLoadingIndicator(boolean flag) {
        refreshLayout.setRefreshing(flag);
    }

    @Override
    public void showNoMovies() {

    }

    @Override
    public void onRefresh() {
        mPresenter.start(this);
    }

    //设置行间距
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
