package com.xyz.mvpdemo.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xyz.mvpdemo.MainActivity;
import com.xyz.mvpdemo.R;
import com.xyz.mvpdemo.base.BaseFragment;
import com.xyz.mvpdemo.contracts.ImageContract;
import com.xyz.mvpdemo.model.bean.ImageBean;
import com.xyz.mvpdemo.model.http.RequestManager;
import com.xyz.mvpdemo.view.activity.ImageDetailActivity;
import com.xyz.mvpdemo.view.adapters.ImagesApapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xy_z on 2016/5/28 14:23
 * 邮箱：xyz@163.com
 * 描述：图片模块相对去其他两个复杂了那么一点，因为多了上拉加载更多
 */
public class ImagesFragmnet extends BaseFragment implements ImageContract.View, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private ImageContract.Presenter mPresenter;
    private ImagesApapter adapter;
    private RecyclerView img_list;
    private StaggeredGridLayoutManager layoutManager;
    private TextView hinTv;
    private List<ImageBean.PinsEntity> pins = new ArrayList<>();

    private boolean isRefres = false;   //刷新和加载区分标记
    private int maxID;                  //获取更多图片时需要的参数
    private int lastPosition = 0;       //list最后一条数据，用于局部刷新
    private boolean isLoading = false;  //是否正在加载更多，防止多次请求


    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.img_fg, null);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        img_list = (RecyclerView) view.findViewById(R.id.img_list);
        hinTv = (TextView) view.findViewById(R.id.hintTv);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        img_list.setLayoutManager(layoutManager);
        img_list.setItemAnimator(new DefaultItemAnimator());
        adapter = new ImagesApapter(context, pins);
        img_list.setAdapter(adapter);
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

        /**
         * 上拉加载更多
         * http://www.jianshu.com/p/4feb0c16d1b5
         * http://www.tuicool.com/articles/Rz2MBnj
         * 我是将以上两个不同的写法进行了结合然后加入了防止正在加载中又多次请求
         */
        img_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int[] visibleItems = layoutManager.findLastVisibleItemPositions(null);
                    int lastitem = Math.max(visibleItems[0], visibleItems[1]);
                    int itemCount = layoutManager.getItemCount();
                    if (lastitem >= itemCount - 1 && itemCount < lastitem + 5) {
                        isRefres = false;
                        if (isLoading) {
                            return;
                        }
                        mPresenter.loadMore(maxID, context);
                    }
                }
            }
        });

        adapter.setOnItemClickListener(new ImagesApapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String imgUrl, String pin_id) {
                screenTransitAnim(imgUrl, view, pin_id);
            }
        });
    }

    @Override
    public void setPresenter(ImageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMovies(ImageBean data) {
        if (isRefres) {
            pins.clear();
            pins.addAll(data.pins);
        } else {
            data.pins.remove(0);
            pins.addAll(data.pins);
        }
        maxID = pins.get(pins.size() - 1).pin_id;
        adapter.notifyItemRangeChanged(lastPosition, pins.size());
        lastPosition = pins.size();
    }

    @Override
    public void setLoadingIndicator(boolean flag) {
        refreshLayout.setRefreshing(flag);
    }

    @Override
    public void showNoMovies() {

    }

    @Override
    public void isMoreLoading(boolean flag) {
        isLoading = flag;
        if (flag) {
            hint("正在加载请稍后~");
        } else {
            hint("加载结束~");
        }
    }

    @Override
    public void onRefresh() {
        isRefres = true;
        if (mPresenter != null) {
            mPresenter.start(this);
        }
    }

    public void hint(String text) {
        if (isLoading) {
            hinTv.setVisibility(View.VISIBLE);
        } else {
            hinTv.setVisibility(View.INVISIBLE);
        }
        hinTv.setText(text);
    }

    private void screenTransitAnim(String imgUrl, View view, String pin_id) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation((MainActivity) context, view.findViewById(R.id.card_image), "img");
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra("imgUrl", imgUrl);
        intent.putExtra("pin_id", pin_id);
        ActivityCompat.startActivity((MainActivity) context, intent, optionsCompat.toBundle());
    }

    @Override
    public void onStop() {
        super.onStop();
        RequestManager.cancelAll(this);
    }
}
