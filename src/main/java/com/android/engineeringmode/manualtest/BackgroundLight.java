package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.engineeringmode.functions.Light;

import java.util.Calendar;

public class BackgroundLight extends Activity implements OnClickListener {
    private Boolean mClosePressed = Boolean.valueOf(false);
    private Boolean mOpenPressed = Boolean.valueOf(false);
    private Button mTpClose;
    private Button mTpOpen;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903064);
        this.mTpOpen = (Button) findViewById(2131492963);
        this.mTpClose = (Button) findViewById(2131492964);
        this.mTpOpen.setOnClickListener(this);
        this.mTpClose.setOnClickListener(this);
    }

    protected void onResume() {
        super.onResume();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131492963:
                this.mOpenPressed = Boolean.valueOf(true);
                Light.setElectric(1, Light.MAIN_KEY_MAX);
                return;
            case 2131492964:
                this.mClosePressed = Boolean.valueOf(true);
                Light.setElectric(1, 0);
                return;
            default:
                return;
        }
    }

    protected void onStop() {
        if (this.mOpenPressed.booleanValue() && this.mClosePressed.booleanValue()) {
            Calendar now = Calendar.getInstance();
            String str = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--BackgroundLight--" + "pressed open and close button";
        }
        super.onStop();
    }
}
