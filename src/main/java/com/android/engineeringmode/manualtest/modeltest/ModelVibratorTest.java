package com.android.engineeringmode.manualtest.modeltest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

public class ModelVibratorTest extends ModelTest3ItemActivity {
    private static final long[] sVibratePattern = new long[]{500, 1000, 500};
    OnClickListener judgeClickLisenter = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case 2131493015:
                    ModelVibratorTest.this.mVibrate.cancel();
                    ModelVibratorTest.this.setResult(1);
                    ModelVibratorTest.this.finish();
                    return;
                case 2131493236:
                    ModelVibratorTest.this.mVibrate.cancel();
                    ModelVibratorTest.this.setResult(2);
                    ModelVibratorTest.this.finish();
                    return;
                case 2131493237:
                    ModelVibratorTest.this.mVibrate.cancel();
                    ModelVibratorTest.this.setResult(3);
                    ModelVibratorTest.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private Handler mHander = new Handler() {
        public void handleMessage(Message msg) {
            ModelVibratorTest.this.setLisentenersForJudgeButtons();
        }
    };
    private TextView mTv = null;
    private Vibrator mVibrate = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903221);
        initResource();
    }

    public void onPause() {
        this.mVibrate.cancel();
        super.onPause();
    }

    private void initResource() {
        setTitle(2131296276);
        this.mTv = (TextView) findViewById(2131493593);
        this.mVibrate = (Vibrator) getSystemService("vibrator");
        this.mVibrate.vibrate(sVibratePattern, 0);
        this.mHander.sendEmptyMessageDelayed(1, 4000);
    }

    private void setLisentenersForJudgeButtons() {
        if (getIntent().getBooleanExtra("model_test", false)) {
            ((ViewStub) findViewById(2131493190)).setVisibility(0);
            ((Button) findViewById(2131493015)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493236)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493237)).setOnClickListener(this.judgeClickLisenter);
        }
    }
}
