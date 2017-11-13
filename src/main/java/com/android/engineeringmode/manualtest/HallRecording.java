package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.android.engineeringmode.util.SaveToExcel;

public class HallRecording extends Activity {
    private ToggleButton BLN_button_recording;
    private SaveToExcel se;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903192);
        this.BLN_button_recording = (ToggleButton) findViewById(2131493506);
        this.BLN_button_recording.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HallRecording.this.se.startSaving();
                } else {
                    HallRecording.this.se.stopSaving();
                }
            }
        });
        this.se = new SaveToExcel();
    }

    protected void onPause() {
        super.onPause();
    }

    public void onStop() {
        Log.w("HallRecording", "onStop");
        super.onStop();
        if (this.BLN_button_recording.isChecked()) {
            this.BLN_button_recording.setChecked(false);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
