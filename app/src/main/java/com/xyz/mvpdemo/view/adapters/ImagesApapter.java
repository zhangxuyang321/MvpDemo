package com.xyz.mvpdemo.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xyz.mvpdemo.R;
import com.xyz.mvpdemo.Util.Port;
import com.xyz.mvpdemo.model.bean.ImageBean;

import java.util.List;

/**
 * 作者：xy_z on 2016/5/30 14:52
 * 邮箱：xyz@163.com
 * 注意：RecyclerView实现瀑布流在滑动时会出现item位置不断变换的问题，这是因为对应的item不是固定的在滑动中需要
 *       不断从新布局，要想避免这个问题要么统一Item中图片的高度（显然不现实），要么就要在图片前先设置高度，所以后台返回的
 *       数据中需要有图片的宽和高
 */
public class ImagesApapter extends RecyclerView.Adapter<ImagesApapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<ImageBean.PinsEntity> pins;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public ImagesApapter(Context context, List<ImageBean.PinsEntity> list) {
        mContext = context;
        pins = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_fg_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        int height = pins.get(position).file.height;
        if(height>750){
            height = 750;
        }
        holder.card_image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height));
        Glide.with(mContext)
                .load(Port.imgurl + pins.get(position).file.key + "_fw320sf")
                .placeholder(R.mipmap.zw)
                .crossFade()
                .into(holder.card_image);
        holder.card_title.setText(pins.get(position).raw_text);
        holder.card_heart_count.setText(String.valueOf(pins.get(position).like_count));
    }

    @Override
    public int getItemCount() {
        return pins.size();
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        ImageBean.PinsEntity.FileEntity file = pins.get(position).file;
        String url = Port.imgurl + file.key;
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, url,String.valueOf(pins.get(position).pin_id));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView card_image;
        private TextView card_title;
        private TextView card_heart_count;

        public ViewHolder(View itemView) {
            super(itemView);
            card_image = (ImageView) itemView.findViewById(R.id.card_image);
            card_title = (TextView) itemView.findViewById(R.id.card_title);
            card_heart_count = (TextView) itemView.findViewById(R.id.card_heart_count);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String imgUrl,String pin_id);
    }
}
