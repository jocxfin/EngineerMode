package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;

public class VibrateTest extends Activity implements OnSeekBarChangeListener {
    private SeekBar mSeekBar;
    private TextView mTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903220);
        this.mSeekBar = (SeekBar) findViewById(2131493592);
        this.mSeekBar.setOnSeekBarChangeListener(this);
        this.mTextView = (TextView) findViewById(2131493053);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        if (progress == 0) {
            Light.setVibrate(Integer.toString(0));
            this.mTextView.setText("Value:  0");
            return;
        }
        Light.setVibrate(Integer.toString((progress * 8) + 1200));
        this.mTextView.setText("Value:  " + ((progress * 8) + 1200));
    }

    public void onStartTrackingTouch(SeekBar arg0) {
    }

    public void onStopTrackingTouch(SeekBar arg0) {
    }

    protected void onResume() {
        super.onResume();
        Light.setVibrate(Integer.toString(0));
        this.mSeekBar.setProgress(0);
        this.mTextView.setText("Value:  0");
    }

    protected void onPause() {
        super.onPause();
        Light.setVibrate(Integer.toString(0));
    }
}
