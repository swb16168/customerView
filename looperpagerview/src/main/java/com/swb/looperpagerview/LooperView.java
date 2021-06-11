package com.swb.looperpagerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther 72774
 * @date 2021/6/7  16:26
 * @description
 */
public class LooperView extends FrameLayout {
    private static final String TAG = "LooperView";

    private ViewPager mViewPager;
    private LooperAdapter mLooperAdapter;

    public LooperView(@NonNull Context context) {
        this(context, null);
    }

    public LooperView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LooperView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        Log.d(TAG, "initView: ");
        View rootView = LayoutInflater.from(context).inflate(R.layout.looper_view, this, false);
        mViewPager = rootView.findViewById(R.id.looper_view_vp);
        mLooperAdapter = new LooperAdapter();
        mViewPager.setAdapter(mLooperAdapter);
        addView(rootView);
    }


    public void setLooperData(List<String> datas) {
        mLooperAdapter.setData(datas);
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 + 1);
    }


    //#region 轮播图适配器
    private class LooperAdapter extends PagerAdapter {
        private List<String> mData = new ArrayList<>();

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_looper, container, false);
            ImageView image = itemView.findViewById(R.id.looper_view_item_img);
            image.setImageResource(R.drawable.default_img);
            if (mData.size() > 0) {
                Picasso.with(container.getContext()).load(mData.get(position)).into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess:  Picasso");
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "onError: Picasso");
                    }
                });
            }

            if (image.getParent() instanceof ViewGroup) {
                ((ViewGroup) image.getParent()).removeView(image);
            }
            container.addView(image);
            return image;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //  super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        public void setData(List<String> datas) {
            this.mData.clear();
            this.mData.addAll(datas);
            notifyDataSetChanged();
        }
    }
//#endregion

}
