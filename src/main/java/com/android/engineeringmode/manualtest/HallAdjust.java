package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.util.ExternFunction;

public class HallAdjust extends Activity {
    private ExternFunction mExternFunction;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            StringBuilder sb;
            if (msg.what == 11) {
                sb = new StringBuilder();
                sb.append("THRESHOLD_LOW ==").append(HallAdjust.this.value[1]).append("\n");
                sb.append("THRESHOLD_HIGH == ").append(HallAdjust.this.value[2]).append("\n");
                HallAdjust.this.psCountTextView.setText(sb.toString() + ((String) HallAdjust.this.getText(2131297378)));
                HallAdjust.this.saveAdjustResultToNvram(HallAdjust.this.value);
                Log.i("HallAdjust", "after calibrate_hall");
                HallAdjust.this.readAdjustResultToNvram();
            } else if (msg.what == 0) {
                sb = new StringBuilder();
                sb.append("THRESHOLD_LOW ==").append(HallAdjust.this.value[1]).append("\n");
                sb.append("THRESHOLD_HIGH == ").append(HallAdjust.this.value[2]).append("\n");
                HallAdjust.this.psCountTextView.setText(sb.toString() + ((String) HallAdjust.this.getText(2131297379)));
            } else if (msg.what == 1) {
                HallAdjust.this.adjustAlsps();
            }
        }
    };
    private RotationUtils mRotationUtils;
    private TextView psCountTextView;
    private short[] value;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.psCountTextView = (TextView) LayoutInflater.from(this).inflate(17367043, null);
        this.psCountTextView.setText(2131297377);
        this.psCountTextView.setGravity(17);
        this.psCountTextView.setTextSize(30.0f);
        setContentView(this.psCountTextView);
        this.mExternFunction = new ExternFunction(this);
        this.mExternFunction.registerOnServiceConnected(this.mHandler, 1, null);
        this.mRotationUtils = new RotationUtils();
    }

    protected void onPause() {
        super.onPause();
        this.mHandler.removeMessages(11);
        this.mHandler.removeMessages(0);
    }

    protected void onDestroy() {
        this.mExternFunction.unregisterOnServiceConnected(this.mHandler);
        this.mExternFunction.dispose();
        super.onDestroy();
    }

    private byte[] short2ByteArray(short target) {
        byte[] array = new byte[2];
        for (int i = 0; i < 2; i++) {
            array[i] = (byte) ((target >> (((array.length - i) - 1) * 8)) & Light.MAIN_KEY_MAX);
        }
        return array;
    }

    private short byteArray2Short(byte[] array) {
        short result = (short) 0;
        for (int i = 0; i < 2; i++) {
            result = (short) (((array[i] & Light.MAIN_KEY_MAX) << (((array.length - i) - 1) * 8)) + result);
        }
        return result;
    }

    private void saveAdjustResultToNvram(short[] adjustValue) {
        byte[] buff = this.mExternFunction.getProductLineTestFlag();
        for (int i = 0; i < adjustValue.length; i++) {
            byte[] temp = short2ByteArray(adjustValue[i]);
            System.arraycopy(temp, 0, buff, (temp.length * i) + 3, temp.length);
        }
        this.mExternFunction.setProductLineTestFlag(buff);
    }

    private short[] readAdjustResultToNvram() {
        byte[] buff = this.mExternFunction.getProductLineTestFlag();
        short[] result = new short[3];
        for (int i = 0; i < result.length; i++) {
            temp = new byte[2];
            System.arraycopy(buff, (temp.length * i) + 3, temp, 0, temp.length);
            result[i] = byteArray2Short(temp);
            Log.i("HallAdjust", "result read from is : " + result[i]);
        }
        return result;
    }

    private void adjustAlsps() {
        this.value = this.mRotationUtils.calibrate_hall();
        Log.i("HallAdjust", "before calibrate_hall");
        readAdjustResultToNvram();
        if (this.value[0] == (short) 1) {
            this.mHandler.sendEmptyMessage(11);
        } else {
            this.mHandler.sendEmptyMessage(0);
        }
    }
}
