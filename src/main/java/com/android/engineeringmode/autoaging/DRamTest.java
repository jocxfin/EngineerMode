package com.android.engineeringmode.autoaging;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DRamTest extends BaseTest {
    private boolean mRun = true;

    public void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setTitle(2131296928);
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
                while (DRamTest.this.mRun) {
                    int[] testBuffer = new int[128];
                    int i = 0;
                    while (i < 128) {
                        testBuffer[i] = k % (i + 1);
                        Log.d("DRamTest", "run : " + testBuffer[i]);
                        i++;
                        k++;
                    }
                }
            }
        }).start();
    }
}
