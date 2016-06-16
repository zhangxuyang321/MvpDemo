package com.xyz.mvpdemo.view.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.xyz.mvpdemo.R;
import com.xyz.mvpdemo.model.bean.MovieBean;
import com.xyz.mvpdemo.view.custom.DepthPageTransformer;
import com.xyz.mvpdemo.view.custom.MyViewPager;

import java.util.List;

/**
 * 作者：xy_z on 2016/6/3 14:58
 * 邮箱：xyz@163.com
 * 描述: 利用RRecyclerView显示不同Item的方式，将轮播图与普通Item分别显示出来
 *       Glide的详细使用请参看http://mrfu.me/2016/02/27/Glide_Getting_Started/
 * 注意：轮播图的显示需要注意布局，如果你在使用的时候不成功，请留意一下布局android:clipChildren="false"不要限制子View的布局
 */
public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<MovieBean.ResultData> movies;
    private MovieViewPagerAdapter adapter;
    private List<MovieBean.MovieInfo> soonMovieInfos;
    private ViewHoder2 hoder2;

    private static final int ITEM_TYPE1 = 0;
    private static final int ITEM_TYPE2 = 1;
    private int lastPosition = -1;
    private int[] startingLocation = new int[2];

    private OnMovieItemClickListener mOnItemClickListener = null;
    public void setmOnItemClickListener(OnMovieItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public MovieAdapter(Context context, List<MovieBean.ResultData> list) {
        this.mContext = context;
        this.movies = list;
        soonMovieInfos = movies.get(1).data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item_type1, parent, false);
            return new ViewHolder1(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item_type2, parent, false);
            //获取到点击时的坐标这里是相对于Item本身的，所以在实际跳转的时候对Y坐标进行了更改，具体请看MovieFragment
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                        startingLocation[0]=   (int) motionEvent.getX();
                        startingLocation[1]=    (int) motionEvent.getY();
                    }
                    return false;
                }
            });
            view.setOnClickListener(this);
            return new ViewHoder2(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder1) {
            if (adapter == null) {
                adapter = new MovieViewPagerAdapter(mContext, movies.get(0).data);
                ((ViewHolder1) holder).myViewPager.setAdapter(adapter);
                ((ViewHolder1) holder).myViewPager.setPageMargin(30);
                ((ViewHolder1) holder).myViewPager.setOffscreenPageLimit(2);
                ((ViewHolder1) holder).myViewPager.setPageTransformer(true, new DepthPageTransformer());
            } else {
                adapter.notifyDataSetChanged();
            }
            adapter.setOnPageClickListener(new MovieViewPagerAdapter.PageClickListener() {
                @Override
                public void onItemClick(View view, String url, int[] startingLocation) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, url,startingLocation);
                    }
                }
            });
            setAnimation(((ViewHolder1) holder).vp_rl, position);
        } else if (holder instanceof ViewHoder2) {
            hoder2 = (ViewHoder2) holder;
            MovieBean.MovieInfo soonMovieInfo = soonMovieInfos.get(position - 1);
            hoder2.movie_title.setText(soonMovieInfo.tvTitle);
            hoder2.movie_storyBrief.setText(soonMovieInfo.story.data.storyBrief);
            hoder2.itemView.setTag(soonMovieInfo.m_iconlinkUrl);

            //这里使用了Palette来为Item设置背景，文字设置颜色
            Glide.with(mContext).load(soonMovieInfo.iconaddress).asBitmap().listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    Palette.Builder builder = Palette.from(resource);
                    builder.generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                            if (null != vibrantSwatch) {
                                hoder2.cardView.setBackgroundColor(vibrantSwatch.getRgb());
                                hoder2.movie_title.setTextColor(vibrantSwatch.getBodyTextColor());
                                hoder2.movie_storyBrief.setTextColor(vibrantSwatch.getBodyTextColor());
                            }
                        }
                    });
                    return false;
                }
            }).into(((ViewHoder2) holder).movie_img);


            setAnimation(hoder2.cardView, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE1 : ITEM_TYPE2;
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.get(1).data.size() + 1;
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        private RelativeLayout vp_rl;
        private MyViewPager myViewPager;

        public ViewHolder1(View itemView) {
            super(itemView);
            vp_rl = (RelativeLayout) itemView.findViewById(R.id.vp_rl);
            myViewPager = (MyViewPager) itemView.findViewById(R.id.viewpager);
        }
    }

    public static class ViewHoder2 extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView movie_img;
        private TextView movie_title;
        private TextView movie_storyBrief;

        public ViewHoder2(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            movie_img = (ImageView) itemView.findViewById(R.id.movie_img);
            movie_title = (TextView) itemView.findViewById(R.id.movie_title);
            movie_storyBrief = (TextView) itemView.findViewById(R.id.movie_storyBrief);
        }
    }


    public interface OnMovieItemClickListener {
        void onItemClick(View view, String url,int[] startingLocation);
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view, (String) view.getTag(),startingLocation);
        }
    }

    private  void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                    .anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
