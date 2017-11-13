package com.android.engineeringmode;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowEncryptImeiActivity extends Activity {
    private TextView mtvEncryptImeiNumberShow = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903199);
        Log.d("ShowEncryptImeiActivity", "onCreate()");
        this.mtvEncryptImeiNumberShow = (TextView) findViewById(2131493535);
        this.mtvEncryptImeiNumberShow.setText(getEncryptImeiNumber());
    }

    protected final String getEncryptImeiNumber() {
        return "encrypt imei number";
    }
}
