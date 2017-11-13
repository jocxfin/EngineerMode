package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

public class GSensorAdjust extends Activity {
    private final int UPDATE_TEXT = 1;
    private AlertDialog dialog;
    private LightSensorUtils lsUtils;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String info;
                if (GSensorAdjust.this.value == 0) {
                    info = (String) GSensorAdjust.this.getText(2131296965);
                    GSensorAdjust.this.textInfo.setTextColor(-16711936);
                } else {
                    info = (String) GSensorAdjust.this.getText(2131296966);
                    GSensorAdjust.this.textInfo.setTextColor(-65536);
                }
                GSensorAdjust.this.textInfo.setText(info);
            }
        }
    };
    private Thread mThread;
    private TextView textInfo;
    private int value;

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
                Log.d("ProximityAdjust", "Adjust ret: " + GSensorAdjust.this.value);
                GSensorAdjust.this.mHandler.sendEmptyMessage(1);
            }
        });
        this.dialog = new Builder(this).setTitle("Warning").setMessage(2131296964).setPositiveButton(17039370, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                GSensorAdjust.this.mThread.start();
                dialog.dismiss();
            }
        }).create();
        this.dialog.setCancelable(false);
        this.dialog.show();
    }
}
