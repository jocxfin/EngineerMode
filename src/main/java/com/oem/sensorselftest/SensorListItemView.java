package com.oem.sensorselftest;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class SensorListItemView extends LinearLayout {
    private TextView sensorNameView;
    private SensorStatus sensorStatus;
    private TextView sensorStatusView;

    public SensorListItemView(Context context, SensorStatus sensorStatus) {
        super(context);
        setOrientation(1);
        this.sensorStatus = sensorStatus;
        this.sensorNameView = new TextView(context);
        this.sensorNameView.setText(SensorUtilityFunctions.getSensorTypeString(sensorStatus.getSensor()) + ": " + sensorStatus.getSensor().getName());
        addView(this.sensorNameView, new LayoutParams(-1, -2));
        this.sensorStatusView = new TextView(context);
        this.sensorStatusView.setText(sensorStatus.getStatus());
        addView(this.sensorStatusView, new LayoutParams(-1, -2));
        this.sensorStatusView.setTextColor(sensorStatus.getColor());
    }

    public void setStatus(String status, int color) {
        this.sensorStatusView.setText(status);
        this.sensorStatusView.setTextColor(color);
        this.sensorStatus.setStatus(status);
        this.sensorStatus.setColor(color);
    }

    public SensorStatus getSensorStatus() {
        return this.sensorStatus;
    }

    public String getSensorName() {
        return this.sensorNameView.getText().toString();
    }
}
