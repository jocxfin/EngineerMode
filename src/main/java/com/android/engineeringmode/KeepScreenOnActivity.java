package com.android.engineeringmode;

import android.app.Activity;
import android.os.Bundle;

public class KeepScreenOnActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
    }
}
