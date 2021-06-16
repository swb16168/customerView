package com.swb.customerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

import com.swb.customerview.R;

/**
 * @auther 72774
 * @date 2021/6/15  16:43
 * @description 模仿实现手机QQ 聊天框的侧滑功能
 */
public class SlideMenuView extends ViewGroup implements View.OnClickListener {
    private static final String TAG = "SlideMenuView";

    private static final int FUNCTION_DELETE = 0X30;
    private static final int DEFAULT_SCROLL_TIME = 1000;
    private static final int DEFAULT_SCROLL_TIME_MIN = 300;
    private int mFunction;
    private View mFunctionView;
    private View mContentView;
    private TextView mDeleteFun;
    private TextView mTopFun;
    private TextView mReadFun;
    private SlideFunClickListener mSlideFunListener = null;
    private float mDownX = 0;
    private float mDownY;
    private int mContentLeft = 0;
    private Scroller mScroller;
    private int mInterceDownX;

    public SlideMenuView(Context context) {
        this(context, null);
    }

    public SlideMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttr(context);

        mScroller = new Scroller(context);

    }

    private void initAttr(Context context) {
        TypedArray a = context.obtainStyledAttributes(R.styleable.SlideMenuView);
        mFunction = a.getInt(R.styleable.SlideMenuView_function, FUNCTION_DELETE);
        a.recycle();
    }

    //初始化完成后调用此方法
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalArgumentException("must have one and only one root element");
        }
        mContentView = getChildAt(0);
        mFunctionView = LayoutInflater.from(getContext()).inflate(R.layout.item_slide_function, this, false);
        addView(mFunctionView);
        //初始化功能页
        mDeleteFun = mFunctionView.findViewById(R.id.slide_fun_delete);
        mTopFun = mFunctionView.findViewById(R.id.slide_fun_top);
        mReadFun = mFunctionView.findViewById(R.id.slide_fun_read);
        //设置点击事件

        mDeleteFun.setOnClickListener(this);
        mTopFun.setOnClickListener(this);
        mReadFun.setOnClickListener(this);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //测量contentView
        //宽度 跟父控件一致，高度则有三种 1-包裹内容  2-与父控件一致  3-指定高度
        LayoutParams contentViewLayoutParams = mContentView.getLayoutParams();
        int contentViewHeight = contentViewLayoutParams.height;
        Log.d(TAG, "onMeasure: contentViewHeight-->" + contentViewHeight);
        int contentHeightMeasureSpec;
        if (contentViewHeight == LayoutParams.MATCH_PARENT) {
            contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (contentViewHeight == LayoutParams.WRAP_CONTENT) {
            contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
        } else {
            contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(contentViewHeight, MeasureSpec.EXACTLY);
        }
        Log.d(TAG, "onMeasure: contentHeightMeasureSpec--->" + contentHeightMeasureSpec);
        mContentView.measure(widthMeasureSpec, contentHeightMeasureSpec);
        //测量编辑部分,宽度约占3/4，高度与内容view相同
        int measuredHeight = mContentView.getMeasuredHeight();
        int functionViewWidth = (widthSize * 3) / 5;

        mFunctionView.measure(MeasureSpec.makeMeasureSpec(functionViewWidth, MeasureSpec.EXACTLY),
                measuredHeight);

        // mFunctionView.measure(functionViewWidth,measuredHeight);
        //测量自己
        setMeasuredDimension(widthSize + functionViewWidth, measuredHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int contentTop = 0;
        int contentRight = mContentLeft + mContentView.getMeasuredWidth();
        int contentBottom = contentTop + mContentView.getMeasuredHeight();
        mContentView.layout(mContentLeft, contentTop, contentRight, contentBottom);

        int functionViewLeft = contentRight;
        int functionViewTop = contentTop;
        int functionViewRight = functionViewLeft + mFunctionView.getMeasuredWidth();
        int functionViewBottom = functionViewTop + mFunctionView.getMeasuredHeight();
        mFunctionView.layout(functionViewLeft, functionViewTop, functionViewRight, functionViewBottom);
    }


    //功能页默认隐藏
    private boolean isFunOpen = false;
    private Direction mCurrDirection = Direction.NORMAL;

    //方向枚举
    enum Direction {
        LEFT, RIGHT, NORMAL
    }

    //滑动与点击 的事件冲突处理
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInterceDownX = (int) getX();
                break;
            case MotionEvent.ACTION_MOVE:
                //最小滑动距离
                int scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                int moveX = (int) ev.getX();
                if (Math.abs(moveX - mInterceDownX) > scaledTouchSlop) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            //标识手按下了屏幕
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                Log.d(TAG, "onTouchEvent: ACTION_DOWN scrollX--->  " + getScrollX());
                break;
            //标识手按下屏幕，并且在滑动
            case MotionEvent.ACTION_MOVE:
                //获取已经滑动的X值
                int scrollX = getScrollX();
                float moveX = event.getX();
                float moveY = event.getY();
                int dx = (int) (moveX - mDownX);
                if (dx > 0) {
                    mCurrDirection = Direction.RIGHT;
                } else {
                    mCurrDirection = Direction.LEFT;
                }

                //第一种方式滑动View
                //scrollTo(-dx,(int)mDownY);
                //判断边界
                int resultScrollX = -dx + scrollX;
                if (resultScrollX <= 0) {
                    scrollBy(0, 0);
                } else if (resultScrollX >= mFunctionView.getMeasuredWidth()) {
                    scrollTo(mFunctionView.getMeasuredWidth(), 0);
                } else {
                    scrollBy(-dx, 0);
                }
                //第二种方式，请求重新布局
                //mContentLeft = dx;
                //requestLayout();


                mDownX = moveX;
                mDownY = moveY;


                break;
            //标识手从屏幕上松开
            case MotionEvent.ACTION_UP:
                //滑动释放处理
                int hasBeenScrollX = getScrollX();
                int funViewWidth = mFunctionView.getMeasuredWidth();
                if (isFunOpen) {
                    //打开状态
                    if (mCurrDirection == Direction.RIGHT) {
                        //向右边滑动 距离小于 功能页宽度的 3/4时（也就是说最右边的一个功能展示区被隐藏了）， 那么则关闭
                        if (hasBeenScrollX < funViewWidth * 3 / 4) {
                            closeFunctionView();
                        } else {
                            openFunctionView();
                        }

                    } else if (mCurrDirection == Direction.LEFT) {
                        openFunctionView();
                    }

                } else {
                    //关闭状态  并且滑动的距离 大于 1/3 功能页的宽度,小于这个值则将其移动回原点
                    if (mCurrDirection == Direction.LEFT) {
                        if (hasBeenScrollX > (funViewWidth / 4)) {
                            openFunctionView();
                        } else {
                            closeFunctionView();
                        }
                    } else if (mCurrDirection == Direction.RIGHT) {
                        closeFunctionView();
                    }

                }
                break;
        }


        return true;//标识已经消费了移动事件
    }

    public boolean isFunOpen() {
        return isFunOpen;
    }

    //打开功能页
    private void openFunctionView() {
        int dx = Math.abs(mFunctionView.getMeasuredWidth() - getScrollX());
        int duration = (int) ((dx / (mFunctionView.getMeasuredWidth() * 4 / 5f)) * DEFAULT_SCROLL_TIME);
        if (duration > DEFAULT_SCROLL_TIME) {
            duration = DEFAULT_SCROLL_TIME;
        } else if (duration < DEFAULT_SCROLL_TIME_MIN) {
            duration = DEFAULT_SCROLL_TIME_MIN;
        }
        mScroller.startScroll(getScrollX(), 0, mFunctionView.getMeasuredWidth() - getScrollX(), 0, duration);
        invalidate();
        isFunOpen = true;
    }

    //关闭功能页
    private void closeFunctionView() {
        int dx = Math.abs(-getScrollX());
        int duration = (int) (dx / (mFunctionView.getMeasuredWidth() * 4 / 5f) * DEFAULT_SCROLL_TIME);

        if (duration > DEFAULT_SCROLL_TIME) {
            duration = DEFAULT_SCROLL_TIME;
        } else if (duration < DEFAULT_SCROLL_TIME_MIN) {
            duration = DEFAULT_SCROLL_TIME_MIN;
        }

        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, duration);
        invalidate();
        isFunOpen = false;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int currX = mScroller.getCurrX();
            scrollTo(currX, 0);
            invalidate();
        }


    }

    @Override
    public void onClick(View v) {
        if (mSlideFunListener == null) {
            return;
        }

        closeFunctionView();
        switch (v.getId()) {
            case R.id.slide_fun_delete:
                mSlideFunListener.onDeleteClick();
                break;
            case R.id.slide_fun_read:
                mSlideFunListener.onReadClick();
                break;

            case R.id.slide_fun_top:
                mSlideFunListener.onTopClick();
                break;
        }
    }

    public void setSlideFunClickListener(SlideFunClickListener listener) {
        this.mSlideFunListener = listener;
    }

    public interface SlideFunClickListener {
        void onDeleteClick();

        void onReadClick();

        void onTopClick();
    }
}
