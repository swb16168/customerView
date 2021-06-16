package com.swb.customerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.swb.customerview.R;
import com.swb.customerview.util.SizeUtils;

/**
 * @auther 72774
 * @date 2021/6/11  10:48
 * @description android 数据小键盘  采用Viewgroup实现
 */
public class KeyPadExView extends ViewGroup implements View.OnClickListener {
    private static final String TAG = "KeyPadExView";

    private static final int KEYSIZE = 11; //0-9 +退格键
    private static final int KEY_BACK = 10; //10 标识back键 退格
    private static final int ROW_SIZE = 4;
    private static final int COLUMN_SIZE = 3;
    private static final int ITEM_MARGIN_DEFAULT = 1;
    private static int NUMBER_SIZE_DEFAULT = 50;
    private int mNumberColor;
    private int mNumberSize;
    private int mNumberBgNormalColor;
    private int mNumberBgPressColor;
    private int mItemMargin;
    private int mPaddingBottom;
    private int mPaddingTop;
    private int mPaddingLeft;
    private int mPaddingRight;
    private KeyPadClickListener mClickListener =null;

    public KeyPadExView(Context context) {
        this(context, null);
    }

    public KeyPadExView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyPadExView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        setUpItem();
    }

    private void setUpItem() {
        removeAllViews();
        for (int i = 0; i < KEYSIZE; i++) {
            TextView numberView = new TextView(getContext());
            if (i == KEY_BACK) {
                numberView.setTag(true);
                numberView.setText("退格");
            } else {
                numberView.setTag(false);
                numberView.setText(String.valueOf(i));
            }
            //设置大小
            numberView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNumberSize);
            //设置颜色
            numberView.setTextColor(mNumberColor);
            //居中显示
            numberView.setGravity(Gravity.CENTER);

            //设置背景
            numberView.setBackground(providerBackground());
            //设置事件
            numberView.setOnClickListener(this);


            addView(numberView);
        }
    }

    /**
     * 提供number背景图
     *
     * @return
     */
    private StateListDrawable providerBackground() {

        StateListDrawable numberBgRes = new StateListDrawable();

        //创建按下时的背景
        GradientDrawable numberPressBgRes = new GradientDrawable();
        numberPressBgRes.setColor(getResources().getColor(R.color.keypad_ex_press));
        numberPressBgRes.setCornerRadius(SizeUtils.dip2px(10));
        //创建普通状态下的背景
        GradientDrawable numberNormalBgRes = new GradientDrawable();
        numberNormalBgRes.setCornerRadius(50);
        numberNormalBgRes.setColor(getResources().getColor(R.color.keypad_ex_normal));
        numberBgRes.addState(new int[]{android.R.attr.state_pressed}, numberPressBgRes);
        //设置该背景使用的限制条件
        numberBgRes.addState(new int[]{}, numberNormalBgRes);
        return numberBgRes;
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyPadExView);


        //间距
        mItemMargin = a.getDimensionPixelSize(R.styleable.KeyPadExView_itemMargin, ITEM_MARGIN_DEFAULT);
        Log.d(TAG, "initAttr: mItemMargin--->" + mItemMargin);
        //小键盘数字的颜色，默认橙色
        mNumberColor = a.getColor(R.styleable.KeyPadExView_numberColor, context.getResources().getColor(R.color.mainColor));
        //数字大小、默认16sp   sp会根据不同设备进行适配，
        mNumberSize = a.getDimensionPixelSize(R.styleable.KeyPadExView_numberSize, NUMBER_SIZE_DEFAULT);
        //数字键默认背景颜色
        mNumberBgNormalColor = a.getColor(R.styleable.KeyPadExView_itemNormalColor, context.getResources().getColor(R.color.keypad_ex_normal));
        //数字键按下时背景颜色
        mNumberBgPressColor = a.getColor(R.styleable.KeyPadExView_itemPressColor, context.getResources().getColor(R.color.keypad_ex_press));
        a.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int letf, top, right, bottom, rowIndex, columnIndex;

        /*for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            boolean isBack = (boolean) child.getTag();
            rowIndex = i / COLUMN_SIZE;            //行坐标
            columnIndex = i % COLUMN_SIZE;      //列坐标
            letf = columnIndex * child.getMeasuredWidth() + (mItemMargin * (columnIndex + 1)) + mPaddingLeft;
            top = rowIndex * child.getMeasuredHeight() + (mItemMargin * (rowIndex + 1)) + mPaddingTop;
            right = letf + child.getMeasuredWidth() - (mItemMargin * (columnIndex + 1)) - mPaddingRight;
            bottom = top + child.getMeasuredHeight() - (mItemMargin * (columnIndex + 1));
            if (isBack) {
                letf = columnIndex * (child.getMeasuredWidth() / 2) + (mItemMargin * (columnIndex + 1)) + mPaddingLeft;
                right = letf + child.getMeasuredWidth() - (mItemMargin * (columnIndex + 1)) - mPaddingRight;

            }
            child.layout(letf, top, right, bottom);
            Log.d(TAG, "onLayout: rowIndex-->" + rowIndex);
            Log.d(TAG, "onLayout: columnIndex-->" + columnIndex);
        }*/

        letf = mItemMargin + mPaddingLeft;
        for (int i = 0; i < getChildCount(); i++) {
            rowIndex = i / COLUMN_SIZE;
            columnIndex = i % COLUMN_SIZE;
            if(columnIndex == 0){
                letf = mItemMargin + mPaddingLeft;
            }
            View item = getChildAt(i);
            top = rowIndex * item.getMeasuredHeight() + mItemMargin *(rowIndex + 1) + mPaddingTop ;
            right = letf + item.getMeasuredWidth()   ;
            bottom = top + item.getMeasuredHeight();
            item.layout(letf,top,right,bottom);
            letf += item.getMeasuredWidth() +mItemMargin;

            Log.d(TAG, "onLayout: i--->" + i);
            Log.d(TAG, "onLayout: bottom--->" + bottom);
        }


    }

    //测量整个的大小  和子View的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPaddingBottom = getPaddingBottom();
        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();
        mPaddingTop = getPaddingTop();
        //拿到容器父控件的宽度和高度
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec  ) ;
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec ) ;
        //摆放方式为 4*3  的方正排列  退格键盘占2格
        int columnWidth = (parentWidth - (COLUMN_SIZE + 1) * mItemMargin -  (mPaddingTop + mPaddingBottom))  / COLUMN_SIZE;
        int rowHeight = (parentHeight - (ROW_SIZE + 1) * mItemMargin  - ( mPaddingLeft + mPaddingRight)) / ROW_SIZE;

        //采用精确模式获取子view的宽度，
        int itemWidth = MeasureSpec.makeMeasureSpec(columnWidth, MeasureSpec.EXACTLY);
        int itemHeigt = MeasureSpec.makeMeasureSpec(rowHeight, MeasureSpec.EXACTLY);
        int backWidth = MeasureSpec.makeMeasureSpec(columnWidth * 2, MeasureSpec.EXACTLY);
        //测试每个子view的长宽
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure((boolean) child.getTag() ? backWidth : itemWidth, itemHeigt);
        }
        //设置自己的长宽
        setMeasuredDimension(parentWidth, parentHeight);

    }


    public void setKeyPadClickListener(KeyPadClickListener listener){
        this.mClickListener = listener;
    }

    public interface KeyPadClickListener{
        void onItemClick(int keyValue);

        void onBackClick();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            boolean isBack = (boolean) v.getTag();
            if (mClickListener != null) {
                if (isBack) {
                    //Log.d(TAG, "onClick: ---点击了back键 ");
                    mClickListener.onBackClick();
                } else {
                    //Log.d(TAG, "onClick: ---点击了数字键---> " + ((TextView) v).getText());
                    mClickListener.onItemClick(Integer.parseInt(((TextView) v).getText().toString()));

                }
            }

        }

    }
}
