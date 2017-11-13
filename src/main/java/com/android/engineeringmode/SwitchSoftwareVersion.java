package com.android.engineeringmode;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.util.ExternFunction;

import java.io.File;

public class SwitchSoftwareVersion extends Activity implements OnClickListener {
    private final int DELAY_TIME = 1000;
    private final int DETECTION = 2;
    private final int SUCCESS = 1;
    private Button mEuropeButton;
    private ExternFunction mExFunction;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    Log.i("SwitchSoftwareVersion", "register success!");
                    SwitchSoftwareVersion.this.mExFunction.setProductLineTestFlagExtraByte(74, (byte) 0);
                    SwitchSoftwareVersion.this.mExFunction.setProductLineTestFlagExtraByte(75, (byte) 0);
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    if (SwitchSoftwareVersion.isDataFileExist()) {
                        SwitchSoftwareVersion.this.mEuropeButton.setBackgroundColor(-65536);
                        SwitchSoftwareVersion.this.mExFunction.setProductLineTestFlagExtraByte(74, (byte) 2);
                        return;
                    }
                    SwitchSoftwareVersion.this.mEuropeButton.setBackgroundColor(-16711936);
                    SwitchSoftwareVersion.this.mExFunction.setProductLineTestFlagExtraByte(74, (byte) 1);
                    SwitchSoftwareVersion.this.reboot();
                    return;
                default:
                    return;
            }
        }
    };
    private Button mIndiaButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903207);
        this.mEuropeButton = (Button) findViewById(2131493560);
        this.mEuropeButton.setOnClickListener(this);
        this.mIndiaButton = (Button) findViewById(2131493561);
        this.mIndiaButton.setOnClickListener(this);
        this.mExFunction = new ExternFunction(this);
        this.mExFunction.registerOnServiceConnected(this.mHandler, 1, null);
    }

    private void reboot() {
        Editor e = getSharedPreferences("switch_activated", 0).edit();
        e.putBoolean("switch_activated", false);
        e.commit();
        new Thread(new Runnable() {
            public void run() {
                Log.i("SwitchSoftwareVersion", "reboot device...");
                ((PowerManager) SwitchSoftwareVersion.this.getSystemService("power")).reboot(null);
            }
        }).start();
    }

    public static boolean isDataFileExist() {
        File shopFile = new File("/data/app/AmazonShopping/AmazonShopping.apk");
        File kindleFile = new File("/data/app/AmazonKindle/AmazonKindle.apk");
        if (shopFile.exists() && kindleFile.exists()) {
            return true;
        }
        return false;
    }

    public void onClick(View v) {
        if (v.getId() == 2131493560) {
            if (isDataFileExist()) {
                getPackageManager().deletePackage("com.amazon.kindle", null, 0);
                getPackageManager().deletePackage("in.amazon.mShop.android.shopping", null, 0);
                this.mHandler.sendEmptyMessageDelayed(2, 1000);
            }
        } else if (v.getId() == 2131493561) {
            this.mExFunction.setProductLineTestFlagExtraByte(75, (byte) 1);
            reboot();
        }
    }

    protected void onDestroy() {
        if (this.mExFunction != null) {
            this.mExFunction.unregisterOnServiceConnected(this.mHandler);
            this.mExFunction.dispose();
        }
        super.onDestroy();
    }
}
