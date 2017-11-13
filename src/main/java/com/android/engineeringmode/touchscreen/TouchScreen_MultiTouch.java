package com.android.engineeringmode.touchscreen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.android.engineeringmode.functions.Light;

import java.util.Vector;

public class TouchScreen_MultiTouch extends Activity {

    public class MyView extends View {
        private int mCurX;
        private int mCurY;
        public Vector<Point> mInputId0 = new Vector();
        public Vector<Point> mInputId1 = new Vector();
        public Vector<Point> mInputId2 = new Vector();
        private final Paint mPaint = new Paint();
        private final Paint mTargetPaint;

        public MyView(Context c) {
            super(c);
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

        protected void onDraw(Canvas canvas) {
            int lastX;
            int lastY;
            int i;
            int N0 = this.mInputId0.size();
            if (N0 > 1) {
                lastX = ((Point) this.mInputId0.get(0)).x;
                lastY = ((Point) this.mInputId0.get(0)).y;
                this.mPaint.setARGB(Light.MAIN_KEY_MAX, 0, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
                for (i = 1; i < N0; i++) {
                    int x = ((Point) this.mInputId0.get(i)).x;
                    int y = ((Point) this.mInputId0.get(i)).y;
                    canvas.drawLine((float) lastX, (float) lastY, (float) x, (float) y, this.mTargetPaint);
                    canvas.drawPoint((float) lastX, (float) lastY, this.mPaint);
                    lastX = x;
                    lastY = y;
                }
            }
            int N1 = this.mInputId1.size();
            if (N1 > 1) {
                lastX = ((Point) this.mInputId1.get(0)).x;
                lastY = ((Point) this.mInputId1.get(0)).y;
                for (i = 0; i < N1; i++) {
                    x = ((Point) this.mInputId1.get(i)).x;
                    y = ((Point) this.mInputId1.get(i)).y;
                    canvas.drawLine((float) lastX, (float) lastY, (float) x, (float) y, this.mTargetPaint);
                    canvas.drawPoint((float) lastX, (float) lastY, this.mPaint);
                    lastX = x;
                    lastY = y;
                }
            }
            int N2 = this.mInputId2.size();
            if (N2 > 1) {
                lastX = ((Point) this.mInputId1.get(0)).x;
                lastY = ((Point) this.mInputId1.get(0)).y;
                for (i = 0; i < N2; i++) {
                    x = ((Point) this.mInputId2.get(i)).x;
                    y = ((Point) this.mInputId2.get(i)).y;
                    canvas.drawLine((float) lastX, (float) lastY, (float) x, (float) y, this.mTargetPaint);
                    canvas.drawPoint((float) lastX, (float) lastY, this.mPaint);
                    lastX = x;
                    lastY = y;
                }
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == 0) {
                this.mInputId0.clear();
                this.mInputId0.clear();
            }
            for (int i = 0; i < event.getPointerCount(); i++) {
                this.mCurX = (int) event.getX(i);
                this.mCurY = (int) event.getY(i);
                switch (i) {
                    case 0:
                        this.mInputId0.add(new Point(this.mCurX, this.mCurY));
                        break;
                    case Light.MAIN_KEY_LIGHT /*1*/:
                        this.mInputId1.add(new Point(this.mCurX, this.mCurY));
                        break;
                    case Light.CHARGE_RED_LIGHT /*2*/:
                        this.mInputId2.add(new Point(this.mCurX, this.mCurY));
                        break;
                    default:
                        break;
                }
            }
            invalidate();
            return true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }
}
