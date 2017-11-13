package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RPMB extends Activity {
    private final String RPMB_ATTK = "dumpsys android.security.keystore --verify_attk_key_pair";
    private final String RPMB_KEY = "dumpsys android.security.keystore --rpmb_check";
    private final String TAG = "RPMB";
    private TextView mRPMB_attk;
    private TextView mRPMB_key;
    private TextView mRPMB_result;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903196);
        this.mRPMB_attk = (TextView) findViewById(2131493531);
        this.mRPMB_key = (TextView) findViewById(2131493532);
        this.mRPMB_result = (TextView) findViewById(2131493533);
        String resRPMB_attk = shellRun("dumpsys android.security.keystore --verify_attk_key_pair");
        String resRPMB_key = shellRun("dumpsys android.security.keystore --rpmb_check");
        Log.d("RPMB", "resRPMB_attk : " + resRPMB_attk + "\nmRPMB_key : " + this.mRPMB_key);
        if (resRPMB_attk.contains("ret: 0")) {
            this.mRPMB_attk.setText(2131297540);
            this.mRPMB_attk.setTextColor(-16711936);
        } else {
            this.mRPMB_attk.setText(2131297541);
            this.mRPMB_attk.setTextColor(-65536);
        }
        if (resRPMB_key.contains("RPMB_KEY_PROVISIONED_AND_OK")) {
            this.mRPMB_key.setText(2131297542);
            this.mRPMB_key.setTextColor(-16711936);
        } else {
            this.mRPMB_key.setText(2131297543);
            this.mRPMB_key.setTextColor(-65536);
        }
        if (resRPMB_key.contains("RPMB_KEY_PROVISIONED_AND_OK") && resRPMB_attk.contains("ret: 0")) {
            this.mRPMB_result.setText("PASS");
            this.mRPMB_result.setTextColor(-16711936);
            return;
        }
        this.mRPMB_result.setText("FAIL");
        this.mRPMB_result.setTextColor(-65536);
    }

    private String shellRun(String command) {
        IOException e;
        InterruptedException e2;
        Throwable th;
        Process process = null;
        BufferedReader bufferedReader = null;
        String result = "";
        try {
            byte[] b = new byte[1024];
            process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (true) {
                try {
                    String line = bufferedReader2.readLine();
                    if (line == null) {
                        break;
                    }
                    Log.e("RPMB", "line:" + line);
                    result = result + line;
                } catch (IOException e3) {
                    e = e3;
                    bufferedReader = bufferedReader2;
                } catch (InterruptedException e4) {
                    e2 = e4;
                    bufferedReader = bufferedReader2;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = bufferedReader2;
                }
            }
            process.waitFor();
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (IOException e5) {
                }
            }
            if (process != null) {
                process.destroy();
            }
            bufferedReader = bufferedReader2;
        } catch (IOException e6) {
            e = e6;
            e.printStackTrace();
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e7) {
                }
            }
            if (process != null) {
                process.destroy();
            }
            return result;
        } catch (InterruptedException e8) {
            e2 = e8;
            try {
                e2.printStackTrace();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e9) {
                    }
                }
                if (process != null) {
                    process.destroy();
                }
                return result;
            } catch (Throwable th3) {
                th = th3;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e10) {
                    }
                }
                if (process != null) {
                    process.destroy();
                }
                throw th;
            }
        }
        return result;
    }
}
