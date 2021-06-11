package com.swb.customerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Px;

import com.swb.customerview.R;
import com.swb.customerview.util.SizeUtils;

/**
 * @auther 72774
 * @date 2021/6/11  10:48
 * @description android 数据小键盘  采用Viewgroup实现
 */
public class KeyPadExView extends ViewGroup {


    private static final int KEYSIZE = 12; //0-9 +退格键
    private static int NUMBER_SIZE_DEFAULT = 50;
    private int mNumberColor;
    private int mNumberSize;
    private int mNumberBgNormalColor;
    private int mNumberBgPressColor;

    public KeyPadExView(Context context) {
        this(context, null);
    }

    public KeyPadExView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyPadExView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);


        for (int i = 0; i < KEYSIZE; i++) {
            TextView numberView = new TextView(getContext());
            numberView.setText(String.valueOf(i));
            //设置大小
            numberView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mNumberSize);
            //设置颜色
            numberView.setTextColor(mNumberColor);
            //居中显示
            numberView.setGravity(Gravity.CENTER);
            //设置背景

            StateListDrawable numberBgRes = new StateListDrawable();
            //创建按下时的背景
            GradientDrawable numberPressBgRes  = new GradientDrawable();
            numberPressBgRes.setColor(getResources().getColor(R.color.keypad_ex_press));
            numberPressBgRes.setCornerRadius(SizeUtils.dip2px(5));
            //设置该背景使用的限制条件
            numberBgRes.addState(new int[]{android.R.attr.state_pressed}, numberPressBgRes);
            //创建普通状态下的背景
            GradientDrawable numberNormalBgRes  = new GradientDrawable();
            numberNormalBgRes.setCornerRadius(5);
            numberPressBgRes.setColor(getResources().getColor(R.color.keypad_ex_normal));
            numberBgRes.addState(new int[]{}, numberPressBgRes);

            numberView.setBackground(numberBgRes);
            addView(numberView);
        }


    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyPadExView);

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

    }
}
