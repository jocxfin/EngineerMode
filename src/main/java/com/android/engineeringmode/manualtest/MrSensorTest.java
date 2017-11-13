package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.android.engineeringmode.Log;

public class MrSensorTest extends Activity {
    private final String EXTRA = "isLidOpen";
    private final String RECEIVER = "com.oppo.keyboard.SLID";
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("mrsendor", "action:" + action);
            if (action.equals("com.oppo.keyboard.SLID")) {
                int isopen = intent.getIntExtra("isLidOpen", -1);
                if (isopen == 0) {
                    MrSensorTest.this.mTextView.setText(2131297022);
                } else if (isopen == 1) {
                    MrSensorTest.this.mTextView.setText(2131297023);
                } else {
                    MrSensorTest.this.mTextView.setText(2131296815);
                }
            }
        }
    };
    private TextView mTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTextView = (TextView) LayoutInflater.from(this).inflate(2130903203, null);
        this.mTextView.setGravity(17);
        this.mTextView.setTextSize(30.0f);
        this.mTextView.setBackgroundColor(2131165187);
        setContentView(this.mTextView);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.oppo.keyboard.SLID");
        registerReceiver(this.mBroadcastReceiver, filter);
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mBroadcastReceiver);
    }
}
