package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class FingerPrintQualityTest extends Activity implements OnClickListener {
    Boolean isPassed = Boolean.valueOf(false);
    private Button mImgTest;
    TextView tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903096);
        this.tv = (TextView) findViewById(2131492997);
        this.mImgTest = (Button) findViewById(2131493083);
        this.mImgTest.setOnClickListener(this);
    }

    public void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
        Calendar now = Calendar.getInstance();
        String time = now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13);
        String content;
        if (this.isPassed.booleanValue()) {
            content = time + "--FingerPrintQualityTest--" + "PASS";
        } else {
            content = time + "--FingerPrintQualityTest--" + "FAIL";
        }
    }

    public void onClick(View v) {
        if (v.getId() == 2131493083) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.setComponent(new ComponentName("com.fingerprints.fingerprintsensortest", "com.fingerprints.fingerprintsensortest.MainActivity"));
            intent.setFlags(805306368);
            if (isActivityAvailable(intent)) {
                getApplicationContext().startActivity(intent);
            }
        }
    }

    private boolean isActivityAvailable(Intent intent) {
        return getPackageManager().resolveActivity(intent, 0) != null;
    }
}
