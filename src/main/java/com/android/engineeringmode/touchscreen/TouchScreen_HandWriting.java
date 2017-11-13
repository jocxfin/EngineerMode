package com.android.engineeringmode.touchscreen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.android.engineeringmode.functions.Light;

import java.util.ArrayList;
import java.util.Calendar;

public class TouchScreen_HandWriting extends Activity {
    private String TAG = "EM-TouchScreen";
    private int mZoom = 1;
    int screenHeight;
    int screenWidth;

    public class MyView extends View {
        private boolean mCurDown;
        private float mCurPressure;
        private int mCurWidth;
        private int mCurX;
        private int mCurY;
        private int mHeaderBottom;
        private final Paint mPaint;
        private final Paint mTargetPaint;
        private final Paint mTextBackgroundPaint;
        private final Paint mTextLevelPaint;
        private final FontMetricsInt mTextMetrics = new FontMetricsInt();
        private final Paint mTextPaint;
        private VelocityTracker mVelocity;
        private final ArrayList<Float> mXs = new ArrayList();
        private final ArrayList<Float> mYs = new ArrayList();

        public MyView(Context c) {
            super(c);
            DisplayMetrics dm = new DisplayMetrics();
            dm = TouchScreen_HandWriting.this.getApplicationContext().getResources().getDisplayMetrics();
            TouchScreen_HandWriting.this.screenWidth = dm.widthPixels;
            TouchScreen_HandWriting.this.screenHeight = dm.heightPixels;
            if (!(480 == TouchScreen_HandWriting.this.screenWidth && 800 == TouchScreen_HandWriting.this.screenHeight)) {
                if (800 == TouchScreen_HandWriting.this.screenWidth && 480 == TouchScreen_HandWriting.this.screenHeight) {
                }
                if (!(1080 == TouchScreen_HandWriting.this.screenWidth && 1920 == TouchScreen_HandWriting.this.screenHeight)) {
                    if (1920 == TouchScreen_HandWriting.this.screenWidth && 1080 == TouchScreen_HandWriting.this.screenHeight) {
                    }
                    this.mTextPaint = new Paint();
                    this.mTextPaint.setAntiAlias(true);
                    this.mTextPaint.setTextSize((float) (TouchScreen_HandWriting.this.mZoom * 10));
                    this.mTextPaint.setARGB(Light.MAIN_KEY_MAX, 0, 0, 0);
                    this.mTextBackgroundPaint = new Paint();
                    this.mTextBackgroundPaint.setAntiAlias(false);
                    this.mTextBackgroundPaint.setARGB(128, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
                    this.mTextLevelPaint = new Paint();
                    this.mTextLevelPaint.setAntiAlias(false);
                    this.mTextLevelPaint.setARGB(192, Light.MAIN_KEY_MAX, 0, 0);
                    this.mPaint = new Paint();
                    this.mPaint.setAntiAlias(true);
                    this.mPaint.setARGB(Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
                    this.mPaint.setStyle(Style.STROKE);
                    this.mPaint.setStrokeWidth(2.0f);
                    this.mTargetPaint = new Paint();
                    this.mTargetPaint.setAntiAlias(false);
                    this.mTargetPaint.setARGB(192, 0, 0, Light.MAIN_KEY_MAX);
                    this.mPaint.setStyle(Style.STROKE);
                    this.mPaint.setStrokeWidth(1.0f);
                }
                TouchScreen_HandWriting.this.mZoom = 2;
                this.mTextPaint = new Paint();
                this.mTextPaint.setAntiAlias(true);
                this.mTextPaint.setTextSize((float) (TouchScreen_HandWriting.this.mZoom * 10));
                this.mTextPaint.setARGB(Light.MAIN_KEY_MAX, 0, 0, 0);
                this.mTextBackgroundPaint = new Paint();
                this.mTextBackgroundPaint.setAntiAlias(false);
                this.mTextBackgroundPaint.setARGB(128, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
                this.mTextLevelPaint = new Paint();
                this.mTextLevelPaint.setAntiAlias(false);
                this.mTextLevelPaint.setARGB(192, Light.MAIN_KEY_MAX, 0, 0);
                this.mPaint = new Paint();
                this.mPaint.setAntiAlias(true);
                this.mPaint.setARGB(Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
                this.mPaint.setStyle(Style.STROKE);
                this.mPaint.setStrokeWidth(2.0f);
                this.mTargetPaint = new Paint();
                this.mTargetPaint.setAntiAlias(false);
                this.mTargetPaint.setARGB(192, 0, 0, Light.MAIN_KEY_MAX);
                this.mPaint.setStyle(Style.STROKE);
                this.mPaint.setStrokeWidth(1.0f);
            }
            TouchScreen_HandWriting.this.mZoom = 2;
            TouchScreen_HandWriting.this.mZoom = 2;
            this.mTextPaint = new Paint();
            this.mTextPaint.setAntiAlias(true);
            this.mTextPaint.setTextSize((float) (TouchScreen_HandWriting.this.mZoom * 10));
            this.mTextPaint.setARGB(Light.MAIN_KEY_MAX, 0, 0, 0);
            this.mTextBackgroundPaint = new Paint();
            this.mTextBackgroundPaint.setAntiAlias(false);
            this.mTextBackgroundPaint.setARGB(128, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
            this.mTextLevelPaint = new Paint();
            this.mTextLevelPaint.setAntiAlias(false);
            this.mTextLevelPaint.setARGB(192, Light.MAIN_KEY_MAX, 0, 0);
            this.mPaint = new Paint();
            this.mPaint.setAntiAlias(true);
            this.mPaint.setARGB(Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
            this.mPaint.setStyle(Style.STROKE);
            this.mPaint.setStrokeWidth(2.0f);
            this.mTargetPaint = new Paint();
            this.mTargetPaint.setAntiAlias(false);
            this.mTargetPaint.setARGB(192, 0, 0, Light.MAIN_KEY_MAX);
            this.mPaint.setStyle(Style.STROKE);
            this.mPaint.setStrokeWidth(1.0f);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            this.mTextPaint.getFontMetricsInt(this.mTextMetrics);
            this.mHeaderBottom = ((-this.mTextMetrics.ascent) + this.mTextMetrics.descent) + 2;
            Log.i(TouchScreen_HandWriting.this.TAG, "Metrics: ascent=" + this.mTextMetrics.ascent + " descent=" + this.mTextMetrics.descent + " leading=" + this.mTextMetrics.leading + " top=" + this.mTextMetrics.top + " bottom=" + this.mTextMetrics.bottom);
        }

        protected void onDraw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setColor(-1);
            paint.setStrokeWidth(1.0f);
            paint.setAntiAlias(true);
            canvas.drawLine(0.0f, (float) (TouchScreen_HandWriting.this.screenHeight / 4), (float) (TouchScreen_HandWriting.this.screenWidth / 4), 0.0f, paint);
            canvas.drawLine(0.0f, (float) ((TouchScreen_HandWriting.this.screenHeight / 4) * 2), (float) ((TouchScreen_HandWriting.this.screenWidth / 4) * 2), 0.0f, paint);
            canvas.drawLine(0.0f, (float) ((TouchScreen_HandWriting.this.screenHeight / 4) * 3), (float) ((TouchScreen_HandWriting.this.screenWidth / 4) * 3), 0.0f, paint);
            canvas.drawLine(0.0f, (float) TouchScreen_HandWriting.this.screenHeight, (float) TouchScreen_HandWriting.this.screenWidth, 0.0f, paint);
            canvas.drawLine((float) (TouchScreen_HandWriting.this.screenWidth / 4), (float) TouchScreen_HandWriting.this.screenHeight, (float) TouchScreen_HandWriting.this.screenWidth, (float) (TouchScreen_HandWriting.this.screenHeight / 4), paint);
            canvas.drawLine((float) ((TouchScreen_HandWriting.this.screenWidth / 4) * 2), (float) TouchScreen_HandWriting.this.screenHeight, (float) TouchScreen_HandWriting.this.screenWidth, (float) ((TouchScreen_HandWriting.this.screenHeight / 4) * 2), paint);
            canvas.drawLine((float) ((TouchScreen_HandWriting.this.screenWidth / 4) * 3), (float) TouchScreen_HandWriting.this.screenHeight, (float) TouchScreen_HandWriting.this.screenWidth, (float) ((TouchScreen_HandWriting.this.screenHeight / 4) * 3), paint);
            int w = getWidth() / 5;
            int base = (-this.mTextMetrics.ascent) + 1;
            int bottom = this.mHeaderBottom;
            canvas.drawRect(0.0f, 0.0f, (float) (w - 1), (float) bottom, this.mTextBackgroundPaint);
            canvas.drawText("X: " + this.mCurX, 1.0f, (float) base, this.mTextPaint);
            canvas.drawRect((float) w, 0.0f, (float) ((w * 2) - 1), (float) bottom, this.mTextBackgroundPaint);
            canvas.drawText("Y: " + this.mCurY, (float) (w + 1), (float) base, this.mTextPaint);
            canvas.drawRect((float) (w * 2), 0.0f, (float) ((w * 3) - 1), (float) bottom, this.mTextBackgroundPaint);
            canvas.drawRect((float) (w * 2), 0.0f, (((float) (w * 2)) + (this.mCurPressure * ((float) w))) - 1.0f, (float) bottom, this.mTextLevelPaint);
            canvas.drawText("Pres: " + this.mCurPressure, (float) ((w * 2) + 1), (float) base, this.mTextPaint);
            canvas.drawRect((float) (w * 3), 0.0f, (float) ((w * 4) - 1), (float) bottom, this.mTextBackgroundPaint);
            canvas.drawText("XVel: " + (this.mVelocity == null ? 0 : (int) (Math.abs(this.mVelocity.getXVelocity()) * 1000.0f)), (float) ((w * 3) + 1), (float) base, this.mTextPaint);
            canvas.drawRect((float) (w * 4), 0.0f, (float) getWidth(), (float) bottom, this.mTextBackgroundPaint);
            canvas.drawText("YVel: " + (this.mVelocity == null ? 0 : (int) (Math.abs(this.mVelocity.getYVelocity()) * 1000.0f)), (float) ((w * 4) + 1), (float) base, this.mTextPaint);
            int N = this.mXs.size();
            float lastX = 0.0f;
            float lastY = 0.0f;
            this.mPaint.setARGB(Light.MAIN_KEY_MAX, 0, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
            for (int i = 0; i < N; i++) {
                float x = ((Float) this.mXs.get(i)).floatValue();
                float y = ((Float) this.mYs.get(i)).floatValue();
                if (!(i <= 0 || Float.compare(lastX, -100.0f) == 0 || Float.compare(x, -100.0f) == 0)) {
                    canvas.drawLine(lastX, lastY, x, y, this.mTargetPaint);
                    canvas.drawPoint(lastX, lastY, this.mPaint);
                }
                lastX = x;
                lastY = y;
            }
            if (this.mVelocity != null) {
                this.mPaint.setARGB(Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, 0, 0);
                canvas.drawLine(lastX, lastY, lastX + (this.mVelocity.getXVelocity() * 16.0f), lastY + (this.mVelocity.getYVelocity() * 16.0f), this.mPaint);
            } else {
                canvas.drawPoint(lastX, lastY, this.mPaint);
            }
            if (this.mCurDown) {
                canvas.drawLine(0.0f, (float) this.mCurY, (float) getWidth(), (float) this.mCurY, this.mTargetPaint);
                canvas.drawLine((float) this.mCurX, 0.0f, (float) this.mCurX, (float) getHeight(), this.mTargetPaint);
                int pressureLevel = (int) (this.mCurPressure * 255.0f);
                this.mPaint.setARGB(Light.MAIN_KEY_MAX, pressureLevel, 128, 255 - pressureLevel);
                canvas.drawPoint((float) this.mCurX, (float) this.mCurY, this.mPaint);
                canvas.drawCircle((float) this.mCurX, (float) this.mCurY, (float) this.mCurWidth, this.mPaint);
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            boolean z = false;
            int action = event.getAction();
            if (action == 0) {
                this.mVelocity = VelocityTracker.obtain();
            }
            this.mVelocity.addMovement(event);
            this.mVelocity.computeCurrentVelocity(1);
            int N = event.getHistorySize();
            for (int i = 0; i < N; i++) {
                this.mXs.add(Float.valueOf(event.getHistoricalX(i)));
                this.mYs.add(Float.valueOf(event.getHistoricalY(i)));
            }
            this.mXs.add(Float.valueOf(event.getX()));
            this.mYs.add(Float.valueOf(event.getY()));
            if (action == 1) {
                this.mXs.add(Float.valueOf(-100.0f));
                this.mYs.add(Float.valueOf(-100.0f));
            }
            if (action == 0) {
                z = true;
            } else if (action == 2) {
                z = true;
            }
            this.mCurDown = z;
            this.mCurX = (int) event.getX();
            this.mCurY = (int) event.getY();
            this.mCurPressure = event.getPressure();
            this.mCurPressure = ((float) ((int) (this.mCurPressure * 100.0f))) / 100.0f;
            this.mCurWidth = (int) (event.getSize() * ((float) (getWidth() / 3)));
            invalidate();
            return true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(new MyView(this));
    }

    public void onStop() {
        Calendar now = Calendar.getInstance();
        String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--TouchScreen_HandWriting--" + "has entered it";
        super.onStop();
    }
}
