package com.swb.customerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.swb.customerview.view.LooperPageView;
import com.swb.looperpagerview.LooperView;

import java.util.ArrayList;
import java.util.List;

public class LooperActivity extends AppCompatActivity {
    private static final String TAG = "LooperActivity";
    //初始化图片数据
    public List<String> imgUrls = new ArrayList<String>() {{
        add("https://article-fd.zol-img.com.cn/t_s640x2000/g6/M00/01/03/ChMkKmCrFqaIBzPcABMdfhaje7EAAPW6wIpb7sAEx2W904.jpg");
        add("https://imgopen.xmcdn.com/group78/M03/D9/99/wKgO1l5ltGiy2KG_AAXOmqbIkhM147.jpg!op_type=3&columns=640&rows=640");
        add("https://imgopen.xmcdn.com/storages/49d1-audiofreehighqps/A6/5F/CMCoOSAEgxdAAALCXQCwc9Gu.jpeg!op_type=3&columns=640&rows=640");
        add("https://imgopen.xmcdn.com/storages/87fa-audiofreehighqps/E6/A3/CMCoOSYEXdC2AAMcPgCjyeaT.jpeg!op_type=3&columns=640&rows=640");
        add("https://imgopen.xmcdn.com/storages/e3e4-audiofreehighqps/38/86/CMCoOSAEWwulAAP7OACiws39.jpeg!op_type=3&columns=640&rows=640");
        add("https://imgopen.xmcdn.com/storages/1870-audiofreehighqps/6E/A1/CMCoOSIEPgXoAAMfXACaJIyJ.jpeg!op_type=3&columns=640&rows=640");
    }};
    private LooperView mLooperView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looper);
        Log.d(TAG, "onCreate: -->" + 1 % 4);
        mLooperView = findViewById(R.id.main_looper);
        //mLooperView.setLooperData(imgUrls);
    }



}
