package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.engineeringmode.touchscreen.TouchScreen_ShellExe;

public class ModeTestKeyCheck extends Activity {
    private String TAG = "ModeTestKeyCheck";
    String[] cmd = new String[]{"/system/bin/sh", "-c", "cat /proc/interrupts | grep 1302"};
    private TextView keyCheck;
    private TextView keyCheck_rebootResult;
    private TextView keyCheck_result;

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(2130903129);
        this.keyCheck = (TextView) findViewById(2131493243);
        this.keyCheck_result = (TextView) findViewById(2131493245);
        this.keyCheck_rebootResult = (TextView) findViewById(2131493244);
        int count = getKeyCount();
        this.keyCheck.setText(String.valueOf(count));
        this.keyCheck.setTextColor(-16711936);
        if (count > 2000 || count <= 0) {
            this.keyCheck_result.setText("FAIL");
            this.keyCheck_result.setTextColor(-65536);
            this.keyCheck_rebootResult.setText("Please reboot and test again");
            this.keyCheck_rebootResult.setTextColor(-65536);
            return;
        }
        this.keyCheck_result.setText("PASS");
        this.keyCheck_result.setTextColor(-16711936);
    }

    private int getKeyCount() {
        try {
            TouchScreen_ShellExe.execCommand(this.cmd);
            String result = TouchScreen_ShellExe.getOutput();
            String[] tmp = result.split(",|\\s+");
            int tmp1 = Integer.parseInt(tmp[1]);
            int tmp2 = Integer.parseInt(tmp[2]);
            int tmp3 = Integer.parseInt(tmp[3]);
            int tmp4 = Integer.parseInt(tmp[4]);
            Log.e(this.TAG, " tmp1= " + tmp1 + " tmp2 =" + tmp2 + " tmp3 =" + tmp3 + " tmp4 = " + tmp4 + " result " + result);
            return ((tmp1 + tmp2) + tmp3) + tmp4;
        } catch (Exception e) {
            Log.i(this.TAG, e.toString());
            return 0;
        }
    }
}
