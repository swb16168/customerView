package com.swb.looperpagerview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @auther 72774
 * @date 2021/6/8  11:07
 * @description
 */
public class DJLooerView extends FrameLayout {
    public DJLooerView(@NonNull Context context) {
        this(context,null);
    }

    public DJLooerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DJLooerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {

    }
}
