package com.android.engineeringmode.autoaging;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CpuTest extends BaseTest {
    private final String TAG = "CpuTest";
    private boolean mRun = true;

    public void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setTitle(2131296929);
        setContentView(2130903202);
        ((TextView) findViewById(2131493540)).setText(2131296921);
    }

    protected void endTest() {
        this.mRun = false;
    }

    protected void runTest() {
        new Thread(null, new Runnable() {
            public void run() {
                int k = 0;
                while (CpuTest.this.mRun) {
                    int i = 0;
                    while (i < 128) {
                        Log.d("CpuTest", "run : " + (k % (i + 1)));
                        i++;
                        k++;
                    }
                }
            }
        }).start();
    }
}
