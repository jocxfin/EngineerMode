package com.android.engineeringmode.autoaging;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.engineeringmode.autotest.AutoBaseActivity;
import com.android.engineeringmode.autotest.AutoTestBaseManager;
import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.util.WakeLock;

import java.util.Calendar;

public class AutoAgingActivity extends AutoBaseActivity {
    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    AutoAgingActivity.this.mStartTime = System.currentTimeMillis();
                    AutoAgingActivity.this.beginTest();
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    AutoAgingActivity.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private long mStartTime;
    private long mTotalDuration = -1;

    private class TimeDialogClickListener implements OnClickListener {
        private TimeDialogClickListener() {
        }

        public void onClick(DialogInterface dialog, int which) {
            AutoAgingActivity.this.mHandler.sendEmptyMessage(2);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(2131296920);
        setRequestedOrientation(1);
        Calendar now = Calendar.getInstance();
        String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--AutoAgingActivity--" + "has entered it";
        this.mHandler.sendEmptyMessageDelayed(1, 200);
    }

    protected void onResume() {
        super.onResume();
        WakeLock.acquire(this);
    }

    protected void onPause() {
        super.onPause();
        WakeLock.release();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public AutoTestBaseManager newTestManager() {
        return new AutoagingManager(this);
    }

    public void onAllTestEnd() {
        Log.d("AutoAgingActivity", "onAllTestEnd()");
        finish();
    }

    public void onOneTestEnd(Intent data) {
        if (this.mAutoTestManager.hasNext()) {
            this.mAutoTestManager.gotoNext();
            Intent intent = this.mAutoTestManager.getCurIntent();
            if (intent == null) {
                onAllTestEnd();
            } else if (isTimeOver()) {
                showDialog(888);
            } else {
                startNormalActivityWithData(intent);
            }
        }
    }

    protected Dialog onCreateDialog(int id) {
        if (888 != id) {
            return null;
        }
        Builder builder = new Builder(this);
        builder.setTitle(17039380).setPositiveButton(17039370, new TimeDialogClickListener()).setMessage(2131296938);
        return builder.create();
    }

    public void onTestFailed(String title) {
        Log.d("AutoAgingActivity", "onTestFailed(), title = " + title);
    }

    private boolean isTimeOver() {
        boolean z = false;
        if (this.mTotalDuration <= 0) {
            return false;
        }
        if (System.currentTimeMillis() - this.mStartTime >= this.mTotalDuration) {
            z = true;
        }
        return z;
    }
}
