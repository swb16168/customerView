package com.swb.customerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.swb.customerview.R;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @auther 72774
 * @date 2021/6/16  16:48
 * @description
 */
public class WatchFaceView extends View {
    private static final String TAG = "WatchFaceView";
    private static final boolean DEFAULT_SCALE_SHOW = true;
    private static final float CENTER_CIRCLE_RADIUS = 10f;
    private int mSecondColor;
    private int mMinuteColor;
    private int mHourColor;
    private int mScaleColor;
    private boolean mScaleShow;
    private int mResBgId;
    private Paint mSecondPaint;
    private Paint mMinutePaint;
    private Paint mHourPaint;
    private Paint mScalePaint;
    private int mDiameter;  //表的直径
    private int mWatchRadius;
    private Bitmap mBackgroundImage = null;
    private Rect mSrcRect;
    private Rect mDesRect;
    private Calendar mCalendar = Calendar.getInstance();
    private float mScaleLength;

    public WatchFaceView(Context context) {
        this(context, null);
    }

    public WatchFaceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WatchFaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        //设置时区
        mCalendar.setTimeZone(TimeZone.getDefault());
        //创建画笔
        initPaints();

    }

    private void initPaints() {

        mSecondPaint = new Paint();             //秒针的画笔
        mSecondPaint.setColor(mSecondColor);    //设置画笔的颜色
        mSecondPaint.setStyle(Paint.Style.STROKE);  //画笔样式
        mSecondPaint.setStrokeWidth(2f);        //设置画笔的宽度
        mSecondPaint.setAntiAlias(true);        //抗锯齿效果


        mMinutePaint = new Paint();             //分针画笔
        mMinutePaint.setColor(mMinuteColor);    //设置画笔的颜色
        mMinutePaint.setStyle(Paint.Style.STROKE);  //画笔样式
        mMinutePaint.setStrokeWidth(4f);        //设置画笔的宽度
        mMinutePaint.setAntiAlias(true);        //抗锯齿效果

        mHourPaint = new Paint();           //时针画笔
        mHourPaint.setColor(mHourColor);    //设置画笔的颜色
        mHourPaint.setStyle(Paint.Style.STROKE);  //画笔样式
        mHourPaint.setStrokeWidth(8f);        //设置画笔的宽度
        mHourPaint.setAntiAlias(true);        //抗锯齿效果


        mScalePaint = new Paint();          //刻度盘画笔
        mScalePaint.setColor(mScaleColor);    //设置画笔的颜色
        mScalePaint.setStyle(Paint.Style.STROKE);  //画笔样式
        mScalePaint.setStrokeWidth(4f);        //设置画笔的宽度
        mScalePaint.setAntiAlias(true);        //抗锯齿效果

    }


    private void initAttr(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WatchFaceView);
        mSecondColor = a.getColor(R.styleable.WatchFaceView_secondColor, getResources().getColor(R.color.watch_second));
        mMinuteColor = a.getColor(R.styleable.WatchFaceView_secondColor, getResources().getColor(R.color.watch_hour));
        mHourColor = a.getColor(R.styleable.WatchFaceView_secondColor, getResources().getColor(R.color.watch_minute));
        mScaleColor = a.getColor(R.styleable.WatchFaceView_secondColor, getResources().getColor(R.color.watch_scale));
        mScaleShow = a.getBoolean(R.styleable.WatchFaceView_scaleShow, DEFAULT_SCALE_SHOW); //是否显示表盘
        mResBgId = a.getResourceId(R.styleable.WatchFaceView_faceBg, -1); //背景图

        if (mResBgId != -1) {
            mBackgroundImage = BitmapFactory.decodeResource(getResources(), R.mipmap.watch_face_bg);
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量自己
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getMode(heightMeasureSpec);
        //super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        //减去内外边距
        width = width - (getPaddingLeft() + getPaddingRight());
        height = height - (getPaddingTop() + getPaddingBottom());
        //最小的一边为表盘的直径
        mDiameter = Math.min(width, height);
        mWatchRadius = mDiameter / 2;
        setMeasuredDimension(mDiameter, mDiameter);
        initRect();
    }

    /**
     * 创建需要的图案
     */
    private void initRect() {
        if (mBackgroundImage == null) {
            Log.d(TAG, "initRect: mBackgroundImage is null...");
            return;
        }
        //从指定图片中 截取图案大小
        mSrcRect = new Rect();
        mSrcRect.left = 0;
        mSrcRect.top = 0;
        mSrcRect.right = mBackgroundImage.getWidth();
        mSrcRect.bottom = mBackgroundImage.getHeight();
        //绘制区域
        mDesRect = new Rect();
        mDesRect.left = 0;
        mDesRect.top = 0;
        mDesRect.right = mDiameter;
        mDesRect.bottom = mDiameter;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //设置时间
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mBackgroundImage = null;
        if (mBackgroundImage != null) {
            //绘制位图
            canvas.drawBitmap(mBackgroundImage, mSrcRect, mDesRect, mScalePaint);
        } else {
            //绘制表的背景 刻度盘
            drawScale(canvas);
        }


        //秒针为0时先更新秒针
        if(mCalendar.get(Calendar.SECOND) == 0){

            //绘制秒针
            drawSecondLine(canvas);
            //绘制分针
            drawMinuteLine(canvas);
            drawHourLine(canvas);
        }else{

            drawHourLine(canvas);
            //绘制分针
            drawMinuteLine(canvas);
            //绘制秒针
            drawSecondLine(canvas);
        }

    }

    private int drawHourLine(Canvas canvas) {
        canvas.save();
        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);
        float minuteOffsetRotate = minute /2f;
        float hourRotate = hour * 30 + minuteOffsetRotate;
        canvas.rotate(hourRotate,mWatchRadius,mWatchRadius);
        int hourRadius = (int) (mWatchRadius * 0.6f);
        canvas.drawLine(mWatchRadius,mWatchRadius - hourRadius,mWatchRadius,mWatchRadius - CENTER_CIRCLE_RADIUS,mHourPaint);
        canvas.restore();
        return minute;
    }

    private void drawMinuteLine(Canvas canvas ) {
        canvas.save();
        int minuteRadius = (int) (mWatchRadius * 0.65f);
        float minuteRotate = mCalendar.get(Calendar.MINUTE) * 6;
        canvas.rotate(minuteRotate,mWatchRadius,mWatchRadius);
        canvas.drawLine(mWatchRadius,mWatchRadius - minuteRadius,mWatchRadius,mWatchRadius - CENTER_CIRCLE_RADIUS,mMinutePaint);
        canvas.restore();
    }

    private void drawSecondLine(Canvas canvas) {
        canvas.save();
        int second = mCalendar.get(Calendar.SECOND);
        int secondRadius = (int) (mWatchRadius * 0.7f);
        float secondRotate = second * 6;
        canvas.rotate(secondRotate,mWatchRadius,mWatchRadius);
        canvas.drawLine(mWatchRadius,mWatchRadius - secondRadius,mWatchRadius,mWatchRadius - CENTER_CIRCLE_RADIUS,mSecondPaint);
        canvas.restore();
    }


    private boolean isUpdate = false;
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isUpdate = true;
        post(new Runnable() {
            @Override
            public void run() {
                if (isUpdate) {
                    Log.d(TAG, "run: onAttachedToWindow--> " + isUpdate);
                    invalidate();

                    postDelayed(this,1000);
                }else{
                    removeCallbacks(this);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isUpdate = false;
    }

    private void drawHour(Canvas canvas, int hour) {
    }

    private enum PointType {
        HOUR, MINUTE, SECOND
    }

    //绘制指针
    private void drawPoint(Canvas canvas, int hour, int minute, int second, PointType pointType) {

        switch (pointType) {
            case HOUR:
                if (hour > 12) {
                    hour = hour - 12;
                }
                //时针角度 加上分针的偏移量 minuteOffset值 一个刻度盘为30度∠ ，一小时有60分钟
                float minuteOffset = minute / 2 ;
                double hourTh = hour  * 30  + minuteOffset;
                float startX = mWatchRadius ;
                float startY = mWatchRadius ;
                Log.d(TAG, "drawPoint: hourTh-->" + hourTh);

                Log.d(TAG, "drawPoint: mWatchRadius-->"  + mWatchRadius);
                float endX =  mWatchRadius * (float)Math.sin(Math.toRadians(hourTh))  +  mScaleLength * 10 ;
                float endY =  mWatchRadius * (float)Math.cos(Math.toRadians(hourTh));
                Log.d(TAG, "drawPoint: endX-->" + endX);
                Log.d(TAG, "drawPoint: endY--->" + endY);
                canvas.drawLine(startX, startY,endX,endY ,mHourPaint);
                break;
        }
    }

    /**
     * 绘制表的背景
     *
     * @param canvas
     */
    private void drawScale(Canvas canvas) {
        //构建两个圆 作为表的刻度盘
        int radius = mDiameter / 2;
        float innerRadius = radius * 0.8f;
        float outerRadius = radius * 0.9f;
        //方法一，利用canvas 的旋转功能完成绘制
            /*canvas.save();
            for (int i = 0; i < 12; i++) {
                canvas.drawLine(radius ,radius-innerRadius,radius,radius - outerRadius ,mScalePaint);
                canvas.rotate(30,radius,radius);
            }
            canvas.restore();
            */
        //方法二：利用三角函数关系 获取坐标
        for (int i = 0; i < 12; i++) {
            float startX = (float) (radius + innerRadius * Math.sin(Math.toRadians(i * 30)));
            float startY = (float) (radius - innerRadius * Math.cos(Math.toRadians(i * 30)));
            if (i % 3 == 0) {
                mScalePaint.setStrokeWidth(8f);        //设置画笔的宽度
            } else {
                mScalePaint.setStrokeWidth(4f);         //设置画笔的宽度
            }

            float endX = (float) (radius + outerRadius * Math.sin(Math.toRadians(i * 30)));
            float endY = (float) (radius - outerRadius * Math.cos(Math.toRadians(i * 30)));
            mScaleLength = startX - endX;
            canvas.drawLine(startX, startY, endX, endY, mScalePaint);
        }
        //绘制表的中心圆圈
        canvas.drawCircle(radius, radius, CENTER_CIRCLE_RADIUS, mScalePaint);
    }
}
