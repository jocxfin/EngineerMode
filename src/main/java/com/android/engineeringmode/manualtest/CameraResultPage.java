package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.android.engineeringmode.util.ExternFunction;

import java.util.ArrayList;

public class CameraResultPage extends Activity {
    private static final String TAG = CameraResultPage.class;
    private ArrayList<Integer> dataList;
    private TextView data_offX;
    private TextView data_offY;
    private TextView data_onX;
    private TextView data_onY;
    private TextView data_stillX;
    private TextView data_stillY;
    private boolean mConnected = false;
    private ExternFunction mExFunction;
    private Handler mNVHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                CameraResultPage.this.mConnected = true;
                if (!CameraResultPage.this.getIntent().getBooleanExtra("model_test", false)) {
                    return;
                }
                if (CameraResultPage.this.mResult) {
                    CameraResultPage.this.mExFunction.setProductLineTestFlagExtraByte(21, (byte) 1);
                    CameraResultPage.this.setResult(1);
                    CameraResultPage.this.finish();
                    return;
                }
                CameraResultPage.this.mExFunction.setProductLineTestFlagExtraByte(21, (byte) 2);
            }
        }
    };
    private boolean mResult = true;
    private TextView result;
    private int stillBadFrame = 0;

    protected void onCreate(Bundle savedInstanceState) {
        int i;
        super.onCreate(savedInstanceState);
        setContentView(2130903071);
        this.data_stillX = (TextView) findViewById(2131492991);
        this.data_stillY = (TextView) findViewById(2131492992);
        this.data_onX = (TextView) findViewById(2131492993);
        this.data_onY = (TextView) findViewById(2131492994);
        this.data_offX = (TextView) findViewById(2131492995);
        this.data_offY = (TextView) findViewById(2131492996);
        this.result = (TextView) findViewById(2131492997);
        this.data_stillX.setText("Still\nX: ");
        this.data_stillY.setText("Y: ");
        this.data_onX.setText("\nON\nX: ");
        this.data_onY.setText("Y: ");
        this.data_offX.setText("\nOFF\nX: ");
        this.data_offY.setText("Y: ");
        SharedPreferences sp = getSharedPreferences("com.android.engineeringmode_preferences", 0);
        this.data_stillX.append(sp.getString("key_is_ois_stillx_value", "NONE"));
        this.data_stillY.append(sp.getString("key_is_ois_stilly_value", "NONE"));
        this.stillBadFrame = sp.getInt("key_is_ois_still_bad_frame_value", 0);
        Bundle bundle = getIntent().getExtras();
        ArrayList list = bundle.getParcelableArrayList("list");
        String resultText = bundle.getString("result");
        this.dataList = (ArrayList) list.get(0);
        Log.d(TAG, " data len: " + list.size());
        for (i = 0; i < 10; i++) {
            this.data_onX.append(((Integer) this.dataList.get(i)).toString());
            if (i != 9) {
                this.data_onX.append(" | ");
            }
        }
        this.dataList = (ArrayList) list.get(1);
        for (i = 0; i < 10; i++) {
            this.data_onY.append(((Integer) this.dataList.get(i)).toString());
            if (i != 9) {
                this.data_onY.append(" | ");
            }
        }
        this.dataList = (ArrayList) list.get(2);
        for (i = 0; i < 10; i++) {
            this.data_offX.append(((Integer) this.dataList.get(i)).toString());
            if (i != 9) {
                this.data_offX.append(" | ");
            }
        }
        this.dataList = (ArrayList) list.get(3);
        for (i = 0; i < 10; i++) {
            this.data_offY.append(((Integer) this.dataList.get(i)).toString());
            if (i != 9) {
                this.data_offY.append(" | ");
            }
        }
        this.dataList = (ArrayList) list.get(4);
        Log.e(TAG, "stillBadFrame " + this.stillBadFrame + ", data lens " + this.dataList.size() + ", [0] " + this.dataList.get(0) + ", [1] " + this.dataList.get(1));
        this.mExFunction = new ExternFunction(this);
        this.mExFunction.registerOnServiceConnected(this.mNVHandler, 1, null);
        if (resultText.contains("FAIL") || resultText.contains("Check")) {
            this.result.setTextColor(-65536);
            this.mResult = false;
        } else {
            this.result.setTextColor(-16711936);
        }
        if (this.stillBadFrame >= 2 || ((Integer) this.dataList.get(0)).intValue() >= 2 || ((Integer) this.dataList.get(1)).intValue() >= 2) {
            Log.e(TAG, "OIS: invalid data");
            this.result.setText(resultText + "\ninvalid data, Please test again");
            this.result.setTextColor(-65536);
            this.mResult = false;
            return;
        }
        this.result.setText(resultText);
    }

    protected void onDestroy() {
        if (this.mExFunction != null) {
            this.mExFunction.unregisterOnServiceConnected(this.mNVHandler);
            this.mExFunction.dispose();
        }
        super.onDestroy();
    }
}
