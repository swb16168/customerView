package com.swb.customerview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.swb.customerview.view.NumberKeyPad;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        //startActivity(new Intent(this,FlowActivity.class));

    }
}
