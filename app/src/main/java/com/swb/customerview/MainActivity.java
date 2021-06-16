package com.swb.customerview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.swb.customerview.view.KeyPadExView;
import com.swb.customerview.view.NumberKeyPad;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KeyPadExView viewById = findViewById(R.id.keypad_ex);
        viewById.setKeyPadClickListener(new KeyPadExView.KeyPadClickListener() {
            @Override
            public void onItemClick(int keyValue) {
                Log.d(TAG, "onItemClick: keyValue--->" + keyValue);
            }

            @Override
            public void onBackClick() {
                Log.d(TAG, "onBackClick: 清除。。。");
            }
        });
        /**
         * 代码设置 背景图
         *   Drawable drawable = getResources().getDrawable(R.drawable.shape_btn, null);
         *   findViewById(R.id.shape_btn).setBackground(drawable);
         */


        /*LinerLayout实现的KeyPad*/
        /*NumberKeyPad keypad = findViewById(R.id.key_pad);
        keypad.setOnKeyPadListener(new NumberKeyPad.OnKeyPadNumberListener() {
            @Override
            public void onNumberInput(int number) {
                Log.d(TAG, "onNumberInput: input--->" + number);
            }

            @Override
            public void ondDelete() {
                Log.d(TAG, "onNumberInput: input--->delete" );
            }
        });*/

        //流式布局
        //startActivity(new Intent(this,FlowActivity.class));

        //侧滑view
        startActivity(new Intent(this,SlideMenuActivity.class));
    }
}
