package com.android.engineeringmode.LCDtest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.engineeringmode.Log;
import com.android.engineeringmode.functions.Light;

public class LCDTestView extends View implements OnClickListener {
    private int mCurrentFrameIndex;
    private Handler mHandler;
    private OnFinishListener mOnFinishListener;
    private int mRepeatTime;
    private int mRepeated;
    private boolean mbIsAutoShowing;
    private boolean mbIsNeedPictureShow;
    private boolean mbIsTouchable;

    public interface OnFinishListener {
        void onFinished();
    }

    public LCDTestView(Context context) {
        this(context, null);
    }

    public LCDTestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LCDTestView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (9 == msg.what) {
                    Log.d("LCDTestView", "handleMessage(), msg SHOWNEXT = 9");
                    if (LCDTestView.this.mbIsAutoShowing) {
                        LCDTestView.this.showNext();
                        LCDTestView.this.mHandler.removeMessages(9);
                        LCDTestView.this.mHandler.sendEmptyMessageDelayed(9, 2000);
                    }
                }
            }
        };
        this.mbIsAutoShowing = false;
        this.mbIsNeedPictureShow = false;
        this.mbIsTouchable = false;
        this.mRepeatTime = 1;
        this.mRepeated = 0;
        this.mCurrentFrameIndex = 0;
        this.mOnFinishListener = null;
        setOnClickListener(this);
    }

    public void startShowing() {
        resetCurrentFrameIndex();
        if (this.mbIsAutoShowing) {
            this.mHandler.removeMessages(9);
            this.mHandler.sendEmptyMessage(9);
            return;
        }
        showNext();
    }

    public void stopShowing() {
        this.mHandler.removeMessages(9);
    }

    public void setIsAutoShowing(boolean bIsAutoShow) {
        this.mbIsAutoShowing = bIsAutoShow;
    }

    public void setIsTouchable(boolean bIsTouchable) {
        this.mbIsTouchable = bIsTouchable;
    }

    public void setNeedShowPicture(boolean bIsNeedShowPicture) {
        this.mbIsNeedPictureShow = bIsNeedShowPicture;
    }

    public void setRepeatTime(int repeatTime) {
        this.mRepeatTime = repeatTime;
    }

    public void showColor(int showColor) {
        switch (showColor) {
            case 0:
                setBackgroundColor(-65536);
                return;
            case Light.MAIN_KEY_LIGHT /*1*/:
                setBackgroundColor(-16711936);
                return;
            case Light.CHARGE_RED_LIGHT /*2*/:
                setBackgroundColor(-16776961);
                return;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                setBackgroundColor(-1);
                return;
            case 4:
                setBackgroundColor(-16777216);
                return;
            case 5:
                setBackgroundColor(-8355712);
                return;
            default:
                return;
        }
    }

    protected void showPic(int nWhich) {
        switch (nWhich) {
            case Light.MAIN_KEY_NORMAL /*6*/:
                setBackgroundResource(2130837512);
                return;
            case 7:
                setBackgroundResource(2130837509);
                return;
            case 8:
                setBackgroundResource(2130837511);
                return;
            default:
                return;
        }
    }

    public void onClick(View v) {
        if (this.mbIsTouchable) {
            if (this.mbIsAutoShowing) {
                this.mHandler.removeMessages(9);
                this.mHandler.sendEmptyMessageDelayed(9, 2000);
            } else {
                this.mHandler.removeMessages(9);
            }
            showNext();
        }
    }

    public void showNext() {
        if (-1 == this.mRepeatTime) {
            if (canShowNext()) {
                this.mCurrentFrameIndex++;
            } else {
                resetCurrentFrameIndex();
            }
            show(this.mCurrentFrameIndex);
        } else if (canShowNext()) {
            this.mCurrentFrameIndex++;
            show(this.mCurrentFrameIndex);
        } else {
            this.mRepeated++;
            if (this.mRepeated >= this.mRepeatTime) {
                if (this.mOnFinishListener != null) {
                    this.mOnFinishListener.onFinished();
                }
                this.mHandler.removeMessages(9);
                return;
            }
            resetCurrentFrameIndex();
            show(this.mCurrentFrameIndex);
        }
    }

    public void show(int frameIndex) {
        if (frameIndex <= 5) {
            showColor(frameIndex);
        } else {
            showPic(frameIndex);
        }
    }

    private void resetCurrentFrameIndex() {
        this.mCurrentFrameIndex = -1;
    }

    private boolean canShowNext() {
        if (this.mbIsNeedPictureShow) {
            if (this.mCurrentFrameIndex < 8) {
                return true;
            }
        } else if (this.mCurrentFrameIndex < 5) {
            return true;
        }
        return false;
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.mOnFinishListener = onFinishListener;
    }

    public void onDestroy() {
        this.mHandler.removeMessages(9);
        this.mHandler = null;
    }
}
