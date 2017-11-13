package com.android.engineeringmode.wifimtk;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.engineeringmode.functions.Light;

public class WiFi_TemperatureSensor extends Activity implements OnClickListener {
    private final int HANDLER_EVENT_TEMPERATURE_SENSOR = 1;
    private final String TAG = "EM_WiFi_TemperatureSensor";
    private boolean fgOriThermoEn = false;
    private Button mClearBtn;
    private Button mGoBtn;
    private Handler mHandler;
    private EditText mLogShow;
    private Button mStopBtn;
    private int u4RunNum = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903234);
        this.mGoBtn = (Button) findViewById(2131493677);
        this.mStopBtn = (Button) findViewById(2131493678);
        this.mClearBtn = (Button) findViewById(2131493679);
        this.mLogShow = (EditText) findViewById(2131493680);
        this.mGoBtn.setOnClickListener(this);
        this.mStopBtn.setOnClickListener(this);
        this.mClearBtn.setOnClickListener(this);
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                long[] u4ThermoRaw = new long[2];
                switch (msg.what) {
                    case Light.MAIN_KEY_LIGHT /*1*/:
                        Log.d("EM_WiFi_TemperatureSensor", "The Handle event is : HANDLER_EVENT_TEMPERATURE_SENSOR");
                        EMWifi.queryThermoInfo(u4ThermoRaw, 2);
                        if (u4ThermoRaw[1] == -1) {
                            WiFi_TemperatureSensor.this.mLogShow.append("Hardware report busy after 99 times retry\n");
                        } else {
                            EditText - get1 = WiFi_TemperatureSensor.this.mLogShow;
                            StringBuilder append = new StringBuilder().append("Run ");
                            WiFi_TemperatureSensor wiFi_TemperatureSensor = WiFi_TemperatureSensor.this;
                            -get1.append(append.append(wiFi_TemperatureSensor.u4RunNum = wiFi_TemperatureSensor.u4RunNum + 1).append(", Thermo value ").append(Long.toHexString(u4ThermoRaw[1])).append("\n").toString());
                        }
                        WiFi_TemperatureSensor.this.mHandler.sendEmptyMessageDelayed(1, 1000);
                        return;
                    default:
                        return;
                }
            }
        };
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case 2131493677:
                onClickThermoGoBtn();
                return;
            case 2131493678:
                onClickThermoStopBtn();
                return;
            case 2131493679:
                this.mLogShow.setText("");
                return;
            default:
                return;
        }
    }

    void onClickThermoStopBtn() {
        this.mHandler.removeMessages(1);
        if (!this.fgOriThermoEn) {
            EMWifi.setThermoEn(0);
        }
        this.mGoBtn.setEnabled(true);
    }

    void onClickThermoGoBtn() {
        long[] i4AlcEn = new long[2];
        EMWifi.queryThermoInfo(i4AlcEn, 2);
        if (i4AlcEn[0] == 1) {
            this.fgOriThermoEn = true;
        } else {
            this.fgOriThermoEn = false;
            EMWifi.setThermoEn(1);
        }
        this.u4RunNum = 0;
        this.mHandler.sendEmptyMessage(1);
        this.mGoBtn.setEnabled(false);
    }

    protected void onStop() {
        super.onStop();
        this.mHandler.removeMessages(1);
        this.mGoBtn.setEnabled(true);
    }
}
