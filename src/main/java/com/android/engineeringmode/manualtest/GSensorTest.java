package com.android.engineeringmode.manualtest;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.engineeringmode.autotest.AutoTestItemActivity;

import java.util.Calendar;

public class GSensorTest extends AutoTestItemActivity implements OnClickListener {
    private boolean isPassed = false;
    private View mBottomBar;
    private TextView mGsensorX;
    private TextView mGsensorY;
    private TextView mGsensorZ;
    private OrientationEventListener mOrientationListener;
    private TextView mResult;
    private boolean mXDirection;
    private boolean mYDirection;
    private boolean mZDirection;
    private Button mbtnExit;
    private TextView mtvAngle;
    private TextView mtvScreenOrient;
    private TextView textX;
    private TextView textY;
    private TextView textZ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        setContentView(2130903106);
        setTitle(2131296955);
        this.mtvAngle = (TextView) findViewById(2131493129);
        this.mtvScreenOrient = (TextView) findViewById(2131493140);
        this.mGsensorX = (TextView) findViewById(2131493131);
        this.mGsensorY = (TextView) findViewById(2131493134);
        this.mGsensorZ = (TextView) findViewById(2131493137);
        this.textX = (TextView) findViewById(2131493132);
        this.textY = (TextView) findViewById(2131493135);
        this.textZ = (TextView) findViewById(2131493138);
        this.mResult = (TextView) findViewById(2131492997);
        this.mOrientationListener = new OrientationEventListener(this) {
            public void onOrientationChanged(int orientation, float[] values) {
                GSensorTest.this.mtvAngle.setText(String.valueOf(orientation));
                float x = values[0];
                float y = values[1];
                float z = values[2];
                if (highlyMatch(x) && lowlyMatch(y) && lowlyMatch(z)) {
                    GSensorTest.this.mXDirection = true;
                    GSensorTest.this.mGsensorX.setTextColor(-65536);
                }
                if (lowlyMatch(x) && highlyMatch(y) && lowlyMatch(z)) {
                    GSensorTest.this.mYDirection = true;
                    GSensorTest.this.mGsensorY.setTextColor(-65536);
                }
                if (lowlyMatch(x) && lowlyMatch(y) && highlyMatch(z)) {
                    GSensorTest.this.mZDirection = true;
                    GSensorTest.this.mGsensorZ.setTextColor(-65536);
                }
                GSensorTest.this.textX.setText(String.valueOf(x));
                GSensorTest.this.textY.setText(String.valueOf(y));
                GSensorTest.this.textZ.setText(String.valueOf(z));
                if (GSensorTest.this.mXDirection && GSensorTest.this.mYDirection && GSensorTest.this.mZDirection) {
                    GSensorTest.this.mResult.setText("Pass");
                    GSensorTest.this.mResult.setVisibility(0);
                    GSensorTest.this.isPassed = true;
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
        if (checkIsAutoAging() || checkIsAutoTest()) {
            this.mBottomBar.setVisibility(0);
            this.mbtnExit.setVisibility(0);
            this.mbtnExit.setOnClickListener(this);
            return;
        }
        this.mBottomBar.setVisibility(8);
        this.mbtnExit.setVisibility(8);
    }

    protected void onStart() {
        super.onStart();
        this.mXDirection = false;
        this.mYDirection = false;
        this.mZDirection = false;
        this.mResult.setVisibility(8);
        System.out.println("onstart");
    }

    protected void onResume() {
        super.onResume();
        this.mOrientationListener.enable();
        this.mtvScreenOrient.setText(getLayoutOrientation() == 0 ? 2131296959 : 2131296960);
        System.out.println("onResume");
    }

    protected void onPause() {
        super.onPause();
        this.mOrientationListener.disable();
        System.out.println("onPause");
    }

    protected void onStop() {
        super.onStop();
        Calendar now = Calendar.getInstance();
        String time = now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13);
        String content;
        if (this.isPassed) {
            content = time + "--GSensorTest--" + "PASS";
        } else {
            content = time + "--GSensorTest--" + "FAIL";
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mtvScreenOrient.setText(getLayoutOrientation() == 0 ? 2131296959 : 2131296960);
    }

    public int getLayoutOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        if (rotation == 0 || rotation == 2) {
            return 1;
        }
        return 0;
    }

    public void onClick(View v) {
        if (2131493142 == v.getId()) {
            endActivity();
        }
    }
}
