package com.swb.customerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.swb.customerview.view.SlideMenuView;

public class SlideMenuActivity extends AppCompatActivity {
    private static final String TAG = "SlideMenuActivity";
    private TextView mTouchTV;
    private int mDownX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidemenu);
        //模仿手机QQ的侧滑删除聊天框

        mTouchTV = findViewById(R.id.touch_text);
        SlideMenuView slideMenu = findViewById(R.id.slide_menu);
        slideMenu.setSlideFunClickListener(new SlideMenuView.SlideFunClickListener() {
            @Override
            public void onDeleteClick() {
                Log.d(TAG, "onDeleteClick: ...");
            }

            @Override
            public void onReadClick() {
                Log.d(TAG, "onReadClick: ");
            }

            @Override
            public void onTopClick() {
                Log.d(TAG, "onTopClick: ");
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int dx = moveX - mDownX;

                int moveResult = -dx + mTouchTV.getScrollX();


                Log.d(TAG, "onTouchEvent: moveResult--->" + moveResult);
                Log.d(TAG, "onTouchEvent: mTouchTV.getMeasuredWidth() /2--->" + (mTouchTV.getMeasuredWidth() /2));
                if(moveResult >=  mTouchTV.getMeasuredWidth() /2 ){
                    mTouchTV.scrollTo(mTouchTV.getMeasuredWidth() /2,0);
                }else{
                    mTouchTV.scrollTo(moveResult,0);
                }
                //mTouchTV.scrollBy(dx,0);
                mDownX = moveX;
                break;

            case MotionEvent.ACTION_UP:

                break;
        }


        return false;
    }
}
