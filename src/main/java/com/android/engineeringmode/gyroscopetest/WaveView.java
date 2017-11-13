package com.android.engineeringmode.gyroscopetest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.util.LinkedList;

public class WaveView extends SurfaceView implements Runnable, Callback {
    private int mBackgroundColor;
    private Context mContext;
    private Object mLock;
    private volatile boolean mLoop;
    private Paint mPaint;
    private LinkedList<DataWrap> mPoints;
    private SurfaceHolder mSurfaceHolder;
    Path xPath;
    Path yPath;
    Path zPath;

    public class DataWrap {
        public int data1;
        public int data2;
        public int data3;

        public DataWrap(int data1, int data2, int data3) {
            this.data1 = data1;
            this.data2 = data2;
            this.data3 = data3;
        }
    }

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mLock = new Object();
        init(context);
    }

    private void init(Context context) {
        this.xPath = new Path();
        this.yPath = new Path();
        this.zPath = new Path();
        this.mPaint = new Paint();
        this.mPoints = new LinkedList();
        this.mSurfaceHolder = getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mContext = context;
        this.mBackgroundColor = this.mContext.getResources().getColor(2131165187);
    }

    public void addPoint(int x, int y, int z) {
        if (this.mPoints.size() > 0 && this.mPoints.size() >= getWidth()) {
            this.mPoints.removeFirst();
        }
        this.mPoints.add(new DataWrap(processData(x), processData(y), processData(z)));
    }

    private int processData(int data) {
        if (data > (getHeight() / 2) - 1) {
            data = (getHeight() / 2) - 1;
        } else if (data < ((-getHeight()) / 2) + 1) {
            data = ((-getHeight()) / 2) + 1;
        }
        return (getHeight() / 2) - data;
    }

    public void draw() {
        if (this.mLoop) {
            Canvas canvas = this.mSurfaceHolder.lockCanvas();
            if (canvas != null) {
                DataWrap data;
                this.mPaint.setAntiAlias(true);
                canvas.drawColor(this.mBackgroundColor);
                this.mPaint.setColor(-1);
                this.mPaint.setStrokeWidth(2.0f);
                canvas.drawLine(2.0f, 0.0f, 2.0f, (float) getHeight(), this.mPaint);
                canvas.drawLine(2.0f, (float) (getHeight() / 2), (float) getWidth(), (float) (getHeight() / 2), this.mPaint);
                this.mPaint.setStrokeWidth(1.0f);
                canvas.drawLine(2.0f, (float) ((getHeight() / 2) - 150), (float) getWidth(), (float) ((getHeight() / 2) - 150), this.mPaint);
                canvas.drawLine(2.0f, (float) ((getHeight() / 2) + 150), (float) getWidth(), (float) ((getHeight() / 2) + 150), this.mPaint);
                this.mPaint.setStrokeCap(Cap.ROUND);
                this.mPaint.setStyle(Style.STROKE);
                int i = 0;
                this.xPath.rewind();
                this.yPath.rewind();
                this.zPath.rewind();
                if (this.mPoints.size() > 0) {
                    data = (DataWrap) this.mPoints.get(0);
                    this.xPath.moveTo(0.0f, (float) data.data1);
                    this.yPath.moveTo(0.0f, (float) data.data2);
                    this.zPath.moveTo(0.0f, (float) data.data3);
                    i = 1;
                }
                while (i < this.mPoints.size()) {
                    data = (DataWrap) this.mPoints.get(i);
                    DataWrap preData = (DataWrap) this.mPoints.get(i - 1);
                    this.xPath.quadTo((float) (i - 1), (float) preData.data1, (float) i, (float) data.data1);
                    this.yPath.quadTo((float) (i - 1), (float) preData.data2, (float) i, (float) data.data2);
                    this.zPath.quadTo((float) (i - 1), (float) preData.data3, (float) i, (float) data.data3);
                    i++;
                }
                this.mPaint.setColor(-65536);
                this.mPaint.setStrokeWidth(5.0f);
                canvas.drawPath(this.xPath, this.mPaint);
                this.mPaint.setColor(-16711936);
                this.mPaint.setStrokeWidth(4.0f);
                canvas.drawPath(this.yPath, this.mPaint);
                this.mPaint.setColor(-16776961);
                this.mPaint.setStrokeWidth(3.0f);
                canvas.drawPath(this.zPath, this.mPaint);
                try {
                    this.mSurfaceHolder.unlockCanvasAndPost(canvas);
                } catch (IllegalArgumentException e) {
                }
            }
        }
    }

    public void run() {
        while (this.mLoop) {
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this.mLock) {
                if (this.mLoop) {
                    draw();
                }
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        synchronized (this.mLock) {
            this.mLoop = true;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this.mLock) {
            this.mLoop = false;
        }
    }
}
