package com.swb.customerview;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.swb.customerview.view.LoginPageView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        LoginPageView loginPageView = findViewById(R.id.custom_login);
        loginPageView.setReportText("同意【XXX】用户协议");
        loginPageView.setOnLoginPageActionListener(new LoginPageView.OnLoginPageActionListener() {
            @Override
            public void onGetVerifytCodeClick(String phoneNum) {
                Log.d(TAG, "onGetVerifytCodeClick: 获取验证码");
            }


            @Override
            public void onLoginClick(String phoneNum, String verifytCode) {
                Log.d(TAG, "onLoginClick: 开始登录---> "+ phoneNum + "   验证码-->" + verifytCode);
                App.getHandler().postDelayed(()->loginPageView.onLoginSuccess(),2000);
            }

            @Override
            public void onOpenUserReport() {
                Log.d(TAG, "onOpenUserReport: 打开用户协议");
            }
        });


    }
}
