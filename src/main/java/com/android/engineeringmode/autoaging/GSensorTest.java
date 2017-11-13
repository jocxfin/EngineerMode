package com.android.engineeringmode.autoaging;

import android.os.Bundle;
import android.widget.TextView;

import com.android.engineeringmode.functions.GSensor;
import com.android.engineeringmode.functions.GSensor.OnChangedListener;

import java.text.DecimalFormat;

public class GSensorTest extends BaseTest implements OnChangedListener {
    private TextView mAngleView;
    private DecimalFormat mFormat = new DecimalFormat();
    private GSensor mGSensor;
    private TextView mXView;
    private TextView mYView;
    private TextView mZView;

    public void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setTitle(2131296955);
        setContentView(2130903050);
        this.mAngleView = (TextView) findViewById(2131492907);
        this.mXView = (TextView) findViewById(2131492908);
        this.mYView = (TextView) findViewById(2131492909);
        this.mZView = (TextView) findViewById(2131492910);
        this.mGSensor = new GSensor(this);
        this.mGSensor.setOnChangedListener(this);
        this.mFormat.setMinimumFractionDigits(5);
        this.mFormat.setMaximumFractionDigits(5);
    }

    protected void runTest() {
        this.mGSensor.enable();
    }

    protected void endTest() {
        this.mGSensor.setOnChangedListener(null);
        this.mGSensor.disable();
    }

    public void onChanged(float angle, float x, float y, float z) {
        this.mAngleView.setText(this.mFormat.format((double) angle));
        this.mXView.setText(this.mFormat.format((double) x));
        this.mYView.setText(this.mFormat.format((double) y));
        this.mZView.setText(this.mFormat.format((double) z));
    }
}
