package com.swb.customerview.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.swb.customerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther 72774
 * @date 2021/6/7  10:59
 * @description
 */
public class LooperPageView extends FrameLayout {
    private static final String TAG = "LooperPageView";

    private ViewGroup mIndicator;
    private ViewPager mImagePager;
    private ImagePagerAdapter mImagePagerAdapter;


    private static Handler sHandler = new Handler(Looper.myLooper());

    public LooperPageView(@NonNull Context context) {
        this(context, null);
    }

    public LooperPageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LooperPageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

        new Thread(() -> {
            while (true) {
                sHandler.post(()-> mImagePager.setCurrentItem(currPageIndex));
                currPageIndex ++;
                if (currPageIndex >= mImagePagerAdapter.getCount()) {
                    currPageIndex = 0;
                }
                SystemClock.sleep(2000);
            }

        }).start();


    }

    private int currPageIndex = 0;

    private void initView(@NonNull Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.looper_page_view, this, true);
        //获取指示器
        mIndicator = rootView.findViewById(R.id.looper_pager_point);
        //获取ViewPager
        mImagePager = rootView.findViewById(R.id.looper_pager_view);
        mImagePagerAdapter = new ImagePagerAdapter();


        mImagePager.setAdapter(mImagePagerAdapter);


        mImagePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: pager 滑动");

                int childCount = mIndicator.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    mIndicator.getChildAt(i).setBackground(
                            getResources().getDrawable(i == position ? R.drawable.indicator_bg : R.drawable.indicator_bg_1, null));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void setData(List<String> imgUrls) {
        mImagePagerAdapter.setData(imgUrls);
        if (mIndicator != null) {
            mIndicator.removeAllViews();
            Log.d(TAG, "setData: imgUrls-->" + imgUrls.size());
            for (int i = 0; i < imgUrls.size(); i++) {
                Log.d(TAG, "setData: 循环次数-i---》" + i);
                //处理指示器
                View child = new View(mIndicator.getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.weight = 5;
                layoutParams.height = 5;
                layoutParams.setMargins(0, 0, 5, 5);
                layoutParams.gravity = LinearLayout.TEXT_ALIGNMENT_CENTER;

                ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(mIndicator.getLayoutParams());
                params.height = 20;
                params.width = 20;
                params.resolveLayoutDirection(50);

                child.setLayoutParams(params);

                if (i == 0) {
                    child.setBackground(getResources().getDrawable(R.drawable.indicator_bg, null));
                } else {
                    child.setBackground(getResources().getDrawable(R.drawable.indicator_bg_1, null));
                }
                mIndicator.addView(child);
                Log.d(TAG, "setData: mIndicator -->addview");
            }

        }
    }


    //#region 内部类---viewPager适配器

    class ImagePagerAdapter extends PagerAdapter {
        private static final String TAG = "ImagePagerAdapter";
        private List<String> mData = new ArrayList<>();

        private ImageView mImageView;

        @Override
        public int getCount() {
            return mData.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View rootView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_image_page, container, false);
            mImageView = rootView.findViewById(R.id.item_image_container);
            if (mData.size() > 0) {
                Picasso.with(container.getContext()).load(mData.get(position)).into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess:Picasso ");
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "onError: Picasso");
                    }
                });
            }

            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container,position,object); 这一句要删除，否则报错
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        public void setData(List<String> imgUrls) {
            mData.clear();
            mData.addAll(imgUrls);
            notifyDataSetChanged();
        }
    }
    //#endregion
}
