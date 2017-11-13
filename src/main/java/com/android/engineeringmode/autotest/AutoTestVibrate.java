package com.android.engineeringmode.autotest;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.widget.TextView;

import com.android.engineeringmode.Log;

public class AutoTestVibrate extends AutoTestItemActivity {
    private Vibrator mVibrate = null;
    private int mVirbateTime = 2000;
    private boolean mbIsFinish = false;
    private TextView mtvShow = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(2131296276);
        this.mVibrate = (Vibrator) getSystemService("vibrator");
        this.mVibrate.vibrate((long) this.mVirbateTime);
        this.mtvShow = new TextView(this);
        setContentView(this.mtvShow);
        this.mtvShow.setTextSize(40.0f);
        this.mtvShow.setText(2131296743);
        this.mtvShow.setGravity(17);
        this.mtvShow.setTextColor(getResources().getColor(17170444));
        setAutoExit(this.mVirbateTime + 500);
        setRequestedOrientation(1);
    }

    protected void onDestroy() {
        Log.i("AutoTestVibrate", "onDestroy");
        this.mVibrate = null;
        this.mtvShow = null;
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("AutoTestVibrate", "onKeyDown, keyCode = " + keyCode);
        if (this.mbIsFinish) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }
}
