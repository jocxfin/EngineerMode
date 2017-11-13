package com.android.engineeringmode.autotest;

import android.os.Handler;
import android.os.Message;

import com.android.engineeringmode.Log;

public class ExitHandler extends Handler {
    private AutoTestItemActivity mActivity = null;

    public ExitHandler(AutoTestItemActivity activity) {
        this.mActivity = activity;
    }

    public void handleMessage(Message msg) {
        if (1 == msg.what) {
            if (this.mActivity != null) {
                Log.i("ExitHandler", "finish the activity: " + this.mActivity.getClass().getName());
                this.mActivity.endActivity();
                this.mActivity = null;
            } else {
                Log.w("ExitHandler", "mActivity = null");
            }
            removeMessages(1);
        }
    }
}
