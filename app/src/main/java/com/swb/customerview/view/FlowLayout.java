package com.swb.customerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swb.customerview.R;
import com.swb.customerview.util.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther 72774
 * @date 2021/6/8  14:06
 * @description
 */
public class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";
    private static final int DEFAULT_MAX_LINE = 10;
    private static final float DEFAULT_HORIZONTAL = SizeUtils.dip2px(5f);
    private static final float DEFAULT_VERTICAL = SizeUtils.dip2px(5f);
    private static final int DEFAULT_MAX_TEXT_LENGTH = 20;
    private static final float DEFAULT_BORDER_RADIUS = SizeUtils.dip2px(10f);
    private static final float DEFAULT_BORDER_SIZE = SizeUtils.dip2px(10f);
    private final int mBorderColor;
    private int mMaxLine;
    private float mItemHorizontalMargin;
    private float mItemVerticalMargin;
    private int mMaxTextLength;
    private int mTextColor;
    private float mBoderRadius;
    private float mBoderSize;

    private List<String> textValues = new ArrayList<>();
    private ItemClickListener mItemClickListener;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取属性

        TypedArray a = context.obtainStyledAttributes(R.styleable.FlowLayout);
        //最大行数
        mMaxLine = a.getInt(R.styleable.FlowLayout_maxLine, DEFAULT_MAX_LINE);
        //水平间距
        mItemHorizontalMargin = a.getDimension(R.styleable.FlowLayout_itemHorizontalMargin, DEFAULT_HORIZONTAL);
        //垂直间距
        mItemVerticalMargin = a.getDimension(R.styleable.FlowLayout_itemVerticalMargin, DEFAULT_VERTICAL);
        //文本长度
        mMaxTextLength = a.getInt(R.styleable.FlowLayout_textMaxLength, DEFAULT_MAX_TEXT_LENGTH);
        //文本颜色
        mTextColor = a.getColor(R.styleable.FlowLayout_textColor, getResources().getColor(R.color.flowTextGrey));
        //边框颜色
        mBorderColor = a.getColor(R.styleable.FlowLayout_boderColor, getResources().getColor(R.color.flowTextGrey));
        //圆角度
        mBoderRadius = a.getDimension(R.styleable.FlowLayout_boderRadius, DEFAULT_BORDER_RADIUS);
        //边框大小
        mBoderSize = a.getDimension(R.styleable.FlowLayout_boderSize, DEFAULT_BORDER_SIZE);
        a.recycle();
    }

    /**
     * @param changed
     * @param l       左位置，相对于父位置
     * @param t       顶部位置，相对于父位置
     * @param r       右位置，相对于父位置
     * @param b       底部位置，相对于父位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child = getChildAt(0);
        int layoutLeft = 0;  //子view距离左边的值    同一行内   每摆放一个View则需 加上该View的测量宽度
        int layoutTop = 0;   //子view距离顶部的值   同一行内此值相同  换行时 需要加上子View的高度
        int layoutRight = 0; //子view距离右边的值   同一行内  每摆放一个子View  等与这个子View 加同一行内所有子view的宽度
        int layoutBottom = child.getMeasuredHeight(); //子View 距离底部的值   同一行内此值相同  换行时 需要加上子View的高度


        layoutLeft = (int) mItemHorizontalMargin + getPaddingLeft();
        layoutTop = (int) mItemVerticalMargin + getPaddingTop();
        layoutBottom = child.getMeasuredHeight() + (int) mItemVerticalMargin + getPaddingBottom();


        for (List<View> rowChildrenViews : mChildrenViews) {
            layoutRight += getPaddingLeft();
            for (View rowChildrenView : rowChildrenViews) {
                layoutRight += rowChildrenView.getMeasuredWidth() + (int) mItemVerticalMargin;

                Log.d(TAG, "onLayout: 判断右边边界" + (layoutRight > getMeasuredWidth() - (int)mItemHorizontalMargin));
                //判断右边 边界
                if (layoutRight > getMeasuredWidth() - (int)mItemHorizontalMargin) {

                    layoutRight = (getMeasuredWidth() - (int)mItemHorizontalMargin - getPaddingRight());
                }
                rowChildrenView.layout(layoutLeft, layoutTop, layoutRight, layoutBottom);
                layoutLeft = layoutRight + (int) mItemHorizontalMargin;
            }
            //换行
            layoutLeft = (int) mItemHorizontalMargin + getPaddingLeft();
            layoutRight = 0;
            layoutTop = layoutBottom + (int) mItemVerticalMargin;
            layoutBottom += child.getMeasuredHeight() + (int) mItemVerticalMargin;
        }

    }

    public void setData(List<String> textValue) {
        this.textValues.clear();
        this.textValues.addAll(textValue);
        //根据数据创建子页面，并且添加到当前容器内
        setUpChildren();
    }


    private void setUpChildren() {
        removeAllViews();
        for (String textValue : textValues) {
            //TextView textView = new TextView(this.getContext());
            TextView textView = getItemViewByXML();
            textView.setText(textValue);
            textView.setOnClickListener((v -> {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(textValue);
                }
            }));
            //设置子View相关属性
            addView(textView);
        }

    }

    public void setItemClickListener(ItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface ItemClickListener {
        /**
         * 点击的值
         *
         * @param textValue
         */
        void onItemClick(String textValue);
    }


    private TextView getItemViewByXML() {
        return (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_flow_text, this, false);
    }

    /*保存该布局所有的子view*/
    private List<List<View>> mChildrenViews = new ArrayList<>();

    /**
     * 进行测量 设置控件的宽度跟高度
     * 这两个值来自于父控件，包含值和模式
     * <p>
     * int类型  4字节  4*8 32bit位   前2位值代表模式 后30位代表具体值
     * EXACTLY ——精确值模式 标识 在XML文件中指定 layout_width=xxdp 或者 match_parent 时
     * AT_MOST ---> 最大值模式  标识 layout_width="wrap_content" 时，控件大小跟随控件内容的变化而变化，不超过父控件即可
     * <p>
     * UNSPECIFIED--->它不指定其大小，view 想多大就多大
     * View 类默认的 onMeasure()方住只支持 EXACTLY 模式
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        Log.d(TAG, "onMeasure: mode--->" + mode);
        Log.d(TAG, "onMeasure: size--->" + parentWidth);
        Log.d(TAG, "onMeasure:  MeasureSpec.AT_MOST--->" + MeasureSpec.AT_MOST);
        Log.d(TAG, "onMeasure: MeasureSpec.EXACTLY--->" + MeasureSpec.EXACTLY);
        Log.d(TAG, "onMeasure: MeasureSpec.UNSPECIFIED--->" + MeasureSpec.UNSPECIFIED);

        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        mChildrenViews.clear();
        // mChildrenViews.clear();
        /* 一行当中可以容纳的子View */
        List<View> rowViewList = new ArrayList<>();
        mChildrenViews.add(rowViewList);
        Log.d(TAG, "onMeasure: childCount -->" + childCount);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            //测量子View
            measureChild(child, parentWidth, parentHeight);
            //当此行内容为空时直接将子View添加进来
            if (rowViewList.size() == 0) {
                rowViewList.add(child);
            } else {
                //拿到子View的尺寸
                //根据尺寸 计算行大小
                //判断是否能将子View添加进来
                boolean isChildrenAttach = checkChildrenAttach(rowViewList, child, parentWidth);
                if (!isChildrenAttach) {
                    // 不能添加则 重新创建一行,添加进当前的子view集合
                    Log.d(TAG, "onMeasure:  rowViewList --->" + rowViewList);
                    rowViewList = new ArrayList<>();
                    mChildrenViews.add(rowViewList);
                }

                rowViewList.add(child);
            }
        }
        //所有子View 测量行数结束   根据尺寸计算行高
        View child = getChildAt(0);
        int childMeasuredHeight = child.getMeasuredHeight();
        Log.d(TAG, "onMeasure: childMeasuredHeight--->" + childMeasuredHeight);

        //计算间距
        int parentHeightTotal = (childMeasuredHeight + (int) mItemVerticalMargin) * mChildrenViews.size()
                + (int) mItemVerticalMargin
                + getPaddingBottom() + getPaddingTop();
        Log.d(TAG, "onMeasure: parentHeightTotal--->" + parentHeightTotal);
        //设置当前父控件的高度
        setMeasuredDimension(parentWidth, parentHeightTotal);
    }


    /**
     * 计算 子View的宽度  是否能附加到父控件的当前行内
     *
     * @param rowViewList 行内子view集合
     * @param child       等待附加的子View
     * @param parenWidth  父控件的宽度
     * @return
     */
    private boolean checkChildrenAttach(List<View> rowViewList, View child, int parenWidth) {
        //获取当前子view的期望宽度
        int childMeasuredWidth = child.getMeasuredWidth();
        int totalWidth = (int) mItemHorizontalMargin;
        //计算间距 设定的子View margin 间距 以及  父控件padding间距
        for (View view : rowViewList) {
            totalWidth += view.getMeasuredWidth() + (int) mItemHorizontalMargin;
        }
        totalWidth += childMeasuredWidth + (int) mItemHorizontalMargin + getPaddingLeft() + getPaddingRight();
        //如果totalWidth小于或等于 父控件的限制宽度  返回true 标识可以添加进来
        return totalWidth <= parenWidth;
    }


    //#region 属性get set

    public int getBorderColor() {
        return mBorderColor;
    }

    public int getMaxLine() {
        return mMaxLine;
    }

    public void setMaxLine(int maxLine) {
        mMaxLine = maxLine;
    }

    public float getItemHorizontalMargin() {
        return mItemHorizontalMargin;
    }

    public void setItemHorizontalMargin(float itemHorizontalMargin) {
        mItemHorizontalMargin = itemHorizontalMargin;
    }

    public float getItemVerticalMargin() {
        return mItemVerticalMargin;
    }

    public void setItemVerticalMargin(float itemVerticalMargin) {
        mItemVerticalMargin = itemVerticalMargin;
    }

    public int getMaxTextLength() {
        return mMaxTextLength;
    }

    public void setMaxTextLength(int maxTextLength) {
        mMaxTextLength = maxTextLength;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public float getBoderRadius() {
        return mBoderRadius;
    }

    public void setBoderRadius(float boderRadius) {
        mBoderRadius = boderRadius;
    }

    public float getBoderSize() {
        return mBoderSize;
    }

    public void setBoderSize(float boderSize) {
        mBoderSize = boderSize;
    }


    //endregion
}
