package com.android.engineeringmode.autotest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.android.engineeringmode.KeepScreenOnActivity;
import com.android.engineeringmode.Log;

public class HeadsetDetectTest extends KeepScreenOnActivity {
    private final BroadcastReceiver mHeadsetPlug = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.HEADSET_PLUG")) {
                HeadsetDetectTest.this.mNoMIc = intent.getIntExtra("microphone", 0);
                if (((AudioManager) HeadsetDetectTest.this.getSystemService("audio")).isWiredHeadsetOn()) {
                    if (HeadsetDetectTest.this.mNoMIc == 1) {
                        HeadsetDetectTest.this.result = "四段式耳机\n";
                        HeadsetDetectTest.this.mtvShow.setText(HeadsetDetectTest.this.result);
                        if (HeadsetDetectTest.this.getIntent().getBooleanExtra("model_test", false)) {
                            ((Activity) context).setResult(1);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    HeadsetDetectTest.this.finish();
                                }
                            }, 2000);
                        }
                    } else {
                        HeadsetDetectTest.this.result = "非四段式耳机！！！\nFAIL！！！\n";
                        HeadsetDetectTest.this.mtvShow.setText(HeadsetDetectTest.this.result);
                    }
                }
                Log.d("HeadsetDetectTest", "mNoMIc=" + HeadsetDetectTest.this.mNoMIc);
                Log.w("HeadsetDetectTest", "receive the action Intent.ACTION_HEADSET_PLUG");
            }
        }
    };
    private IntentFilter mIntentFilter;
    private int mNoMIc = 0;
    private TextView mtvShow = null;
    private String result = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("耳机检测");
        setContentView(2130903117);
        this.mtvShow = (TextView) findViewById(2131493189);
        this.mtvShow.setText(this.result);
        this.mIntentFilter = new IntentFilter();
        if (this.mIntentFilter == null) {
            finish();
        }
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
        Log.i("HeadsetDetectTest", "onDestroy");
        this.mtvShow = null;
        this.mIntentFilter = null;
        super.onDestroy();
    }
}
