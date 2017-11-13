package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

public class ProximityAdjust extends Activity {
    private final int DATA_LENGTH = 32;
    private final int UPDATE_TEXT = 1;
    private LightSensorUtils lsUtils;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("mean:").append(350).append("\n");
                ProximityAdjust.this.textInfo.setText(sb.toString() + ((String) ProximityAdjust.this.getText(2131296987)));
            }
        }
    };
    private Thread mThread;
    private TextView textInfo;
    private int[] value;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.textInfo = (TextView) LayoutInflater.from(this).inflate(2130903203, null);
        this.textInfo.setText(2131296986);
        this.textInfo.setGravity(17);
        this.textInfo.setTextSize(30.0f);
        this.textInfo.setBackgroundColor(2131165187);
        setContentView(this.textInfo);
        this.lsUtils = new LightSensorUtils(this);
        this.mThread = new Thread(null, new Runnable() {
            public void run() {
                ProximityAdjust.this.lsUtils.openLightSensor();
                ProximityAdjust.this.value = ProximityAdjust.this.lsUtils.startAdjust();
                Log.e("ProximityAdjust", "high:" + ProximityAdjust.this.value[0] + " low:" + ProximityAdjust.this.value[1]);
                ProximityAdjust.this.mHandler.sendEmptyMessage(1);
            }
        });
        this.mThread.start();
    }
}
