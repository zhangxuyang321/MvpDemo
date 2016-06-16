package com.xyz.mvpdemo.view.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xyz.mvpdemo.R;
import com.xyz.mvpdemo.Util.Util;
import com.xyz.mvpdemo.model.bean.MovieBean;

import java.util.List;

/**
 * 作者：xy_z on 2016/6/3 15:42
 * 邮箱：xyz@163.com
 * 描述：电影模块对应的轮播图Adapter
 */
public class MovieViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<MovieBean.MovieInfo> movieInfos;
    private PageClickListener onPageClickListener = null;

    private int[] startingLocation = new int[2];

    public void setOnPageClickListener(PageClickListener onPageClickListener) {
        this.onPageClickListener = onPageClickListener;
    }


    public MovieViewPagerAdapter(Context context, List<MovieBean.MovieInfo> list) {
        mContext = context;
        movieInfos = list;
    }

    @Override
    public int getCount() {
        return movieInfos == null ? 0 : movieInfos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_pager_item, container, false);
        ImageView vp_img = (ImageView) view.findViewById(R.id.vp_img);
        TextView vp_grade = (TextView) view.findViewById(R.id.vp_grade);
        MovieBean.MovieInfo movieInfo = movieInfos.get(position);
        Glide.with(mContext).load(movieInfo.iconaddress).into(vp_img);
        vp_grade.setText(movieInfos.get(position).grade);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    startingLocation[0] = (int) motionEvent.getRawX();
                    startingLocation[1] = (int) motionEvent.getY();
                    MovieBean.MovieInfo movie = movieInfos.get(position);
                    if (onPageClickListener != null) {
                        onPageClickListener.onItemClick(view, movie.m_iconlinkUrl, startingLocation);
                    }
                }
                return true;
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface PageClickListener {
        void onItemClick(View view, String url, int[] startingLocation);
    }
}
