package com.swb.customerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.swb.customerview.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class FlowActivity extends AppCompatActivity {

    private List<String> names = new ArrayList<String>() {{
        add("张三");
        add("李四李四");
        add("王五");
        add("赵六赵六赵六赵六赵六赵六赵六赵六赵六赵六赵六赵六赵六赵六赵六赵六赵六赵六赵六赵六赵六赵六赵六");
        add("钱七");
        add("孙八");
        add("老九钱七");
        add("老1钱七");
        add("老2钱七");
        add("老3钱七");
        add("老4钱七");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);

        FlowLayout flowLayout = this.findViewById(R.id.flow_container);
        flowLayout.setData(names);

        //names.add("老铁~~");
        //flowLayout.postDelayed(() -> flowLayout.setData(null), 10000);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }
}
