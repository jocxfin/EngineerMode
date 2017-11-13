package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RGBSensorTest extends Activity implements SensorEventListener {
    private TextView CCTText;
    private final String TAG = "RGBSensorTest";
    private final int TYPE_RGB_CCT = 33171020;
    private SensorManager mSensorManager;
    private Sensor sensor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903193);
        this.CCTText = (TextView) findViewById(2131493507);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.sensor = this.mSensorManager.getDefaultSensor(33171020);
        this.mSensorManager.registerListener(this, this.sensor, 3);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 33171020) {
            String calParameter = readFileByLines("/persist/sensors/als_rgb_cal_parameter");
            String[] cal = new String[4];
            if (calParameter != null) {
                cal = calParameter.split(",");
            }
            this.CCTText.setText("CCT(k): " + event.values[0] + "\n" + "kx1: " + cal[1] + "\n" + "b: " + cal[2] + "\n" + "k1k2: " + cal[3] + "\n");
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private String readFileByLines(String fileName) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        String tempString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                tempString = reader.readLine();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("RGBSensorTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("RGBSensorTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("RGBSensorTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    return tempString;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("RGBSensorTest", "readFileByLines io close exception :" + e122.getMessage());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = reader;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            Log.e("RGBSensorTest", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return tempString;
        }
        return tempString;
    }

    protected void onDestroy() {
        if (this.sensor != null) {
            this.mSensorManager.unregisterListener(this);
        }
        super.onDestroy();
    }
}
