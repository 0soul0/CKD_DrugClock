package com.example.gtc_5a0a.clock;

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
import android.widget.TextView;

import java.util.TimeZone;

/**
 * Analog clock purely use Canvas API.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-1-26 19:58:54
 */
public class CanvasClockNew extends View {
    /**
     * Welcome to my personal blog.
     */
    public static final String BLOG_2DXGUJUN = "http://2dxgujun.com";

    private TextView textView;
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

    private Paint mSecondCircle;
    private Paint mMinuteCircle;
    private Paint mHourCircle;

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
    //--------------------------------------------------------
    private Handler handler_scemd;
    private Handler handler_minute;
    private Handler handler_hour;

    int k = 0, a = 0, v = 0;
    private Runnable mTickRunnable = new Runnable() {
        @Override
        public void run() {
//            Log.i("LOG", "Runnable Tick");
            onTimeChanged();
            invalidate();
            if (mAttached) {
                mTickHandler.postDelayed(mTickRunnable, 1000);
            }
        }
    };

    private Handler mTickHandler;

    public CanvasClockNew(Context context) {
        this(context, null);
    }

    public CanvasClockNew(Context context, AttributeSet attrs) {
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

        //-------------------------------------------------------------------------- 自定
        mSecondCircle = new Paint(mLittleScalePaint);
        mSecondCircle.setStrokeWidth(mLargeScaleStrokeWidth + 20);
        mSecondCircle.setARGB(195, 0, 240, 255);

        mMinuteCircle = new Paint(mLittleScalePaint);
        mMinuteCircle.setStrokeWidth(mLargeScaleStrokeWidth + 18);
        mMinuteCircle.setARGB(195, 0, 255, 240);

        mHourCircle = new Paint(mLittleScalePaint);
        mHourCircle.setStrokeWidth(mLargeScaleStrokeWidth + 16);
        mHourCircle.setARGB(195, 255, 0, 240);
        //-------------------------------------------------------------------------- 自定


        mHourHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourHandPaint.setStyle(Paint.Style.STROKE);
        mHourHandPaint.setStrokeWidth(mHourHandStrokeWidth);
        mHourHandPaint.setStrokeCap(Paint.Cap.ROUND);

        mMinuteHandPaint = new Paint(mHourHandPaint);
        mMinuteHandPaint.setStrokeWidth(mMinuteHandStrokeWidth);

        mSecondHandPaint = new Paint(mMinuteHandPaint);
        mSecondHandPaint.setStrokeWidth(mSecondHandStrokeWidth);
        mSecondHandPaint.setColor(Color.RED);
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
        Log.i("canve", "" + getWidth() / 2 + "    " + getHeight() / 2);
/*
        // Draw my blog link.
        canvas.save();
        canvas.translate(-mDistance, -mDistance);
        canvas.drawTextOnPath(BLOG_2DXGUJUN, mLinkPath, 0, 0, mLinkPaint);
        canvas.restore();
*/


        //太陽
        Paint sun = new Paint();
        sun.setARGB(150, 255, 235, 0);
        sun.setAntiAlias(false);
        canvas.save();
//       for(int k=0;k<24;k++) {
        canvas.rotate(15 * mHours, 0, getHeight() / 2);
//           Log.i("canves", "" + k + "    " + k * 15);
        int x = 0;
        int yy = getHeight() / 3 + getHeight() / 2;
        //圓
        canvas.drawCircle(x, yy, 45, sun);
        RectF ovabig = new RectF(-60 + x, -180 + yy, 60 + x, -48 + yy);
        RectF ovasmall = new RectF(-60 + x, -140 + yy, 60 + x, -48 + yy);
        //橢圓
        sun.setARGB(155, 255, 255, 0);
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                sun.setARGB(150, 255, 157, 0);
                canvas.drawArc(ovabig, 80, 25, true, sun);
            } else {
                sun.setARGB(150, 255, 94, 0);
                canvas.drawArc(ovasmall, 80, 20, true, sun);
            }
            canvas.rotate(360 / 8, x, yy);
        }


        //月亮
        int ymoon = -yy + getHeight();
        sun.setARGB(150, 146, 0, 255);
        canvas.drawCircle(x, ymoon, 45, sun);
        sun.setARGB(220, 255, 255, 255);
        canvas.drawOval(new RectF(x - getWidth() / 16, ymoon - getWidth() / 17, x + getWidth() / 35, ymoon + getWidth() / 17), sun);
//       }

        canvas.restore();
        //
        //-------------------------------------------------------------------
        Paint paint = new Paint();
        float y = -mDialRadius + mDialPadding;
        if (mHours > 6 && mHours <= 11) {
            paint.setARGB(183 + mHours, 0, 225 - 20 * mHours, 255);
        } else if (mHours > 11 && mHours <= 13) {
            paint.setARGB(183 + mHours, 255 - 19 * mHours, 255, 0);
        } else if (mHours > 13 && mHours <= 18) {
            paint.setARGB(183 + mHours, 0, 225 - 12 * mHours, 255);
        } else {
            paint.setARGB(183 + mHours, 0 + 9 * mHours, 0, 255);
        }

        if (mHours > 12) {
            mHours = mHours - 12;
        }

        //背景
//        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
//        canvas.drawPaint(paint);
        paint.setTextSize(getWidth() / 6);

        //TEXT顏色

        //TEXT+零
        if (mHours > 9) {
            if (mMinutes > 9) {
                canvas.drawText(mHours + ":" + mMinutes, 0 - getWidth() / 5, 0 + getHeight() / 25, paint);
            } else {
                canvas.drawText(mHours + ":0" + mMinutes, 0 - getWidth() / 5, 0 + getHeight() / 25, paint);
            }
        } else {
            if (mMinutes > 9) {
                canvas.drawText("0" + mHours + ":" + mMinutes, 0 - getWidth() / 5, 0 + getHeight() / 25, paint);
            } else {
                canvas.drawText("0" + mHours + ":0" + mMinutes, 0 - getWidth() / 5, 0 + getHeight() / 25, paint);
            }
        }


        // Draw hour hand.

        for (int i = 0; i < mHours * 5; i++) {
            canvas.drawLine(0, y * 8 / 10, 0, y * 8 / 10 + mLargeScaleLength * 4 / 6, mHourCircle);
            mHourCircle.setARGB(195 + i, 255, 0, 240 - 4 * i);
            canvas.rotate(6);
        }
        //歸零
        canvas.rotate(-6 * mHours * 5);
        // Draw minute hand.
        for (int i = 0; i < mMinutes; i++) {
            canvas.drawLine(0, y * 9 / 10, 0, y * 9 / 10 + mLargeScaleLength * 4 / 6, mMinuteCircle);
            mMinuteCircle.setARGB(195 + i, 0, 255, 240 - 4 * i);
            canvas.rotate(6);
        }


        //歸零
        canvas.rotate(-6 * mMinutes);
        // Draw second hand.
        for (int i = 0; i < mSeconds; i++) {
            canvas.drawLine(0, y, 0, y + mLargeScaleLength * 4 / 6, mSecondCircle);
            mSecondCircle.setARGB(195 + i, 0, 240 - 4 * i, 255);
            canvas.rotate(6);
        }


//        // Draw hour hand.
//        canvas.save();
//        canvas.rotate(mHours / 12.0f * 360.0f + mMinutes / 60.0f * 30.0f);
//        canvas.drawLine(0, mHourHandTailLength, 0, -mHourHandLength, mHourHandPaint);
//        canvas.restore();

//        // Draw minute hand.
//        canvas.save();
//        canvas.rotate(mMinutes / 60.0f * 360.0f);
//        canvas.drawLine(0, mMinuteHandTailLength, 0, -mMinuteHandLength, mMinuteHandPaint);
//        canvas.restore();
//
//        // Draw second hand.
//        canvas.save();
//        canvas.rotate(mSeconds / 60.0f * 360.0f);
////        Log.i("canve", mSeconds+"    "+mSeconds / 60.0f * 360.0f);
//        canvas.drawLine(0, mSecondHandTailLength, 0, -mSecondHandLength, mSecondHandPaint);
//        canvas.restore();


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
        Log.i("ccccccc", "" + mCalendar);
        mHours = mCalendar.hour;
        mMinutes = mCalendar.minute;
        mSeconds = mCalendar.second;

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
}

