package com.xyz.mvpdemo.view.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import com.xyz.mvpdemo.Util.Util;

/**
 * 作者：xy_z on 2016/6/12 17:54
 * 邮箱：xyz@163.com
 * 描述：电影界面跳转详情时动画的基础，这是按照泡在网上的日子写的，如果想要了解更多请转至http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0505/2833.html
 * 感谢：Miroslaw Stanek的开源精神与泡在网上的日子翻译
 */
public class RevealBackgroundView extends View {

    public static final int STATE_NOT_STARTED = 0;          //初始状态，还没有开始动画
    public static final int STATE_FILL_STARTED = 1;         //填充状态，动画正在执行
    public static final int STATE_FINISHED = 2;             //结束状态，动画执行结束
    private int state = STATE_NOT_STARTED;                  //当前状态，默认为初始状态

    private static final Interpolator INTERPOLATOR = new AccelerateInterpolator(); //插入器，动画从开始到结束，变化率是一个加速的过程。
    private static final int FILL_TIME = 500;             //动画执行时间
    private Paint fillPaint;          //画笔
    private int currentRadius;        //当前圆半径，
    ObjectAnimator revealAnimator;    //属性动画
    private float startLocationX;     //圆心X轴坐标
    private float startLocationY;     //圆心Y轴坐标

    private OnStateChangeListener onStateChangeListener; //状态改变监听器

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public RevealBackgroundView(Context context) {
        super(context);
        init();
    }

    public RevealBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RevealBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RevealBackgroundView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 初始化画笔
     */
    private void init() {
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL); //设置填充样式
        fillPaint.setColor(Color.WHITE);
    }


    /**
     * 从点击处执行动画的方法
     *
     * @param tapLocationOnScreen 点击坐标的数组
     */
    public void startFromLocation(int[] tapLocationOnScreen) {
        changeState(STATE_FILL_STARTED);
        startLocationX = tapLocationOnScreen[0];
        startLocationY = tapLocationOnScreen[1];
        revealAnimator = ObjectAnimator.ofInt(this, "currentRadius", 0, getWidth() + getHeight()).setDuration(FILL_TIME);
        revealAnimator.setInterpolator(INTERPOLATOR);
        revealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                changeState(STATE_FINISHED);
            }
        });
        revealAnimator.start();
    }

    /**
     * 强制结束动画的方法，用于退出界面等特殊情况
     */
    public void setToFinish() {
        changeState(STATE_FINISHED);
        invalidate();
    }

    /**
     * 画圆
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (state == STATE_FINISHED) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), fillPaint);
        } else {
            canvas.drawCircle(startLocationX, startLocationY, currentRadius, fillPaint);
        }
    }

    /**
     * 因为ObjectAnimator属性动画是通过set,get方法改变view的值从而实现动画的，所以要写一个set方法
     * 详情请参考http://blog.csdn.net/jdsjlzx/article/details/45558843
     * @param radius 改变的半径
     */
    public void setCurrentRadius(int radius) {
        this.currentRadius = radius;
        invalidate();
    }

    /**
     * 改变状态的方法
     *
     * @param state 改变后的状态
     */
    public void changeState(int state) {
        if (this.state == state) {
            return;
        }

        this.state = state;
        if (onStateChangeListener != null) {
            onStateChangeListener.onStateChange(state);
        }
    }

    public interface OnStateChangeListener {
        void onStateChange(int state);
    }
}
