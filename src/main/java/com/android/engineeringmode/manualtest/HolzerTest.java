package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;
import com.oem.util.Feature;

import java.util.Calendar;

public class HolzerTest extends Activity {
    private final String ACTION_DISABLE_LIDCONTROLSSLEEP = "oem.intent.action.DISABLE_LIDCONTROLSSLEEP";
    private final String ACTION_ENABLE_LIDCONTROLSSLEEP = "oem.intent.action.ENABLE_LIDCONTROLSSLEEP";
    private final int HOLZER_TEST_PASS = 1;
    private boolean IsHallAutoTest = false;
    private boolean isInModelTest = false;
    OnClickListener judgeClickLisenter = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case 2131493015:
                    HolzerTest.this.setResult(1);
                    HolzerTest.this.finish();
                    return;
                case 2131493236:
                    HolzerTest.this.setResult(2);
                    HolzerTest.this.finish();
                    return;
                case 2131493237:
                    HolzerTest.this.setResult(3);
                    HolzerTest.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private boolean lib_closed = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    HolzerTest.this.setResult(1);
                    HolzerTest.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private View mLayoutView;
    private boolean mLidControlsSleep;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.i("HolzerTest", "onReceive atcion:" + intent.getAction());
            int libopen = intent.getIntExtra("lidOpen", 1);
            Log.i("HolzerTest", "lidOpen : " + libopen);
            if (libopen == 1) {
                HolzerTest.this.mtvKeepScreenOn.setText(2131297213);
                HolzerTest.this.mtvKeepScreenOn.setTextSize(50.0f);
                HolzerTest.this.mtvKeepScreenOn.setTextColor(-1);
                HolzerTest.this.mLayoutView.setBackgroundColor(-16777216);
                if (HolzerTest.this.lib_closed && HolzerTest.this.isInModelTest) {
                    HolzerTest.this.mHandler.sendEmptyMessageDelayed(1, 500);
                }
            } else if (libopen == 0) {
                HolzerTest.this.mtvKeepScreenOn.setText(2131297214);
                HolzerTest.this.mtvKeepScreenOn.setTextSize(50.0f);
                HolzerTest.this.mtvKeepScreenOn.setTextColor(-1);
                HolzerTest.this.mLayoutView.setBackgroundColor(-16711936);
                HolzerTest.this.lib_closed = true;
            }
        }
    };
    private boolean mReceiverRegisted = false;
    private TextView mtvKeepScreenOn = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903127);
        this.mtvKeepScreenOn = (TextView) findViewById(2131493239);
        this.mLayoutView = findViewById(2131493238);
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
        this.mLidControlsSleep = getResources().getBoolean(17956923);
        Log.i("HolzerTest", "mLidControlsSleep:" + this.mLidControlsSleep);
        this.IsHallAutoTest = Feature.isHallAutoTestSupported(this);
        if (this.IsHallAutoTest) {
            this.mtvKeepScreenOn.setText(2131297211);
        } else {
            this.mtvKeepScreenOn.setText(2131297210);
        }
        Calendar now = Calendar.getInstance();
        String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--HolzerTest--" + "has entered it";
        if (!this.IsHallAutoTest) {
            setLisentenersForJudgeButtons();
        }
    }

    private void setLisentenersForJudgeButtons() {
        if (this.isInModelTest) {
            ((ViewStub) findViewById(2131493190)).setVisibility(0);
            ((Button) findViewById(2131493015)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493236)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493237)).setOnClickListener(this.judgeClickLisenter);
        }
    }

    protected void onPause() {
        super.onPause();
        if (this.mLidControlsSleep && this.IsHallAutoTest) {
            sendBroadcast(new Intent("oem.intent.action.ENABLE_LIDCONTROLSSLEEP"));
        }
        if (this.mReceiverRegisted && this.IsHallAutoTest) {
            this.mReceiverRegisted = false;
            unregisterReceiver(this.mReceiver);
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.mLidControlsSleep && this.IsHallAutoTest) {
            sendBroadcast(new Intent("oem.intent.action.DISABLE_LIDCONTROLSSLEEP"));
        }
        if (!this.mReceiverRegisted && this.IsHallAutoTest) {
            this.mReceiverRegisted = true;
            registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.LID_SWITCH"));
        }
    }
}
