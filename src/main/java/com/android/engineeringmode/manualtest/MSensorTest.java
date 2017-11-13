package com.android.engineeringmode.manualtest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.android.engineeringmode.KeepScreenOnActivity;

public class MSensorTest extends KeepScreenOnActivity implements SensorEventListener {
    private static String DEGREESYMBOL = "°";
    private int[] mDirectionArray = new int[]{2131297009, 2131297013, 2131297014, 2131297010, 2131297015, 2131297016, 2131297011, 2131297017, 2131297018, 2131297012, 2131297019, 2131297020};
    private Sensor mMSensor;
    private Sensor mOrientationSensor;
    private SensorManager mSensorManager;
    private float[] mValues = new float[3];
    private TextView textDir;
    private TextView textO;
    private TextView textSen;
    private TextView textX;
    private TextView textY;
    private TextView textZ;

    public void onAccuracyChanged(Sensor arg0, int arg1) {
        if (arg0.getType() == 2) {
            this.textSen.setText(String.valueOf(arg1));
        }
    }

    public void onSensorChanged(SensorEvent arg0) {
        if (arg0.sensor.getType() == 2) {
            this.textX.setText(String.valueOf(arg0.values[0]));
            this.textY.setText(String.valueOf(arg0.values[1]));
            this.textZ.setText(String.valueOf(arg0.values[2]));
        } else if (arg0.sensor.getType() == 3) {
            this.textO.setText(((int) arg0.values[0]) + DEGREESYMBOL);
            this.textDir.setText(getOrientationResId(arg0.values[0]));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903163);
        this.textX = (TextView) findViewById(2131493433);
        this.textY = (TextView) findViewById(2131493435);
        this.textZ = (TextView) findViewById(2131493437);
        this.textO = (TextView) findViewById(2131493431);
        this.textSen = (TextView) findViewById(2131493440);
        this.textDir = (TextView) findViewById(2131493439);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mOrientationSensor = this.mSensorManager.getDefaultSensor(3);
        this.mSensorManager.registerListener(this, this.mOrientationSensor, 1);
        this.mMSensor = this.mSensorManager.getDefaultSensor(2);
        this.mSensorManager.registerListener(this, this.mMSensor, 1);
    }

    protected void onResume() {
        super.onStart();
        this.mSensorManager.registerListener(this, this.mOrientationSensor, 1);
        this.mSensorManager.registerListener(this, this.mMSensor, 1);
    }

    protected void onPause() {
        super.onPause();
        this.mSensorManager.unregisterListener(this);
    }

    private String getOrientationResId(float xAngles) {
        int tempAngles = (((int) xAngles) + 360) % 360;
        if (tempAngles % 90 == 0) {
            return getResources().getString(this.mDirectionArray[(tempAngles / 90) * 3]);
        }
        int vAngles;
        int res_id = this.mDirectionArray[(((tempAngles / 90) * 3) + ((tempAngles % 90) / 45)) + 1];
        int temp = tempAngles % 90;
        if (temp > 45) {
            vAngles = 90 - temp;
        } else {
            vAngles = temp;
        }
        return getResources().getString(res_id) + vAngles;
    }
}
