package com.oem.sensorselftest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;
import com.qualcomm.qti.sensors.core.sensortest.SensorsReg;

public class SensorOffsetTest extends Activity {
    private TextView mTextView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903197);
        this.mTextView = (TextView) findViewById(2131493534);
        int err = SensorsReg.open();
        if (err != 0) {
            Log.d("SensorTest", "Sensor Open failed: " + err);
            return;
        }
        byte[] bArr = null;
        byte[] bArr2 = null;
        byte[] bArr3 = null;
        byte[] bArr4 = null;
        byte[] bArr5 = null;
        byte[] readResult121 = null;
        try {
            bArr = SensorsReg.getRegistryValue(114);
            bArr2 = SensorsReg.getRegistryValue(115);
            bArr3 = SensorsReg.getRegistryValue(118);
            bArr4 = SensorsReg.getRegistryValue(119);
            bArr5 = SensorsReg.getRegistryValue(120);
            readResult121 = SensorsReg.getRegistryValue(121);
        } catch (Exception e) {
            Log.d("SensorTest", "Read item 114 failed!");
        }
        if (bArr != null) {
            this.mTextView.append("PROX NE (114), result is: " + toInt(bArr) + "\n");
        }
        if (bArr2 != null) {
            this.mTextView.append("PROX SW (115), result is: " + toInt(bArr2) + "\n");
        }
        if (bArr3 != null) {
            this.mTextView.append("Gesture UP, result is: " + toInt(bArr3) + "\n");
        }
        if (bArr4 != null) {
            this.mTextView.append("Gesture DOWN, result is: " + toInt(bArr4) + "\n");
        }
        if (bArr5 != null) {
            this.mTextView.append("Gesture LEFT, result is: " + toInt(bArr5) + "\n");
        }
        if (readResult121 != null) {
            this.mTextView.append("Gesture RIGHT, result is: " + toInt(readResult121) + "\n");
        }
        err = SensorsReg.close();
        if (err != 0) {
            Log.d("SensorTest", "Sensor Close failed: " + err);
        }
    }

    public static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = bytes.length - 1; i >= 0; i--) {
            Log.d("SensorTest", "bytes[" + i + "]: " + bytes[i]);
            result |= (bytes[i] & Light.MAIN_KEY_MAX) << (i * 8);
        }
        Log.d("SensorTest", "result: " + result);
        return result;
    }
}
