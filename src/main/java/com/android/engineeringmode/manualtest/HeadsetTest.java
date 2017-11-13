package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.TextView;

import com.android.engineeringmode.Log;

import java.util.Calendar;

public class HeadsetTest extends Activity {
    int mHeadCategory;
    private final BroadcastReceiver mHeadsetPlug = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.HEADSET_PLUG")) {
                HeadsetTest.this.mHeadsetState = intent.getIntExtra("state", 0);
                HeadsetTest.this.mHeadCategory = intent.getIntExtra("standard", 0);
                HeadsetTest.this.mNoMIc = intent.getIntExtra("microphone", 0);
                Log.d("HeadSetTest", "mHeadCategory=" + HeadsetTest.this.mHeadsetState + ",mHeadCategory=" + HeadsetTest.this.mHeadCategory);
                HeadsetTest.this.showHeadsetState();
                Log.w("HeadSetTest", "receive the action Intent.ACTION_HEADSET_PLUG");
                Log.w("HeadSetTest", "mbIsHeadsetPlugged = " + HeadsetTest.this.mbIsHeadsetPlugged);
            }
        }
    };
    int mHeadsetState;
    IntentFilter mIntentFilter;
    int mNoMIc;
    private boolean mbIsHeadsetPlugged;
    TextView mtvHeadsetName;
    TextView mtvStatus;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903118);
        setTitle(2131296750);
        this.mtvStatus = (TextView) findViewById(2131493192);
        this.mtvHeadsetName = (TextView) findViewById(2131493193);
        this.mIntentFilter = new IntentFilter();
        if (this.mIntentFilter == null) {
            finish();
        }
        getInitHeadsetState();
        showHeadsetState();
        this.mIntentFilter.addAction("android.intent.action.HEADSET_PLUG");
    }

    protected void onResume() {
        registerReceiver(this.mHeadsetPlug, this.mIntentFilter);
        super.onResume();
    }

    protected void onPause() {
        unregisterReceiver(this.mHeadsetPlug);
        super.onPause();
    }

    protected void onDestroy() {
        Calendar now = Calendar.getInstance();
        String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--HeadsetTest--" + "PASS";
        this.mIntentFilter = null;
        super.onDestroy();
    }

    void getInitHeadsetState() {
        this.mbIsHeadsetPlugged = ((AudioManager) getSystemService("audio")).isWiredHeadsetOn();
        Log.w("HeadSetTest", "in getInitHeadsetState(), mbIsHeadsetPlugged = " + this.mbIsHeadsetPlugged);
    }

    void showHeadsetState() {
        Log.w("HeadSetTest", "showHeadsetState");
        if (this.mHeadsetState == 0) {
            this.mtvStatus.setText(2131296840);
        }
        if (this.mHeadsetState != 1) {
            return;
        }
        if (this.mNoMIc == 1) {
            if (this.mHeadCategory == 0) {
                this.mtvStatus.setText(2131296842);
            } else if (this.mHeadCategory == 1) {
                this.mtvStatus.setText(2131296841);
            }
        } else if (this.mNoMIc == 0) {
            this.mtvStatus.setText(2131296839);
        }
    }
}
