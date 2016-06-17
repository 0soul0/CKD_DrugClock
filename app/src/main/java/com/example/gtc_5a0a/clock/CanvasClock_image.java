package com.example.gtc_5a0a.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextPaint;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by GTC_5A0A on 2016/1/17.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.text.TextPaint;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.TimeZone;

/**
 * Analog clock purely use Canvas API.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-1-26 19:58:54
 */
public class CanvasClock_image extends View implements View.OnTouchListener {
    /**
     * Welcome to my personal blog.
     */
    public static final String BLOG_2DXGUJUN = "http://2dxgujun.com";

    // ---------------------- Robot Dimension ---------------------- //
    private Paint mPaint;
    private Paint mEyePaint;

    private float mHeadRadius = 74f;
    private float mHeadBodySpace = 5f;
    private float mEyeRadius = 10f;
    private float mEyeSpace = 74f;
    private float mAntWidth = 10f;
    private float mAntHeight = 24f;
    private float mAntRoundCorner = 10f;
    private float mBodyWidth = 150f;
    private float mBodyHeight = 140f;
    private float mBodyRoundCorner = 7f;
    private float mArmWidth = 34f;
    private float mArmHeight = 110f;
    private float mArmBodySpace = 5f;
    private float mArmRoundCorner = 15f;
    private float mFootWidth = 40f;
    private float mFootHeight = 54f;
    private float mFootSpace = 10f;
    private float mFootRoundCorner = 5f;

    private RectF mHeadRectF;

    private PointF mLeftEyePointF;
    private PointF mRightEyePointF;

    private RectF mLeftAntRectF;
    private RectF mRightAntRectF;

    private RectF mBodyRectF;

    private RectF mLeftArmRectF;
    private RectF mRightArmRectF;

    private RectF mLeftFootRectF;
    private RectF mRightFootRectF;
    // ------------------------------------------------------------- //

    private Paint mDialPaint;
    private Paint mLittleScalePaint;
    private Paint mLargeScalePaint;
    private Paint mHourHandPaint;
    private Paint mMinuteHandPaint;
    private Paint mSecondHandPaint;
    private Paint mLinkPaint;

    private float mDialRadius;
    private float mDialStrokeWidth;
    private int mDialPadding;

    private float mLittleScaleLength;
    private float mLittleScaleStrokeWidth;

    private float mLargeScaleLength;
    private float mLargeScaleStrokeWidth;

    private float mHourHandLength;
    private float mMinuteHandLength;
    private float mSecondHandLength;

    private float mHourHandTailLength;
    private float mMinuteHandTailLength;
    private float mSecondHandTailLength;

    private float mHourHandStrokeWidth;
    private float mMinuteHandStrokeWidth;
    private float mSecondHandStrokeWidth;

    private float mLinkTextSize;

    private float mDistance;
    private Path mLinkPath;
    private RectF mLinkRectF;

    private Time mCalendar;

    private int mHours;
    private int mMinutes;
    private int mSeconds;

    private boolean mAttached;
    //----------------------------------------------------//
    public static int[] drugmake = new int[12];
    private Paint Colorscslepaint;
    private int mMiiscomputer;
    private int mLaser=0;
    private int redalarm=0;
    private int mMiis = 0;
    private Bitmap bmp;
    private Paint myRectPaint;
    private Runnable mTickRunnable = new Runnable() {
        @Override
        public void run() {
//            Log.i("LOG", "Runnable Tick");
            onTimeChanged();
            invalidate();
            if (mAttached) {
                mTickHandler.postDelayed(mTickRunnable, 100);
            }
        }
    };

    private Handler mTickHandler;

    public CanvasClock_image(Context context) {
        this(context, null);
    }


    public CanvasClock_image(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        mTickHandler = new Handler();

        mCalendar = new Time();
        mLinkPath = new Path();
        mLinkRectF = new RectF();

        mHeadRectF = new RectF();
        mLeftEyePointF = new PointF();
        mRightEyePointF = new PointF();
        mLeftAntRectF = new RectF();
        mRightAntRectF = new RectF();
        mBodyRectF = new RectF();
        mLeftArmRectF = new RectF();
        mRightArmRectF = new RectF();
        mLeftFootRectF = new RectF();
        mRightFootRectF = new RectF();


    }


    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);

        mEyePaint = new Paint(mPaint);
        mEyePaint.setColor(Color.WHITE);

        mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDialPaint.setStyle(Paint.Style.STROKE);
        mDialPaint.setStrokeWidth(mDialStrokeWidth);

        mLinkPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mLinkPaint.setTextSize(mLinkTextSize);
        mLinkPaint.setTextAlign(TextPaint.Align.CENTER);

        mLittleScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLittleScalePaint.setStyle(Paint.Style.STROKE);
        mLittleScalePaint.setStrokeWidth(mLittleScaleStrokeWidth);

        mLargeScalePaint = new Paint(mLittleScalePaint);
        mLargeScalePaint.setStrokeWidth(mLargeScaleStrokeWidth);

        mHourHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourHandPaint.setStyle(Paint.Style.STROKE);
        mHourHandPaint.setStrokeWidth(mHourHandStrokeWidth);
        mHourHandPaint.setStrokeCap(Paint.Cap.ROUND);

        mMinuteHandPaint = new Paint(mHourHandPaint);
        mMinuteHandPaint.setStrokeWidth(mMinuteHandStrokeWidth);

        mSecondHandPaint = new Paint(mMinuteHandPaint);
        mSecondHandPaint.setStrokeWidth(mSecondHandStrokeWidth);
        mSecondHandPaint.setColor(Color.RED);
//------------------------------------------------------------//

        Colorscslepaint = new Paint();
        Colorscslepaint.setAntiAlias(true);
        myRectPaint = new Paint();
        myRectPaint.setAntiAlias(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            mTickHandler.post(mTickRunnable);

            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

            getContext().registerReceiver(mIntentReceiver, filter);
        }

        // NOTE: It's safe to do these after registering the receiver since the always runs
        // in the main thread, therefore the receiver can't run before this method returns.

        // The time zone may have changed while the receiver wasn't registered, so update the Time.
        mCalendar = new Time();

        // Make sure we update to the current time.
        onTimeChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



  /*      // Draw head.
        canvas.drawArc(mHeadRectF, -180, 180, true, mPaint);
        // Draw body.
        canvas.drawRoundRect(mBodyRectF, mBodyRoundCorner, mBodyRoundCorner, mPaint);
        // Draw arms.
        canvas.drawRoundRect(mLeftArmRectF, mArmRoundCorner, mArmRoundCorner, mPaint);
        canvas.drawRoundRect(mRightArmRectF, mArmRoundCorner, mArmRoundCorner, mPaint);
        // Draw feet.
        canvas.drawRoundRect(mLeftFootRectF, mFootRoundCorner, mFootRoundCorner, mPaint);
        canvas.drawRoundRect(mRightFootRectF, mFootRoundCorner, mFootRoundCorner, mPaint);
        // Draw eyes.
        canvas.drawCircle(mLeftEyePointF.x, mLeftEyePointF.y, mEyeRadius, mEyePaint);
        canvas.drawCircle(mRightEyePointF.x, mRightEyePointF.y, mEyeRadius, mEyePaint);
//
        // Draw left ant.
        float px = mLeftEyePointF.x + (mRightEyePointF.x - mLeftEyePointF.x) / 2;
        float py = mLeftEyePointF.y;
        canvas.save();
        canvas.rotate(-30, px, py);
        canvas.drawRoundRect(mLeftAntRectF, mAntRoundCorner, mAntRoundCorner, mPaint);
        canvas.restore();

        // Draw right ant.
        canvas.save();
        canvas.rotate(30, px, py);
        canvas.drawRoundRect(mRightAntRectF, mAntRoundCorner, mAntRoundCorner, mPaint);
        canvas.restore();
*/
        // Draw dial.
        canvas.translate(getWidth() / 2, getHeight() / 2);
//        canvas.drawCircle(0, 0, mDialRadius, mDialPaint);
/*
        // Draw my blog link.
        canvas.save();
        canvas.translate(-mDistance, -mDistance);
        canvas.drawTextOnPath(BLOG_2DXGUJUN, mLinkPath, 0, 0, mLinkPaint);
        canvas.restore();
*/
        // Draw dial scale.
        int xx = getWidth();
        float y = -mDialRadius + mDialPadding + xx / 15;

        Log.i("canvevv", y + "");
//        //  String[] number={"1","2","3","4","5","6","7","8","9","10","11","12"};
//        for (int i = 0; i < 60; i++) {
//            if (i % 5 == 0) {
//                //   canvas.drawLine(0, y, 0, y + mLargeScaleLength, mLargeScalePaint);
//                canvas.drawLine(0, y, 0, y + mLargeScaleLength, mLargeScalePaint);
//
//            } else {
//                canvas.drawLine(0, y, 0, y + mLittleScaleLength, mLittleScalePaint);
//            }
//            canvas.rotate(360 / 60);
//        }


        // 秒鐘_圓
        if (mHours >= 6 && mHours < 18) {
            //sun
            //底圖_圓
            Colorscslepaint.setStyle(Paint.Style.STROKE);
            Colorscslepaint.setARGB(150, 255, 228, 196);
            canvas.drawCircle(0, 0, Math.abs(y), Colorscslepaint);
            canvas.drawCircle(0, 0, Math.abs(y + mLargeScaleLength + xx / 30), Colorscslepaint);
            Colorscslepaint.reset();
            int r = 245, g = 211, b = 94;
            for (int i = 0; i < 600 - mMiiscomputer; i++) {
//245 -  250           211   -   109       94  -  63
//            Log.i("canves", "   " + r + "   " + g + "   " + b);
                Colorscslepaint.setARGB(255, r, g, b);
                if (i % 60 == 0 && i < 300) {
                    r--;
                } else if (i % 60 == 0 && i > 300) {
                    r++;
                }
                if (i % 3 == 0 && i < 300) {
                    g--;
                } else if (i % 3 == 0 && i > 300) {
                    g++;
                }
                if (i % 10 == 0 && i < 300) {
                    b--;
                } else if (i % 10 == 0 && i > 300) {
                    b++;
                }
                canvas.drawArc(new RectF(-Math.abs(y), -Math.abs(y), Math.abs(y), Math.abs(y)), 270, 0.9f, true, Colorscslepaint);
                canvas.rotate(-0.6f);
            }
        } else {
            //Moon
            //底圖_圓
            Colorscslepaint.setStyle(Paint.Style.STROKE);
            Colorscslepaint.setARGB(30, 12, 206, 208);
            canvas.drawCircle(0, 0, Math.abs(y), Colorscslepaint);
            canvas.drawCircle(0, 0, Math.abs(y + mLargeScaleLength + xx / 30), Colorscslepaint);
            Colorscslepaint.reset();
            int r = 12, g = 206, b = 208;
            for (int i = 0; i < 600 - mMiiscomputer; i++) {
//12  -28        206   -   52          208   -  96
//            Log.i("canves", "   " + r + "   " + g + "   " + b);
                Colorscslepaint.setARGB(255, r, g, b);
                if (i % 15 == 0 && i < 300) {
                    r++;
                } else if (i % 15 == 0 && i > 300) {
                    r--;
                }
                if (i % 2 == 0 && i < 300) {
                    g--;
                } else if (i % 2 == 0 && i > 300) {
                    g++;
                }
                if (i % 3 == 0 && i < 300) {
                    b--;
                } else if (i % 3 == 0 && i > 300) {
                    b++;
                }
                canvas.drawArc(new RectF(-Math.abs(y), -Math.abs(y), Math.abs(y), Math.abs(y)), 270, 0.9f, true, Colorscslepaint);
                canvas.rotate(-0.6f);
            }

        }
        canvas.rotate(-0.6f * mMiiscomputer);


        //中心圓
        Colorscslepaint.setARGB(255, 255, 255, 255);
        canvas.drawCircle(0, 0, Math.abs(y + mLargeScaleLength + xx / 30), Colorscslepaint);


        //分隔
        for (int i = 0; i < 12; i++) {
            Colorscslepaint.setARGB(255, 236, 231, 216);
            Colorscslepaint.setStrokeWidth(mSecondHandStrokeWidth + getWidth() / 30);
            canvas.drawLine(0, y, 0, y + mLargeScaleLength + xx / 30, Colorscslepaint);
            canvas.rotate(360 / 12);
        }


        //字+中心圖片

        if (mHours >= 6 && mHours < 18) {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.sun);
        } else {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.moon);
        }

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float scaleWidth = (float) width / 7500;
        float scaleHeight = (float) width / 7500;
        Log.i("canves", width + "    " + scaleWidth);
        // 取得想要缩放的matrix參數
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的圖片
        Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        canvas.drawBitmap(newbm, -newbm.getWidth() / 2, -newbm.getHeight() / 2, null);
//        Log.i("canvess", "W   " + scaleWidth + "     " + newbm.getWidth() + "    H   " + scaleHeight + "    " + newbm.getHeight());

        if (mHours >= 6 && mHours < 18) {
            //sun
            Colorscslepaint.setARGB(255, 204, 86, 60);
            Colorscslepaint.setTextSize(xx / 20);
            Colorscslepaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("早安", 0, getHeight() / 8, Colorscslepaint);
        } else {
            // /Moon
            Colorscslepaint.setARGB(255, 56, 60, 95);
            Colorscslepaint.setTextSize(xx / 20);
            Colorscslepaint.setTextAlign(Paint.Align.CENTER);

            canvas.drawText("晚安", 0, getHeight() / 8, Colorscslepaint);
        }


        //數字
        Colorscslepaint.setARGB(255, 0, 0, 0);
        Colorscslepaint.setTextSize(xx / 18);
        Colorscslepaint.setAntiAlias(true);
        canvas.rotate(-2);
        for (int i = 1; i < 13; i++) {
            canvas.rotate(360 / 12, 0, 0);
            Colorscslepaint.setStrokeWidth(getWidth() / 120);
//            for (int j = 0; j < 1; j++) {
//
//                canvas.rotate(-30 * i, 0, y + mLargeScaleLength + xx / 12);
//                canvas.drawText("" + i, -xx / 45, y + mLargeScaleLength + xx / 9, Colorscslepaint);
//                canvas.rotate(30 * i, 0, y + mLargeScaleLength + xx / 12);
//
//            }
            //3
            if (i == 3) {
                canvas.drawLine(-3 * xx / 104, y + mLittleScaleLength + xx / 11, 3 * xx / 104, y + mLittleScaleLength + xx / 11, Colorscslepaint);
                canvas.drawLine(-3 * xx / 104, y + mLittleScaleLength + xx / 7, 3 * xx / 104, y + mLittleScaleLength + xx / 7, Colorscslepaint);
                Colorscslepaint.setStrokeWidth(getWidth() / 150);
                canvas.drawLine(-3 * xx / 208, y + mLittleScaleLength + xx / 11, -3 * xx / 208, y + mLittleScaleLength + xx / 7, Colorscslepaint);
                canvas.drawLine(0, y + mLittleScaleLength + xx / 11, 0, y + mLittleScaleLength + xx / 7, Colorscslepaint);
                canvas.drawLine(3 * xx / 208, y + mLittleScaleLength + xx / 11, 3 * xx / 208, y + mLittleScaleLength + xx / 7, Colorscslepaint);
            }
            //6
            if (i == 6) {
                canvas.drawLine(-3 * xx / 100, y + mLittleScaleLength + xx / 11, 3 * xx / 100, y + mLittleScaleLength + xx / 11, Colorscslepaint);
                canvas.drawLine(-3 * xx / 100, y + mLittleScaleLength + xx / 7, 3 * xx / 100, y + mLittleScaleLength + xx / 7, Colorscslepaint);
                Colorscslepaint.setStrokeWidth(getWidth() / 150);
                //l
                canvas.drawLine(3 * xx / 208, y + mLittleScaleLength + xx / 11, 3 * xx / 208, y + mLittleScaleLength + xx / 7, Colorscslepaint);
                //V
                // 右邊
                canvas.drawLine(-3 * xx / 208, y + mLittleScaleLength + xx / 7, 0, y + mLittleScaleLength + xx / 11, Colorscslepaint);
                Colorscslepaint.setStrokeWidth(getWidth() / 180);
                //左邊
                canvas.drawLine(-3 * xx / 116, y + mLittleScaleLength + xx / 11, -3 * xx / 208, y + mLittleScaleLength + xx / 7, Colorscslepaint);

            }

            //9
            if (i == 9) {
                canvas.drawLine(-3 * xx / 100, y + mLittleScaleLength + xx / 11, 3 * xx / 100, y + mLittleScaleLength + xx / 11, Colorscslepaint);
                canvas.drawLine(-3 * xx / 100, y + mLittleScaleLength + xx / 7, 3 * xx / 100, y + mLittleScaleLength + xx / 7, Colorscslepaint);
                //l
                Colorscslepaint.setStrokeWidth(getWidth() / 150);
                canvas.drawLine(-3 * xx / 208, y + mLittleScaleLength + xx / 11, -3 * xx / 208, y + mLittleScaleLength + xx / 7, Colorscslepaint);
                //X
                // 左邊
                canvas.drawLine(0, y + mLittleScaleLength + xx / 11, 3 * xx / 116, y + mLittleScaleLength + xx / 7, Colorscslepaint);
                // 右邊
                Colorscslepaint.setStrokeWidth(getWidth() / 180);
                canvas.drawLine(0, y + mLittleScaleLength + xx / 7, 3 * xx / 116, y + mLittleScaleLength + xx / 11, Colorscslepaint);
            }

            //12
            if (i == 12) {

                canvas.drawLine(-xx / 26, y + mLittleScaleLength + xx / 11, xx / 26, y + mLittleScaleLength + xx / 11, Colorscslepaint);
                canvas.drawLine(-xx / 26, y + mLittleScaleLength + xx / 7, xx / 26, y + mLittleScaleLength + xx / 7, Colorscslepaint);
                //X
                // 左邊
                Colorscslepaint.setStrokeWidth(getWidth() / 180);
                canvas.drawLine(0, y + mLittleScaleLength + xx / 11, -xx / 30, y + mLittleScaleLength + xx / 7, Colorscslepaint);
                // 右邊
                Colorscslepaint.setStrokeWidth(getWidth() / 150);
                canvas.drawLine(-xx / 30, y + mLittleScaleLength + xx / 11, 0, y + mLittleScaleLength + xx / 7, Colorscslepaint);
                //II
                Colorscslepaint.setStrokeWidth(getWidth() / 150);
                canvas.drawLine(xx / 104, y + mLittleScaleLength + xx / 11, xx / 104, y + mLittleScaleLength + xx / 7, Colorscslepaint);
                canvas.drawLine(3 * xx / 104, y + mLittleScaleLength + xx / 11, 3 * xx / 104, y + mLittleScaleLength + xx / 7, Colorscslepaint);

            }
        }
        canvas.rotate(2);

        //到數計時
        // 一个材质,打造出一个线性梯度沿著一条线。
//        Shader mShader = new LinearGradient(0, 0, 100, 0, new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.LTGRAY}, null, Shader.TileMode.REPEAT);
//        Colorscslepaint.setShader(mShader);
//        Colorscslepaint.reset();
        Colorscslepaint.setTextSize(xx / 20);
        canvas.drawText(Min+":"+Sec, xx / 7, 2 * getHeight() / 15, Colorscslepaint);

        //到數變色//圓
        if(Min<5) {
            Colorscslepaint.setARGB(150, 255, 179, 179);
        }else if(Min>5&Min<10){
            Colorscslepaint.setARGB(150,250, 255, 158);
        }else{
            Colorscslepaint.setARGB(150, 190, 255, 184);
        }
        canvas.drawCircle(xx / 7, getHeight() / 8, xx / 11, Colorscslepaint);

       //圓邊
        Colorscslepaint.setStyle(Paint.Style.STROKE);
        //到數變色
        if(Min<5) {
            Colorscslepaint.setARGB(255, 255, 15, 15);
        }else if(Min>5&Min<10){
            Colorscslepaint.setARGB(255,251, 255, 5);
        }else{
            Colorscslepaint.setARGB(255, 0, 255, 21);
        }
//        Colorscslepaint.setStrokeWidth();
        canvas.drawCircle(xx / 7, getHeight() / 8, xx / 11, Colorscslepaint);
        canvas.drawCircle(xx / 7, getHeight() / 8, xx / 13, Colorscslepaint);
        //刻度
//        canvas.drawCircle(xx / 7, getHeight() / 8, xx / 30, Colorscslepaint);
        for (int i = 0; i < 12; i++) {

            canvas.drawLine(18 * xx / 91, getHeight() / 8, 20 * xx / 91, getHeight() / 8, Colorscslepaint);
            canvas.rotate(360 / 12, xx / 7, getHeight() / 8);
        }
        Colorscslepaint.reset();



        //掃描
        if(Min<5) {
            Colorscslepaint.setARGB(255, 255, 15, 15);
        }else if(Min>5&Min<10){
            Colorscslepaint.setARGB(255,230, 107, 255);
        }else{
            Colorscslepaint.setARGB(255, 0, 255, 166);
        }

//        Colorscslepaint.setStyle(Paint.Style.STROKE);
//        Colorscslepaint.setStrokeWidth(xx/8);
        canvas.rotate(36 * mLaser, xx / 7, getHeight() / 8);
        canvas.drawArc(new RectF(Math.abs(xx / 9), Math.abs(getHeight() / 12), Math.abs(xx / 5), Math.abs(getHeight() / 7)), 270, 40, true, Colorscslepaint);
        canvas.rotate(-36 * mLaser, xx / 7, getHeight() / 8);


        // 提醒5分鐘紅色提醒
        if(Min<5) {
            Colorscslepaint.setARGB(210 - (mMiiscomputer % 30) * 7, 255, 0, 0);
            canvas.drawCircle(xx / 7, getHeight() / 8, (mMiiscomputer % 30) * xx / 32, Colorscslepaint);
            Colorscslepaint.reset();
        }


        // Draw hour hand.
        canvas.save();
        canvas.rotate(mHours / 12.0f * 360.0f + mMinutes / 60.0f * 30.0f);

        if (mHours >= 6 && mHours < 18) {
            //sun
            Colorscslepaint.setARGB(255, 125, 57, 8);

        } else {
            // /Moon
            Colorscslepaint.setARGB(255, 2, 148, 161);
        }
        Colorscslepaint.setStrokeWidth(mSecondHandStrokeWidth + xx / 35);
        canvas.drawLine(0, mHourHandTailLength, 0, -mHourHandLength + xx / 10, Colorscslepaint);
        canvas.restore();


        // Draw minute hand.
        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f);
        if (mHours >= 6 && mHours < 18) {
            Colorscslepaint.setARGB(255, 102, 39, 0);
        } else {/// /Moon
            Colorscslepaint.setARGB(255, 42, 208, 206);
        }
        Colorscslepaint.setStrokeWidth(mSecondHandStrokeWidth + xx / 90);
        canvas.drawLine(0, mMinuteHandTailLength, 0, -mMinuteHandLength + xx / 10, Colorscslepaint);
        canvas.restore();

        //用要閃爍圓
        drugmake[0] = 1;
        Colorscslepaint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < 12; i++) {
            if (drugmake[i] == 1) {
                if (mMiiscomputer % 15 == 0) {
                    Colorscslepaint.setARGB(255, 249, 113, 39);
                    canvas.drawCircle(0, y - mLargeScaleLength, Math.abs(mLargeScaleLength / 2 + xx / 60), Colorscslepaint);
                    Colorscslepaint.setARGB(255, 237, 198, 45);
                    canvas.drawCircle(0, y - mLargeScaleLength, Math.abs(mLargeScaleLength / 2), Colorscslepaint);
                }
            }
            canvas.rotate(360 / 12);
        }
        canvas.restore();
//        Colorscslepaint.reset();

        //0.1秒
        mMiiscomputer++;
        if(mLaser>9){mLaser=0;}else{mLaser++;}

//        // Draw second hand.
//        canvas.save();
//        canvas.rotate(mSeconds / 60.0f * 360.0f);
//        canvas.drawLine(0, mSecondHandTailLength, 0, -mSecondHandLength, mSecondHandPaint);
//        canvas.restore();
    }

    public static int Sec=0;
    public static int Min=4;
    public static void time(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sfh = new SimpleDateFormat("ss");
        SimpleDateFormat sfm = new SimpleDateFormat("mm");
        int crruent_Sec = Integer.parseInt(sfh.format(date));
        int crruent_Min = Integer.parseInt(sfm.format(date));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mDialRadius = Math.min(w, h) / 2.0f - 10;
        mDialStrokeWidth = mDialRadius / 20.0f;
        mDialPadding = (int) mDialStrokeWidth;
        mDialPaint.setStrokeWidth(mDialStrokeWidth);

        mLittleScaleLength = mDialStrokeWidth;
        mLittleScaleStrokeWidth = mLittleScaleLength / 4.0f + 1;
        mLittleScalePaint.setStrokeWidth(mLittleScaleStrokeWidth);

        mLargeScaleLength = mLittleScaleLength * 2.5f;
        mLargeScaleStrokeWidth = mLargeScaleLength / 4.0f + 3;
        mLargeScalePaint.setStrokeWidth(mLargeScaleStrokeWidth);

        mHourHandLength = mDialRadius / 2.0f;
        mMinuteHandLength = mDialRadius / 2.0f + mDialRadius / 4.0f;
        mSecondHandLength = mDialRadius / 2.0f + mDialRadius / 3.0f;

        mHourHandTailLength = mHourHandLength / 10.0f;
        mMinuteHandTailLength = mHourHandTailLength;
        mSecondHandTailLength = mSecondHandLength / 6.0f;

        mHourHandStrokeWidth = (int) dp2px(3);
        mHourHandPaint.setStrokeWidth(mHourHandStrokeWidth);
        mMinuteHandStrokeWidth = (int) dp2px(2);
        mMinuteHandPaint.setStrokeWidth(mMinuteHandStrokeWidth);
        mSecondHandStrokeWidth = (int) dp2px(1);
        mSecondHandPaint.setStrokeWidth(mSecondHandStrokeWidth);

        mLinkTextSize = (int) sp2px(mDialStrokeWidth / 2);
        mLinkPaint.setTextSize(mLinkTextSize);

        mDistance = mDialRadius * (int) Math.sqrt(2) - mDialRadius / 2;
        mLinkRectF.set(0, 0, mDistance * 2, mDistance * 2);
        mLinkPath.reset();
        mLinkPath.addArc(mLinkRectF, -180, 180);
        // ------------------------------- //

        float heightSum = mAntHeight + mHeadRadius + mBodyHeight + mFootHeight + 5;
        float proportion = h / 3 / heightSum;

        mHeadRadius *= proportion;
        mHeadBodySpace *= proportion;
        mEyeRadius *= proportion;
        mEyeSpace *= proportion;
        mAntWidth *= proportion;
        mAntHeight *= proportion;
        mAntRoundCorner *= proportion;
        mBodyWidth *= proportion;
        mBodyHeight *= proportion;
        mBodyRoundCorner *= proportion;
        mArmWidth *= proportion;
        mArmHeight *= proportion;
        mArmBodySpace *= proportion;
        mArmRoundCorner *= proportion;
        mFootWidth *= proportion;
        mFootHeight *= proportion;
        mFootSpace *= proportion;
        mFootRoundCorner *= proportion;

        float headLeft = w / 2 - mHeadRadius;
        float headRight = headLeft + mHeadRadius * 2;
        float headTop = mAntHeight + mDialRadius - mHeadRadius * 2;
        float headBottom = headTop + mHeadRadius * 2;
        mHeadRectF.set(headLeft, headTop, headRight, headBottom);

        float leftEyeX = w / 2 - mEyeSpace / 2;
        float leftEyeY = headTop + mHeadRadius / 2;
        mLeftEyePointF.set(leftEyeX, leftEyeY);

        float rightEyeX = leftEyeX + mEyeSpace;
        float rightEyeY = leftEyeY;
        mRightEyePointF.set(rightEyeX, rightEyeY);

        float leftAntLeft = w / 2 - mAntWidth / 2;
        float leftAntRight = leftAntLeft + mAntWidth;
        float leftAntTop = headTop - mAntHeight;
        float leftAntBottom = leftAntTop + mAntHeight;
        mLeftAntRectF.set(leftAntLeft, leftAntTop, leftAntRight, leftAntBottom);

        float rightAntLeft = leftAntLeft;
        float rightAntRight = rightAntLeft + mAntWidth;
        float rightAntTop = headTop - mAntHeight;
        float rightAntBottom = rightAntTop + mAntHeight;
        mRightAntRectF.set(rightAntLeft, rightAntTop, rightAntRight, rightAntBottom);

        float bodyLeft = (w - mBodyWidth) / 2;
        float bodyRight = bodyLeft + mBodyWidth;
        float bodyTop = headTop + mHeadRadius + mHeadBodySpace;
        float bodyBottom = bodyTop + mBodyHeight;
        mBodyRectF.set(bodyLeft, bodyTop, bodyRight, bodyBottom);

        float leftArmLeft = bodyLeft - mArmBodySpace - mArmWidth;
        float leftArmRight = leftArmLeft + mArmWidth;
        float leftArmTop = bodyTop;
        float leftArmBottom = leftArmTop + mArmHeight;
        mLeftArmRectF.set(leftArmLeft, leftArmTop, leftArmRight, leftArmBottom);

        float rightArmLeft = bodyRight + mArmBodySpace;
        float rightArmRight = rightArmLeft + mArmWidth;
        float rightArmTop = bodyTop;
        float rightArmBottom = rightArmTop + mArmHeight;
        mRightArmRectF.set(rightArmLeft, rightArmTop, rightArmRight, rightArmBottom);

        float leftFootLeft = w / 2 - mFootSpace / 2 - mFootWidth;
        float leftFootRight = leftFootLeft + mFootWidth;
        float leftFootTop = bodyBottom - 5;
        float leftFootBottom = leftFootTop + mFootHeight;
        mLeftFootRectF.set(leftFootLeft, leftFootTop, leftFootRight, leftFootBottom);

        float rightFootLeft = leftFootRight + mFootSpace;
        float rightFootRight = rightFootLeft + mFootWidth;
        float rightFootTop = bodyBottom - 5;
        float rightFootBottom = rightFootTop + mFootHeight;
        mRightFootRectF.set(rightFootLeft, rightFootTop, rightFootRight, rightFootBottom);
    }

    private void onTimeChanged() {
        mCalendar.setToNow();

        mHours = mCalendar.hour;
        mMinutes = mCalendar.minute;
        mSeconds = mCalendar.second;


        if (mSeconds > mMiis) {
            mMiis = mSeconds;
            mMiiscomputer = mMiis * 10;
        } else if (mSeconds == 0 && mMiis != 0) {
            mMiis = 0;
            mMiiscomputer = 0;
        }
    }

    /**
     * Broadcast receiver.
     */
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIME_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
            }

            onTimeChanged();
        }
    };

    private float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("ccccccc", "dddd");
        Log.i("ccccccc", "" + event.getX() + "    " + event.getY());


        return true;
    }

//    @Override
//    public void onClick(View v) {
//                Log.i("ccccccc","dddd");
////       Log.i("ccccccc",""+event.getX()+"    "+event.getY());
//    }

}