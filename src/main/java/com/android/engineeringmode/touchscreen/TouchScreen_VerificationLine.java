package com.android.engineeringmode.touchscreen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.android.engineeringmode.functions.Light;

import java.util.Vector;

public class TouchScreen_VerificationLine extends Activity implements OnTouchListener {
    public double mDiversity = 0.0d;
    public Vector<Point> mInput = new Vector();
    public int mLineIndex = 0;
    public Vector<Point> mPts1 = null;
    private int mRectHeight;
    private int mRectWidth;
    public boolean mRun = false;
    private int mZoom = 5;
    public MyView myView;

    class MyView extends View {
        private Paint mLinePaint;
        private Rect mRect;
        private Paint mRectPaint;
        private Paint mTextPaint;

        public MyView(Context context) {
            super(context);
            this.mLinePaint = null;
            this.mTextPaint = null;
            this.mRectPaint = null;
            this.mRect = null;
            this.mLinePaint = new Paint();
            this.mLinePaint.setAntiAlias(true);
            this.mTextPaint = new Paint();
            this.mTextPaint.setAntiAlias(true);
            this.mTextPaint.setTextSize(((float) TouchScreen_VerificationLine.this.mZoom) * 9.0f);
            this.mTextPaint.setARGB(Light.MAIN_KEY_MAX, 0, 0, 0);
            this.mRect = new Rect(0, 0, TouchScreen_VerificationLine.this.mRectWidth, TouchScreen_VerificationLine.this.mRectHeight);
            this.mRectPaint = new Paint();
            this.mRectPaint.setARGB(Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawRect(this.mRect, this.mRectPaint);
            this.mLinePaint.setARGB(Light.MAIN_KEY_MAX, 0, 0, Light.MAIN_KEY_MAX);
            Point p1 = (Point) TouchScreen_VerificationLine.this.mPts1.get(0);
            Point p2 = (Point) TouchScreen_VerificationLine.this.mPts1.get(TouchScreen_VerificationLine.this.mPts1.size() - 1);
            canvas.drawLine((float) p1.x, (float) p1.y, (float) p2.x, (float) p2.y, this.mLinePaint);
            this.mLinePaint.setARGB(Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, 0, 0);
            for (int i = 0; i < TouchScreen_VerificationLine.this.mInput.size() - 1; i++) {
                p1 = (Point) TouchScreen_VerificationLine.this.mInput.get(i);
                p2 = (Point) TouchScreen_VerificationLine.this.mInput.get(i + 1);
                canvas.drawLine((float) p1.x, (float) p1.y, (float) p2.x, (float) p2.y, this.mLinePaint);
            }
            canvas.drawText("Diversity : " + Double.toString(TouchScreen_VerificationLine.this.mDiversity), (float) (TouchScreen_VerificationLine.this.mZoom * 20), (float) (TouchScreen_VerificationLine.this.mRectHeight - (TouchScreen_VerificationLine.this.mZoom * 10)), this.mTextPaint);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        dm = getApplicationContext().getResources().getDisplayMetrics();
        this.mRectWidth = dm.widthPixels;
        this.mRectHeight = dm.heightPixels;
        if (!(480 == this.mRectWidth && 800 == this.mRectHeight)) {
            if (800 == this.mRectWidth && 480 == this.mRectHeight) {
            }
            if (1920 > this.mRectWidth || 1080 > this.mRectHeight) {
                if (1080 <= this.mRectWidth && 1920 <= this.mRectHeight) {
                }
                this.mPts1 = readPoints(0);
                this.mLineIndex++;
                this.myView = new MyView(this);
                setContentView(this.myView);
                this.myView.setOnTouchListener(this);
                Log.i("TVL", "Oncreate");
            }
            this.mZoom = 5;
            this.mPts1 = readPoints(0);
            this.mLineIndex++;
            this.myView = new MyView(this);
            setContentView(this.myView);
            this.myView.setOnTouchListener(this);
            Log.i("TVL", "Oncreate");
        }
        this.mZoom = 2;
        this.mZoom = 5;
        this.mPts1 = readPoints(0);
        this.mLineIndex++;
        this.myView = new MyView(this);
        setContentView(this.myView);
        this.myView.setOnTouchListener(this);
        Log.i("TVL", "Oncreate");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 25) {
            CalculateDiversity();
        } else if (keyCode == 24) {
            this.mInput.clear();
            this.mPts1 = readPoints(this.mLineIndex);
            this.mLineIndex++;
            if (4 == this.mLineIndex) {
                this.mLineIndex = 0;
            }
            this.mDiversity = 0.0d;
            this.myView.invalidate();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onResume() {
        super.onResume();
        Log.i("TVL", "onResume");
    }

    public void onPause() {
        Log.i("TVL", "onPause");
        super.onPause();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Calculate");
        menu.add(0, 2, 1, "NextLine");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem aMenuItem) {
        switch (aMenuItem.getItemId()) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                CalculateDiversity();
                break;
            case Light.CHARGE_RED_LIGHT /*2*/:
                this.mInput.clear();
                this.mPts1 = readPoints(this.mLineIndex);
                this.mLineIndex++;
                if (4 == this.mLineIndex) {
                    this.mLineIndex = 0;
                }
                this.mDiversity = 0.0d;
                this.myView.invalidate();
                break;
        }
        return super.onOptionsItemSelected(aMenuItem);
    }

    public void CalculateDiversity() {
        Point cp = new Point(0, 0);
        if (this.mInput.size() != 0) {
            double error = 0.0d;
            float ratio = ((float) this.mRectHeight) / ((float) this.mRectWidth);
            int i;
            switch (this.mLineIndex - 1) {
                case -1:
                    for (i = 0; i < this.mInput.size(); i++) {
                        cp = (Point) this.mInput.get(i);
                        error += ((double) Math.abs(((((float) cp.x) * ratio) + ((float) cp.y)) - ((float) this.mRectHeight))) / Math.sqrt((double) ((ratio * ratio) + 1.0f));
                    }
                    break;
                case 0:
                    for (i = 0; i < this.mInput.size(); i++) {
                        error += (double) Math.abs(((Point) this.mInput.get(i)).x - (this.mRectWidth / 2));
                    }
                    break;
                case Light.MAIN_KEY_LIGHT /*1*/:
                    for (i = 0; i < this.mInput.size(); i++) {
                        error += (double) Math.abs(((Point) this.mInput.get(i)).y - (this.mRectHeight / 2));
                    }
                    break;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    for (i = 0; i < this.mInput.size(); i++) {
                        cp = (Point) this.mInput.get(i);
                        error += ((double) Math.abs((((float) cp.x) * ratio) - ((float) cp.y))) / Math.sqrt((double) ((ratio * ratio) + 1.0f));
                    }
                    break;
            }
            this.mDiversity = error / ((double) this.mInput.size());
            this.myView.invalidate();
        }
    }

    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == 0 || 2 == e.getAction()) {
            this.mInput.add(new Point((int) e.getX(), (int) e.getY()));
            this.myView.invalidate();
        }
        return true;
    }

    public Vector<Point> readPoints(int LineIndex) {
        Vector<Point> v = new Vector();
        float ratio = ((float) this.mRectHeight) / ((float) this.mRectWidth);
        int i;
        switch (this.mLineIndex) {
            case 0:
                for (i = 0; i < this.mRectHeight; i++) {
                    v.add(new Point(this.mRectWidth / 2, i));
                }
                break;
            case Light.MAIN_KEY_LIGHT /*1*/:
                for (i = 0; i < this.mRectWidth; i++) {
                    v.add(new Point(i, this.mRectHeight / 2));
                }
                break;
            case Light.CHARGE_RED_LIGHT /*2*/:
                for (i = 0; i < this.mRectWidth; i++) {
                    v.add(new Point(i, (int) (((float) i) * ratio)));
                }
                break;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                for (i = 0; i < this.mRectWidth; i++) {
                    v.add(new Point(this.mRectWidth - i, (int) (((float) i) * ratio)));
                }
                break;
        }
        return v;
    }
}
