package com.android.engineeringmode.touchscreen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import com.android.engineeringmode.functions.Light;

import java.util.Random;

public class TouchScreen_VerificationPoint extends Activity implements OnTouchListener {
    public Point PrePoint;
    private String TAG = "EM-TouchScreen";
    public Bitmap mBitmap;
    public int mBitmapPad = 0;
    public DiversityCanvas mDiversityCanvas;
    public double mPointError = 0.0d;
    private int mRectHeight;
    private int mRectWidth;
    public boolean mRun = false;
    private int mZoom = 5;
    public Random rand;

    class DiversityCanvas extends SurfaceView implements Callback {
        DiversityThread mThread = null;

        class DiversityThread extends Thread {
            private Paint mCrossPaint = null;
            private Rect mRect = null;
            private Paint mRectPaint = null;
            private SurfaceHolder mSurfaceHolder = null;
            private Paint mTextPaint = null;

            public DiversityThread(SurfaceHolder s, Context c) {
                this.mSurfaceHolder = s;
                this.mTextPaint = new Paint();
                this.mTextPaint.setAntiAlias(true);
                this.mTextPaint.setTextSize(((float) TouchScreen_VerificationPoint.this.mZoom) * 9.0f);
                this.mTextPaint.setARGB(Light.MAIN_KEY_MAX, 0, 0, 0);
                this.mRect = new Rect(0, 0, TouchScreen_VerificationPoint.this.mRectWidth, TouchScreen_VerificationPoint.this.mRectHeight);
                this.mRectPaint = new Paint();
                this.mRectPaint.setARGB(Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX);
                this.mCrossPaint = new Paint();
                this.mCrossPaint.setARGB(Light.MAIN_KEY_MAX, Light.MAIN_KEY_MAX, 0, 0);
            }

            public void run() {
                while (TouchScreen_VerificationPoint.this.mRun) {
                    Canvas canvas = null;
                    canvas = this.mSurfaceHolder.lockCanvas(null);
                    synchronized (this.mSurfaceHolder) {
                        if (canvas != null) {
                            doDraw(canvas);
                        }
                        try {
                        } catch (Throwable th) {
                            if (canvas != null) {
                                this.mSurfaceHolder.unlockCanvasAndPost(canvas);
                                SystemClock.sleep(1);
                            }
                        }
                    }
                    if (canvas != null) {
                        this.mSurfaceHolder.unlockCanvasAndPost(canvas);
                        SystemClock.sleep(1);
                    }
                }
            }

            private void doDraw(Canvas canvas) {
                canvas.drawRect(this.mRect, this.mRectPaint);
                if (TouchScreen_VerificationPoint.this.mBitmap != null) {
                    canvas.drawBitmap(TouchScreen_VerificationPoint.this.mBitmap, (float) (TouchScreen_VerificationPoint.this.PrePoint.x - TouchScreen_VerificationPoint.this.mBitmapPad), (float) (TouchScreen_VerificationPoint.this.PrePoint.y - TouchScreen_VerificationPoint.this.mBitmapPad), null);
                } else {
                    canvas.drawLine((float) (TouchScreen_VerificationPoint.this.PrePoint.x - 15), (float) (TouchScreen_VerificationPoint.this.PrePoint.y - 15), (float) (TouchScreen_VerificationPoint.this.PrePoint.x + 15), (float) (TouchScreen_VerificationPoint.this.PrePoint.y + 15), this.mCrossPaint);
                    canvas.drawLine((float) (TouchScreen_VerificationPoint.this.PrePoint.x - 15), (float) (TouchScreen_VerificationPoint.this.PrePoint.y + 15), (float) (TouchScreen_VerificationPoint.this.PrePoint.x + 15), (float) (TouchScreen_VerificationPoint.this.PrePoint.y - 15), this.mCrossPaint);
                }
                canvas.drawText("point error : " + Double.toString(TouchScreen_VerificationPoint.this.mPointError), 20.0f, (float) (TouchScreen_VerificationPoint.this.mRectHeight / 2), this.mTextPaint);
            }
        }

        public DiversityCanvas(Context context) {
            super(context);
            getHolder().addCallback(this);
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        public void surfaceCreated(SurfaceHolder holder) {
            TouchScreen_VerificationPoint.this.mRun = true;
            this.mThread = new DiversityThread(holder, null);
            this.mThread.start();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            TouchScreen_VerificationPoint.this.mRun = false;
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
            if (1080 > this.mRectWidth || 1920 > this.mRectHeight) {
                if (1920 <= this.mRectWidth && 1080 <= this.mRectHeight) {
                }
                this.rand = new Random();
                this.PrePoint = new Point(this.mRectWidth / 2, this.mRectHeight / 2);
                this.mDiversityCanvas = new DiversityCanvas(this);
                setContentView(this.mDiversityCanvas);
                this.mDiversityCanvas.setOnTouchListener(this);
                this.mBitmap = BitmapFactory.decodeResource(getResources(), 2130837510);
                if (this.mBitmap == null) {
                    this.mBitmapPad = this.mBitmap.getHeight() / 2;
                }
            }
            this.mZoom = 5;
            this.rand = new Random();
            this.PrePoint = new Point(this.mRectWidth / 2, this.mRectHeight / 2);
            this.mDiversityCanvas = new DiversityCanvas(this);
            setContentView(this.mDiversityCanvas);
            this.mDiversityCanvas.setOnTouchListener(this);
            this.mBitmap = BitmapFactory.decodeResource(getResources(), 2130837510);
            if (this.mBitmap == null) {
                this.mBitmapPad = this.mBitmap.getHeight() / 2;
            }
        }
        this.mZoom = 2;
        this.mZoom = 5;
        this.rand = new Random();
        this.PrePoint = new Point(this.mRectWidth / 2, this.mRectHeight / 2);
        this.mDiversityCanvas = new DiversityCanvas(this);
        setContentView(this.mDiversityCanvas);
        this.mDiversityCanvas.setOnTouchListener(this);
        this.mBitmap = BitmapFactory.decodeResource(getResources(), 2130837510);
        if (this.mBitmap == null) {
            this.mBitmapPad = this.mBitmap.getHeight() / 2;
        }
    }

    public boolean onTouch(View arg0, MotionEvent event) {
        if (event.getAction() == 0) {
            Log.i(this.TAG, "The PrePoint.x value is : " + String.valueOf(this.PrePoint.x));
            Log.i(this.TAG, "The PrePoint.y value is : " + String.valueOf(this.PrePoint.y));
            int xTouch = (int) event.getX();
            int yTouch = (int) event.getY();
            Log.i(this.TAG, "The xTouch value is : " + String.valueOf(xTouch));
            Log.i(this.TAG, "The yTouch value is : " + String.valueOf(yTouch));
            this.mPointError = Math.sqrt((double) (((xTouch - this.PrePoint.x) * (xTouch - this.PrePoint.x)) + ((yTouch - this.PrePoint.y) * (yTouch - this.PrePoint.y))));
            this.PrePoint = new Point(this.rand.nextInt(this.mRectWidth), this.rand.nextInt(this.mRectHeight));
        }
        return true;
    }
}
