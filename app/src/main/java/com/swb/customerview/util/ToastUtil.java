package com.swb.customerview.util;

import android.content.Context;
import android.widget.Toast;

import com.swb.customerview.App;


/**
 * @auther 72774
 * @date 2021/6/5  16:18
 * @description
 */
public class ToastUtil {

    public static void showToast(Context context, String msg) {
        App.getHandler().post(() -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show());
    }
}
