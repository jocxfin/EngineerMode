package com.android.engineeringmode.manualtest.modeltest;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.android.engineeringmode.functions.Light;

public class ModelTest3ItemActivity extends Activity {
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    ModelTest3ItemActivity.this.setResult(1);
                    ModelTest3ItemActivity.this.finish();
                    return;
                default:
                    return;
            }
        }
    };

    protected void onTestPassed() {
        this.handler.sendEmptyMessageDelayed(1, 500);
    }

    public void onBackPressed() {
        setResult(4);
        finish();
    }
}
