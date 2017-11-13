package com.android.engineeringmode.wifimtk;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;

public class WiFi_Rx extends Activity implements OnClickListener {
    private final int HANDLER_EVENT_RX = 2;
    private final String TAG = "EM_WiFi_Rx";
    private long[] i4Init;
    private CheckBox mALCCheck;
    private ChannelInfo mChannel;
    private ArrayAdapter<String> mChannelAdatper;
    private int mChannelIndex = 0;
    private Spinner mChannelSpinner;
    private TextView mFCSText;
    private Button mGoBtn;
    private Handler mHandler;
    private TextView mPerText;
    private TextView mRxText;
    private Button mStopBtn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903231);
        this.mFCSText = (TextView) findViewById(2131493643);
        this.mRxText = (TextView) findViewById(2131493645);
        this.mPerText = (TextView) findViewById(2131493647);
        this.mALCCheck = (CheckBox) findViewById(2131493648);
        this.mGoBtn = (Button) findViewById(2131493649);
        this.mStopBtn = (Button) findViewById(2131493650);
        this.mGoBtn.setOnClickListener(this);
        this.mStopBtn.setOnClickListener(this);
        this.i4Init = new long[2];
        this.mChannel = new ChannelInfo();
        this.mChannelSpinner = (Spinner) findViewById(2131493641);
        this.mChannelAdatper = new ArrayAdapter(this, 17367048);
        this.mChannelAdatper.setDropDownViewResource(17367049);
        for (int i = 0; i < this.mChannel.getChannelNumber(); i++) {
            this.mChannelAdatper.add(this.mChannel.mChannelName[i]);
        }
        this.mChannelSpinner.setAdapter(this.mChannelAdatper);
        this.mChannelSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                WiFi_Rx.this.mChannel.mChannelIndex = arg2;
                Log.i("EM_WiFi_Rx", "The mChannelIndex is : " + arg2);
                EMWifi.setChannel((long) WiFi_Rx.this.mChannel.getChannelFreq());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Light.CHARGE_RED_LIGHT /*2*/:
                        long[] i4Rx = new long[2];
                        long i4RxPer = -1;
                        Log.i("EM_WiFi_Rx", "The Handle event is : HANDLER_EVENT_RX");
                        try {
                            i4RxPer = Long.parseLong(WiFi_Rx.this.mPerText.getText().toString());
                        } catch (NumberFormatException e) {
                        }
                        EMWifi.getPacketRxStatus(i4Rx, 2);
                        long i4RxCntOk = i4Rx[0] - WiFi_Rx.this.i4Init[0];
                        long i4RxCntFcsErr = i4Rx[1] - WiFi_Rx.this.i4Init[1];
                        if (i4RxCntFcsErr + i4RxCntOk != 0) {
                            i4RxPer = (100 * i4RxCntFcsErr) / (i4RxCntFcsErr + i4RxCntOk);
                        }
                        WiFi_Rx.this.mFCSText.setText(String.valueOf(i4RxCntFcsErr));
                        WiFi_Rx.this.mRxText.setText(String.valueOf(i4RxCntOk));
                        WiFi_Rx.this.mPerText.setText(String.valueOf(i4RxPer));
                        break;
                }
                WiFi_Rx.this.mHandler.sendEmptyMessageDelayed(2, 1000);
            }
        };
    }

    public void onClick(View arg0) {
        if (arg0.getId() == this.mGoBtn.getId()) {
            onClickBtnRxGo();
        }
        if (arg0.getId() == this.mStopBtn.getId()) {
            onClickBtnRxStop();
        }
    }

    protected void onStop() {
        super.onStop();
        this.mHandler.removeMessages(2);
        this.mGoBtn.setEnabled(true);
    }

    private void onClickBtnRxGo() {
        int i;
        EMWifi.getPacketRxStatus(this.i4Init, 2);
        Log.d("itiNIT[0]", String.valueOf(this.i4Init[0]));
        Log.d("itiNIT[1]", String.valueOf(this.i4Init[1]));
        if (this.mALCCheck.isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        EMWifi.setATParam(9, (long) i);
        EMWifi.setATParam(1, 2);
        this.mHandler.sendEmptyMessage(2);
        this.mFCSText.setText("0");
        this.mRxText.setText("0");
        this.mPerText.setText("0");
        this.mGoBtn.setEnabled(false);
    }

    private void onClickBtnRxStop() {
        long i4RxPer = -1;
        long[] i4Rx = new long[2];
        long[] u4Value = new long[1];
        this.mHandler.removeMessages(2);
        for (int i = 0; i < 100; i++) {
            EMWifi.setATParam(1, 0);
            EMWifi.getATParam(1, u4Value);
            if (u4Value[0] == 0) {
                break;
            }
            SystemClock.sleep(10);
        }
        EMWifi.getPacketRxStatus(i4Rx, 2);
        try {
            i4RxPer = Long.parseLong(this.mPerText.getText().toString());
        } catch (NumberFormatException e) {
        }
        long i4RxCntOk = i4Rx[0] - this.i4Init[0];
        long i4RxCntFcsErr = i4Rx[1] - this.i4Init[1];
        if (i4RxCntFcsErr + i4RxCntOk != 0) {
            i4RxPer = (100 * i4RxCntFcsErr) / (i4RxCntFcsErr + i4RxCntOk);
        }
        this.mFCSText.setText(String.valueOf(i4RxCntFcsErr));
        this.mRxText.setText(String.valueOf(i4RxCntOk));
        this.mPerText.setText(String.valueOf(i4RxPer));
        this.mGoBtn.setEnabled(true);
    }
}
