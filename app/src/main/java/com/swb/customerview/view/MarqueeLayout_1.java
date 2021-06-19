package com.swb.customerview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * @auther 72774
 * @date 2021/6/18  16:47
 * @description 跑马灯效果
 */
public class MarqueeLayout_1 extends ViewGroup {
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

    public MarqueeLayout_1(Context context) {
        this(context, null);
    }

    public MarqueeLayout_1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeLayout_1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

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


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();
        totalChildWidth = 0;
        mParentWidthMeasureSpec = MeasureSpec.getSize(widthMeasureSpec) - mPaddingLeft - mPaddingRight;

        int startIndex = mPaddingLeft;
        for (int i = 0; i < getChildCount(); i++) {
            View item = getChildAt(i);
            if (item.getVisibility() == GONE) {
                continue;
            }
            int itemMeasureWidth = MeasureSpec.makeMeasureSpec(mParentWidthMeasureSpec, MeasureSpec.AT_MOST);
            item.measure(itemMeasureWidth, heightMeasureSpec);
            int itemMeasuredHeight = item.getMeasuredHeight();
            if (itemMeasuredHeight > maxHeight) {
                maxHeight = itemMeasuredHeight;
            }
            totalChildWidth += item.getMeasuredWidth();

            Log.d(TAG, "onMeasure:itemMeasuredHeight - >> " + itemMeasuredHeight);
            Log.d(TAG, "onMeasure:mPaddingBottom - >> " + mPaddingBottom);
            item.setTag(new ItemPosition(startIndex, startIndex + item.getMeasuredWidth(), mPaddingTop, itemMeasuredHeight + mPaddingBottom));
            startIndex += item.getMeasuredWidth();
            ;
        }

        int selfHeightSpec = MeasureSpec.makeMeasureSpec(maxHeight + mPaddingTop + mPaddingBottom, MeasureSpec.AT_MOST);

        setMeasuredDimension(mParentWidthMeasureSpec, selfHeightSpec);

        //判断 所有孩子的宽度是否大于 父容器的宽度
        if(totalChildWidth > mParentWidthMeasureSpec){
            startMaquee();
        }else{
            stopMarquee();
        }
    }


    public boolean isMarqueeRuning() {
        return this.marqueeRuning;
    }

    private class ItemPosition {
        public ItemPosition(int left, int right, int top, int bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }

        public int left;
        public int right;
        public int top;
        public int bottom;

        @Override
        public String toString() {
            return "ItemPosition{" +
                    "left=" + left +
                    ", right=" + right +
                    ", top=" + top +
                    ", bottom=" + bottom +
                    '}';
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        doLayout();
    }


    private void startMaquee() {
        marqueeRuning = true;
        removeCallbacks(mTask);
        post(mTask);
    }

    private void stopMarquee() {
        marqueeRuning = false;
        removeCallbacks(mTask);
    }

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            doLayout();
            postDelayed(this, 50);
        }
    };

    private int stepSize = 3;

    private void doLayout() {
        for (int i = 0; i < getChildCount(); i++) {
            View item = getChildAt(i);
            if (item.getVisibility() == GONE) {
                continue;
            }
            ItemPosition position = (ItemPosition) item.getTag();
            Log.d(TAG, "onLayout: position-->" + position);
            item.layout(position.left, position.top, position.right, position.bottom);
            //如果最左边的坐标 大于 所有子View的宽度之和，那么将当前view 移动到 最右边出口 并且减去最后一个子View的宽度


            if (position.right < 0) {
                View lastChild = getChildAt(getChildCount()-1);
                position.left = totalChildWidth - lastChild.getMeasuredWidth() ;
            } else {
                position.left -= stepSize;
            }
            position.right = position.left + item.getMeasuredWidth();
        }


    }


}
