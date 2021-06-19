package com.swb.customerview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.swb.customerview.R;

/**
 * @auther 72774
 * @date 2021/6/18  16:47
 * @description 跑马灯效果
 */
public class MarqueeLayout extends ViewGroup {
    private static final String TAG = "MarqueeLayout";
    private int DEFAULT_STEP = -5;
    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingRight;
    private int mPaddingBottom;
    private int totalChildWidth = 0;
    private boolean marqueeRuning = false;
    private int maxHeight = 0;
    private int mParentWidthMeasureSpec;

    public MarqueeLayout(Context context) {
        this(context, null);
    }

    public MarqueeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        //  return new MyLayoutParams(getContext(), attrs);
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new MarginLayoutParams(lp);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();
        totalChildWidth = 0;
        mParentWidthMeasureSpec = MeasureSpec.getSize(widthMeasureSpec) - mPaddingLeft - mPaddingRight;
        for (int i = 0; i < getChildCount(); i++) {
            View item = getChildAt(i);
            if (item.getVisibility() == GONE) {
                continue;
            }
            int itemMeasureWidth = MeasureSpec.makeMeasureSpec(mParentWidthMeasureSpec, MeasureSpec.AT_MOST);
            item.measure(itemMeasureWidth, heightMeasureSpec);
            if (item.getMeasuredHeight() > maxHeight) {
                maxHeight = item.getMeasuredHeight();
            }
            totalChildWidth +=item.getMeasuredWidth();
        }


        int selfHeightSpec = MeasureSpec.makeMeasureSpec(maxHeight + mPaddingTop + mPaddingBottom, MeasureSpec.AT_MOST);

        setMeasuredDimension(mParentWidthMeasureSpec, selfHeightSpec);

    }


    private int runStep = 5;

    public void startMarquee() {

        marqueeRuning = true;
        removeCallbacks(runTask);
        post(runTask);
    }

    public void stopMarquee() {
        marqueeRuning = false;
        removeCallbacks(runTask);
        runStep = DEFAULT_STEP;
        requestLayout();
    }

    private final Runnable runTask = new Runnable() {
        @Override
        public void run() {
            runStep += DEFAULT_STEP;
            if(runStep  <= -totalChildWidth){
                runStep  = mParentWidthMeasureSpec;
            }
            postDelayed(this, 50);
            requestLayout();

        }
    };


    public boolean isMarqueeRuning() {
        return this.marqueeRuning;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout:mPaddingLeft--->" + mPaddingLeft);
        int left = mPaddingLeft + runStep;
        int right;

        for (int i = 0; i < getChildCount(); i++) {
            View item = getChildAt(i);
            int measuredWidth = item.getMeasuredWidth();
            if (item.getVisibility() == GONE) {
                continue;
            }
            right = left + measuredWidth;
            item.layout(left, mPaddingTop, right, measuredWidth);
            left += item.getMeasuredWidth();
        }
    }
}
