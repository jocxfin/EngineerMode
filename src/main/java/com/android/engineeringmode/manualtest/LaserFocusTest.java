package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class LaserFocusTest extends Activity {
    private TextView mFocus;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903134);
        this.mFocus = (TextView) findViewById(2131493317);
        this.mFocus.setText("Developing");
    }
}
