package com.android.engineeringmode.autoaging;

import android.content.Intent;
import android.os.Bundle;

import com.android.engineeringmode.parameter.GlobalParameter;
import com.android.engineeringmode.util.ProcessManager;

public class WifiTest extends BaseTest {
    private static int UID = 1000;
    private Intent mIntent = new Intent();
    private boolean mRunning = false;

    public void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setTitle(2131296415);
        this.mIntent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
    }

    protected void runTest() {
        if (!this.mRunning) {
            startActivityForResult(this.mIntent, GlobalParameter.getExternalRequestCode());
            this.mRunning = true;
        }
    }

    protected void endTest() {
        if (this.mRunning) {
            ProcessManager.killApplicationProcess("com.android.settings", UID);
            this.mRunning = false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GlobalParameter.getExternalRequestCode()) {
            this.mRunning = false;
        }
    }
}
