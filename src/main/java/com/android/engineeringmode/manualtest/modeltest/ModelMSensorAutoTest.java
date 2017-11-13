package com.android.engineeringmode.manualtest.modeltest;

import android.content.res.Resources;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.android.engineeringmode.manualtest.MagneticEventListener;

public class ModelMSensorAutoTest extends ModelTest3ItemActivity {
    private int mAccuracy = 3;
    private float mAngleX;
    private float mAngleY;
    private float mAngleZ;
    private MagneticEventListener mMagneticListener;
    private SensorManager mSensorManager;
    private float[] mValues = new float[3];
    private boolean mfirst_time = true;
    Resources res;
    private TextView textDir;
    private TextView textO;
    private TextView textX;
    private TextView textY;
    private TextView textZ;
    private boolean xPass = false;
    private boolean yPass = false;
    private boolean zPass = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903163);
        this.textX = (TextView) findViewById(2131493433);
        this.textY = (TextView) findViewById(2131493435);
        this.textZ = (TextView) findViewById(2131493437);
        this.textO = (TextView) findViewById(2131493431);
        this.textDir = (TextView) findViewById(2131493439);
        this.res = getResources();
        this.mMagneticListener = new MagneticEventListener(this) {
            public void onDistanceChanged(float[] values, int orientation) {
                if (ModelMSensorAutoTest.this.mfirst_time) {
                    ModelMSensorAutoTest.this.mAngleX = values[0];
                    ModelMSensorAutoTest.this.mAngleY = values[1];
                    ModelMSensorAutoTest.this.mAngleZ = values[2];
                    ModelMSensorAutoTest.this.mfirst_time = false;
                }
                ModelMSensorAutoTest.this.textX.setText(String.valueOf(values[0]));
                ModelMSensorAutoTest.this.textY.setText(String.valueOf(values[1]));
                ModelMSensorAutoTest.this.textZ.setText(String.valueOf(values[2]));
                ModelMSensorAutoTest.this.textO.setText(String.valueOf(orientation));
                ModelMSensorAutoTest.this.textDir.setText(ModelMSensorAutoTest.this.getDir(orientation));
                if (!ModelMSensorAutoTest.this.mfirst_time && ((double) Math.abs(values[0] - ModelMSensorAutoTest.this.mAngleX)) > 10.0d) {
                    ModelMSensorAutoTest.this.xPass = true;
                }
                if (!ModelMSensorAutoTest.this.mfirst_time && ((double) Math.abs(values[1] - ModelMSensorAutoTest.this.mAngleY)) > 10.0d) {
                    ModelMSensorAutoTest.this.yPass = true;
                }
                if (!ModelMSensorAutoTest.this.mfirst_time && ((double) Math.abs(values[2] - ModelMSensorAutoTest.this.mAngleZ)) > 10.0d) {
                    ModelMSensorAutoTest.this.zPass = true;
                }
                if (ModelMSensorAutoTest.this.xPass && ModelMSensorAutoTest.this.yPass && ModelMSensorAutoTest.this.zPass) {
                    ModelMSensorAutoTest.this.onTestPassed();
                }
            }
        };
        this.mSensorManager = (SensorManager) getSystemService("sensor");
    }

    protected void onResume() {
        super.onStart();
        this.mMagneticListener.enable();
    }

    protected void onPause() {
        super.onPause();
        this.mMagneticListener.disable();
    }

    private String getDir(int v) {
        switch (v) {
            case 0:
                return this.res.getString(2131297009);
            case 90:
                return this.res.getString(2131297010);
            case 180:
                return this.res.getString(2131297011);
            case 270:
                return this.res.getString(2131297012);
            default:
                if (v > 0 && v <= 45) {
                    return this.res.getString(2131297013) + translateAngle(v);
                } else if (v > 45 && v < 90) {
                    return this.res.getString(2131297014) + translateAngle(v);
                } else if (v > 90 && v <= 135) {
                    return this.res.getString(2131297015) + translateAngle(v);
                } else if (v > 135 && v < 180) {
                    return this.res.getString(2131297016) + translateAngle(v);
                } else if (v > 180 && v <= 225) {
                    return this.res.getString(2131297017) + translateAngle(v);
                } else if (v > 225 && v < 270) {
                    return this.res.getString(2131297018) + translateAngle(v);
                } else if (v > 270 && v <= 315) {
                    return this.res.getString(2131297019) + translateAngle(v);
                } else if (v <= 315 || v >= 360) {
                    return "";
                } else {
                    return this.res.getString(2131297020) + translateAngle(v);
                }
        }
    }

    private int translateAngle(int v) {
        if (v > 0 && v <= 45) {
            return v;
        }
        if (v > 45 && v < 90) {
            return 90 - v;
        }
        if (v > 90 && v <= 135) {
            return v - 90;
        }
        if (v > 135 && v < 180) {
            return 180 - v;
        }
        if (v > 180 && v <= 225) {
            return v - 180;
        }
        if (v > 225 && v < 270) {
            return 270 - v;
        }
        if (v > 270 && v <= 315) {
            return v - 270;
        }
        if (v <= 315 || v >= 360) {
            return v;
        }
        return 360 - v;
    }
}
