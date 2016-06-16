package com.xyz.mvpdemo.view.adapters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xyz.mvpdemo.R;
import com.xyz.mvpdemo.Util.MyToast;
import com.xyz.mvpdemo.Util.ViewHelper;
import com.xyz.mvpdemo.model.bean.News;

import java.util.List;

/**
 * 作者：xy_z on 2016/5/30 14:52
 * 邮箱：xyz@163.com
 * 这里的动画效果参考了https://github.com/wasabeef/recyclerview-animators，从中将这个缩放动画摘了出来
 */
public class NewsApapter extends RecyclerView.Adapter<NewsApapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<News.Stories> news;
    private Interpolator mInterpolator = new LinearInterpolator();       //差值器
    private OnRecyclerViewItemClickListener mOnItemClickListener = null; //自定义点击事件

    private int mDuration = 500;    //动画执行时间
    private float mFrom = 0.5f;     //动画从多少开始
    private int mLastPosition = -1; //记录位置，防止往回滑动时，也会调用动画

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public NewsApapter(Context context, @NonNull List<News.Stories> data) {
        this.context = context;
        news = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_fg_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(news.get(position).images.get(0)).into(holder.news_img);
        holder.news_title.setText(news.get(position).title);
        holder.itemView.setTag(position);
        if (position > mLastPosition) {
            for (Animator anim : getAnimators(holder.itemView)) {
                anim.setDuration(mDuration).start();
                anim.setInterpolator(mInterpolator);
            }
            mLastPosition = position;
        } else {
            ViewHelper.clear(holder.itemView);
        }
    }

    //条目的缩放动画
    private Animator[] getAnimators(View itemView) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(itemView, "scaleX", mFrom, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(itemView, "scaleY", mFrom, 1f);
        return new ObjectAnimator[]{scaleX, scaleY};
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView news_img;
        private TextView news_title;

        public ViewHolder(View itemView) {
            super(itemView);
            news_img = (ImageView) itemView.findViewById(R.id.news_img);
            news_title = (TextView) itemView.findViewById(R.id.news_title);
        }
    }
}
