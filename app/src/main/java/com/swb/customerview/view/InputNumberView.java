package com.swb.customerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.swb.customerview.R;

/**
 * @auther 72774
 * @date 2021/6/2  10:55
 * @description
 */
public class InputNumberView extends LinearLayout {

    private static final String TAG = "InputNumberView";
    private TextView mMinuBtn;
    private TextView mAdditionBtn;
    private EditText mNumberInputView;
    private int mDefaultValue;
    private int mStep;
    private int mMax;
    private int mMin;
    private int mBtnBgRes;

    public InputNumberView(Context context) {
        this(context, null);
    }

    public InputNumberView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttr(context,attrs);
        initView(context);
        initEvent();

    }

    private void initAttr(Context context,AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.InputNumberView);
        mMax = a.getInt(R.styleable.InputNumberView_max, 10);
        mMin = a.getInt(R.styleable.InputNumberView_max, -10);
        mStep = a.getInt(R.styleable.InputNumberView_max, 1);
        mDefaultValue = a.getInt(R.styleable.InputNumberView_max, 0);
        mBtnBgRes = a.getColor(R.styleable.InputNumberView_imputBg, -1);
        Log.d(TAG, "initAttr: mMax===>" + mMax);
        Log.d(TAG, "initAttr: mMin===>" + mMin);
        Log.d(TAG, "initAttr: mStep===>" + mStep);
        Log.d(TAG, "initAttr: mDefaultValue===>" + mDefaultValue);
        Log.d(TAG, "initAttr: mBtnBgRes===>" + mBtnBgRes);


        //释放属性资源
        a.recycle();
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_input_number, this, false);
        //两种写法等价存在
        // View rootView = LayoutInflater.from(context).inflate(R.layout.view_input_number, this,true);
        addView(rootView);


        mMinuBtn = rootView.findViewById(R.id.number_minus);
        mAdditionBtn = rootView.findViewById(R.id.number_addition);
        mNumberInputView = rootView.findViewById(R.id.number_input_view);

    }

    //初始化事件
    private void initEvent() {
        mMinuBtn.setOnClickListener((v -> updateText(false)));
        mAdditionBtn.setOnClickListener((v -> updateText(true)));

    }

    /**
     * 更新控件值
     *
     * @param valueAdd true 标识增加  false标准减法
     */
    private void updateText(boolean valueAdd) {
        Log.d(TAG, "updateText: valueAdd===>" + valueAdd);
        if (valueAdd) {
            this.mDefaultValue = mDefaultValue + this.mStep;

        } else {
            this.mDefaultValue = mDefaultValue - this.mStep;
        }
        mNumberInputView.setText(String.valueOf(mDefaultValue));
    }


}
