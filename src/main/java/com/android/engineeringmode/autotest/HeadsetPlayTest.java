package com.android.engineeringmode.autotest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;

import com.android.engineeringmode.KeepScreenOnActivity;
import com.android.engineeringmode.Log;
import com.android.engineeringmode.audiotest.InternalRingtonePlay;

public class HeadsetPlayTest extends KeepScreenOnActivity {
    OnClickListener judgeClickLisenter = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case 2131493015:
                    HeadsetPlayTest.this.setResult(1);
                    HeadsetPlayTest.this.finish();
                    return;
                case 2131493236:
                    HeadsetPlayTest.this.setResult(2);
                    HeadsetPlayTest.this.finish();
                    return;
                case 2131493237:
                    HeadsetPlayTest.this.setResult(3);
                    HeadsetPlayTest.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private Handler mHander = new Handler() {
        public void handleMessage(Message msg) {
            HeadsetPlayTest.this.setLisentenersForJudgeButtons();
        }
    };
    private InternalRingtonePlay mInternalRingtonePlayer = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("播放音乐");
        setContentView(2130903117);
        this.mInternalRingtonePlayer = new InternalRingtonePlay(this);
        this.mInternalRingtonePlayer.play();
        this.mInternalRingtonePlayer.setRepeatTime(5);
        this.mHander.sendEmptyMessageDelayed(1, 5000);
    }

    private void setLisentenersForJudgeButtons() {
        if (getIntent().getBooleanExtra("model_test", false)) {
            ((ViewStub) findViewById(2131493190)).setVisibility(0);
            ((Button) findViewById(2131493015)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493236)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493237)).setOnClickListener(this.judgeClickLisenter);
        }
    }

    protected void onDestroy() {
        Log.i("HeadsetPlayTest", "onDestroy");
        if (this.mInternalRingtonePlayer != null) {
            this.mInternalRingtonePlayer.stop();
        }
        super.onDestroy();
    }
}
