package com.android.engineeringmode.manualtest.modeltest;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.engineeringmode.manualtest.OrientationEventListener;

public class ModelGsensorTest extends ModelTest3ItemActivity {
    private View mBottomBar;
    private TextView mGsensorX;
    private TextView mGsensorY;
    private TextView mGsensorZ;
    private OrientationEventListener mOrientationListener;
    private boolean mXDirection;
    private boolean mYDirection;
    private boolean mZDirection;
    private Button mbtnExit;
    private TextView mtvAngle;
    private TextView mtvScreenOrient;
    private TextView mtvSuccess;
    private TextView textX;
    private TextView textY;
    private TextView textZ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903106);
        setTitle(2131296955);
        initResources();
    }

    private void initResources() {
        this.mtvAngle = (TextView) findViewById(2131493129);
        this.mtvScreenOrient = (TextView) findViewById(2131493140);
        this.mtvSuccess = (TextView) findViewById(2131492997);
        this.mGsensorX = (TextView) findViewById(2131493131);
        this.mGsensorY = (TextView) findViewById(2131493134);
        this.mGsensorZ = (TextView) findViewById(2131493137);
        this.textX = (TextView) findViewById(2131493132);
        this.textY = (TextView) findViewById(2131493135);
        this.textZ = (TextView) findViewById(2131493138);
        this.mOrientationListener = new OrientationEventListener(this) {
            public void onOrientationChanged(int orientation, float[] values) {
                ModelGsensorTest.this.mtvAngle.setText(String.valueOf(orientation));
                float x = (float) (Math.round(values[0] * 10.0f) / 10);
                float y = (float) (Math.round(values[1] * 10.0f) / 10);
                float z = (float) (Math.round(values[2] * 10.0f) / 10);
                if (highlyMatch(x) && lowlyMatch(y) && lowlyMatch(z)) {
                    ModelGsensorTest.this.mXDirection = true;
                    ModelGsensorTest.this.mGsensorX.setTextColor(-65536);
                }
                if (lowlyMatch(x) && highlyMatch(y) && lowlyMatch(z)) {
                    ModelGsensorTest.this.mYDirection = true;
                    ModelGsensorTest.this.mGsensorY.setTextColor(-65536);
                }
                if (lowlyMatch(x) && lowlyMatch(y) && highlyMatch(z)) {
                    ModelGsensorTest.this.mZDirection = true;
                    ModelGsensorTest.this.mGsensorZ.setTextColor(-65536);
                }
                ModelGsensorTest.this.textX.setText(String.valueOf(values[0]));
                ModelGsensorTest.this.textY.setText(String.valueOf(values[1]));
                ModelGsensorTest.this.textZ.setText(String.valueOf(values[2]));
                if (ModelGsensorTest.this.mXDirection && ModelGsensorTest.this.mYDirection && ModelGsensorTest.this.mZDirection) {
                    ModelGsensorTest.this.mtvSuccess.setText("Pass");
                    ModelGsensorTest.this.mtvSuccess.setVisibility(0);
                    ModelGsensorTest.this.mtvSuccess.setTextColor(-16711936);
                    disable();
                    ModelGsensorTest.this.setRequestedOrientation(1);
                    ModelGsensorTest.this.onTestPassed();
                }
            }

            boolean lowlyMatch(float x) {
                if (x >= 2.0f || x <= -2.0f) {
                    return false;
                }
                return true;
            }

            boolean highlyMatch(float x) {
                if (Math.abs(x) >= 12.0f || Math.abs(x) <= 8.0f) {
                    return false;
                }
                return true;
            }
        };
        this.mBottomBar = findViewById(2131493141);
        this.mbtnExit = (Button) findViewById(2131493142);
        this.mBottomBar.setVisibility(8);
        this.mbtnExit.setVisibility(8);
    }

    protected void onResume() {
        int i;
        super.onResume();
        this.mOrientationListener.enable();
        TextView textView = this.mtvScreenOrient;
        if (getLayoutOrientation() == 0) {
            i = 2131296959;
        } else {
            i = 2131296960;
        }
        textView.setText(i);
    }

    protected void onPause() {
        super.onPause();
        this.mOrientationListener.disable();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        int i;
        super.onConfigurationChanged(newConfig);
        TextView textView = this.mtvScreenOrient;
        if (getLayoutOrientation() == 0) {
            i = 2131296959;
        } else {
            i = 2131296960;
        }
        textView.setText(i);
    }

    private int getLayoutOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        if (rotation == 0 || rotation == 2) {
            return 1;
        }
        return 0;
    }
}
