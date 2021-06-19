package com.swb.customerview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @auther 72774
 * @date 2021/6/18  16:47
 * @description 直播间 垂直滚动效果
 */
public class MarqueeLayoutVertical extends ViewGroup {
    private static final String TAG = "MarqueeLayoutVertical";

    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingRight;
    private int mPaddingBottom;
    private TextView mFirstTv;
    private TextView mSecondTv;
    private String[] mTextArray;
    private int mCurrentTextIndex = 0;
    private static final int ANIMATOR_TIME = 3000;

    public MarqueeLayoutVertical(Context context) {
        this(context, null);
    }

    public MarqueeLayoutVertical(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeLayoutVertical(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mFirstTv = new TextView(getContext());
        mSecondTv = new TextView(getContext());
        addView(mFirstTv);
        addView(mSecondTv);
        //todo : 设置属性


    }


    public void setTextArray(String[] textArray) {
        if (textArray.length == 0) {
            throw new IllegalArgumentException("text array length must greator the one");
        }

        //设置内容
        this.mTextArray = textArray;
        mFirstTv.setText(mTextArray[0]);
        if (textArray.length > 1) {
            Log.d(TAG, "setTextArray: mTextArray[1]-->" + mTextArray[1]);
            mSecondTv.setText(mTextArray[1]);
            this.mCurrentTextIndex = 1;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();
        int childHeight = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.AT_MOST);
        mFirstTv.measure(widthMeasureSpec, childHeight);
        mSecondTv.measure(widthMeasureSpec, childHeight);
        setMeasuredDimension(widthMeasureSpec, Math.max(mFirstTv.getMeasuredHeight(), mSecondTv.getMeasuredHeight()));

    }


    //方向定义
    enum Direction {
        BOTTOM_2_TOP, TOP_2_BOTTOM
    }

    private Direction mCurrDirection = Direction.TOP_2_BOTTOM;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放子View
        mFirstTv.layout(mPaddingLeft, mPaddingTop, mPaddingLeft + mFirstTv.getMeasuredWidth(), mPaddingBottom + mFirstTv.getMeasuredHeight());
        if (mCurrDirection == Direction.BOTTOM_2_TOP) {

            mSecondTv.layout(mPaddingLeft, mPaddingTop + mFirstTv.getMeasuredHeight(), mSecondTv.getMeasuredWidth() + mPaddingLeft, mPaddingBottom + mSecondTv.getMeasuredHeight() + mFirstTv.getMeasuredHeight());

        } else if (mCurrDirection == Direction.TOP_2_BOTTOM) {
            mSecondTv.layout(mPaddingLeft, -(mPaddingBottom + mFirstTv.getMeasuredHeight()), mSecondTv.getMeasuredWidth() + mPaddingLeft, mPaddingBottom);

        }
        if (this.mTextArray.length > 1) {
            doAnimator();

        }


    }

    private void doAnimator() {

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATOR_TIME);
        if (mCurrDirection == Direction.BOTTOM_2_TOP) {
            //设置动画
            ObjectAnimator firstAnimator = ObjectAnimator.ofFloat(mFirstTv, "translationY", 0, -mFirstTv.getMeasuredHeight());
            Log.d(TAG, "doAnimator: mSecondTv-->" + mSecondTv.getHeight());
            Log.d(TAG, "doAnimator: mSecondTv.getMeasuredHeight-->" + mSecondTv.getMeasuredHeight());
            ObjectAnimator secondAnimator = ObjectAnimator.ofFloat(mSecondTv, "translationY", 0, -mSecondTv.getMeasuredHeight());
            animatorSet.playTogether(firstAnimator, secondAnimator);
        } else {
            //设置动画
            ObjectAnimator firstAnimator = ObjectAnimator.ofFloat(mFirstTv, "translationY", 0, mFirstTv.getMeasuredHeight());
            Log.d(TAG, "doAnimator: mSecondTv-->" + mSecondTv.getHeight());
            Log.d(TAG, "doAnimator: mSecondTv.getMeasuredHeight-->" + mSecondTv.getMeasuredHeight());
            ObjectAnimator secondAnimator = ObjectAnimator.ofFloat(mSecondTv, "translationY", 0, mSecondTv.getMeasuredHeight());
            animatorSet.playTogether(firstAnimator, secondAnimator);


        }
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeCallbacks(mTask);
                postDelayed(mTask,ANIMATOR_TIME);
            }
        });
        animatorSet.start();

    }

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            //播放动画之前 切换数据
            String firstText = mTextArray[mCurrentTextIndex];
            mFirstTv.setText(firstText);
            mCurrentTextIndex++;
            if(mCurrentTextIndex >= mTextArray.length){
                mCurrentTextIndex = 0;
            }
            String secondText = mTextArray[mCurrentTextIndex];
            mSecondTv.setText(secondText);
            doAnimator();
        }
    };


    //#region 解决 layout_margin 问题
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        //  return new MyLayoutParams(getContext(), attrs);
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams lp) {
        return new MarginLayoutParams(lp);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

//#endregion 解决layout_margin
}
