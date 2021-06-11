package com.swb.customerview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.swb.customerview.R;

/**
 * @auther 72774
 * @date 2021/6/3  18:09
 * @description
 */
public class NumberKeyPad extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "MyKeyPad";
    private View mRootView;
    private OnKeyPadNumberListener mKeyPadListener;

    public NumberKeyPad(Context context) {
        this(context, null);
    }

    public NumberKeyPad(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberKeyPad(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRootView = LayoutInflater.from(context).inflate(R.layout.view_keypad, this, true);
        findViewById(R.id.number_0).setOnClickListener(this);
        findViewById(R.id.number_1).setOnClickListener(this);
        findViewById(R.id.number_2).setOnClickListener(this);
        findViewById(R.id.number_3).setOnClickListener(this);
        findViewById(R.id.number_4).setOnClickListener(this);
        findViewById(R.id.number_5).setOnClickListener(this);
        findViewById(R.id.number_6).setOnClickListener(this);
        findViewById(R.id.number_7).setOnClickListener(this);
        findViewById(R.id.number_8).setOnClickListener(this);
        findViewById(R.id.number_9).setOnClickListener(this);
        findViewById(R.id.number_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mKeyPadListener ==null){
            Log.d(TAG, "callback is null.... ");
            return;
        }
        if(v.getId()== R.id.number_back){
            //Log.d(TAG, "onClick: 点击了 back键");
            mKeyPadListener.ondDelete();
        }else {
            String text = ((TextView) v).getText().toString();
            mKeyPadListener.onNumberInput(Integer.parseInt(text));
            //Log.d(TAG, "onClick: 点击了数字键===》" + text);
        }

    }

    public void setOnKeyPadListener(OnKeyPadNumberListener listener){
        this.mKeyPadListener = listener;
    }
    public interface OnKeyPadNumberListener {
        void onNumberInput(int number);

        void ondDelete();
    }
}
