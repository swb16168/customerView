package com.swb.customerview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @auther 72774
 * @date 2021/6/9  16:01
 * @description
 */
public class CircleView extends View {
    private static final String TAG = "CircleView";
    private int mColor = Color.RED;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mPaint.setColor(mColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d(TAG, "onDraw: getPaddingLeft--->" + getPaddingLeft());
        Log.d(TAG, "onDraw: getPaddingRight--->" + getPaddingRight());
        Log.d(TAG, "onDraw: getPaddingTop--->" + getPaddingTop());
        Log.d(TAG, "onDraw: getPaddingBottom--->" + getPaddingBottom());

        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int radius = Math.min(width, height) / 2;
        canvas.drawCircle(getPaddingLeft() + width  / 2, height / 2  +  getPaddingLeft(), radius , mPaint);
    }
}
