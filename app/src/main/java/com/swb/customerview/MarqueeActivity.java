package com.swb.customerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.swb.customerview.view.MarqueeLayout;
import com.swb.customerview.view.MarqueeLayoutVertical;
import com.swb.customerview.view.MarqueeLayout_1;

public class MarqueeActivity extends AppCompatActivity {
    private static final String TAG = "MarqueeActivity";
    private MarqueeLayoutVertical mMarqueeLayoutVertical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquee);

        MarqueeLayout_1 marquee = findViewById(R.id.marquee_layout);

        findViewById(R.id.marquee_btn).setOnClickListener((v -> {
            /*if (marquee.isMarqueeRuning()) {
                marquee.stopMarquee();
            }else {
                marquee.startMaquee();
            }*/
        }));

        mMarqueeLayoutVertical = findViewById(R.id.marquee_layout_vertical);
        mMarqueeLayoutVertical.setTextArray(new String[]{"张三","李四","王五","赵六","钱七"});

    }
}
