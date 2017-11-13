package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;

public class ContactorTest extends Activity {
    private IntentFilter mBatteryFilter;
    private BroadcastReceiver mBatteryStatusReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                int elec = intent.getIntExtra("batterycurrent", 0);
                if (elec > 30000) {
                    elec = -(65536 - elec);
                }
                ContactorTest.this.mElectricity.setText(String.valueOf(elec));
                ContactorTest.this.mElectricity.postInvalidate();
                if (elec > 100 && !ContactorTest.this.mShowPass) {
                    ContactorTest.this.mPass.setVisibility(0);
                    ContactorTest.this.mPass.postInvalidate();
                    ContactorTest.this.mShowPass = true;
                }
            }
        }
    };
    private Button mClose;
    private TextView mElectricity;
    private Handler mHander = new Handler() {
        public void handleMessage(Message msg) {
            if (1 == msg.what) {
                ContactorTest.this.sendBroadcast(new Intent("oppo.intent.action.engineering.mode.update.battery"));
                ContactorTest.this.mHander.sendEmptyMessageDelayed(1, 500);
            }
        }
    };
    private Button mOpen;
    private TextView mPass;
    private volatile boolean mShowPass;

    private class ButtonOnClickListener implements OnClickListener {
        private ButtonOnClickListener() {
        }

        public void onClick(View view) {
            if (ContactorTest.this.mOpen == view) {
                Light.openContactorTest();
                Toast.makeText(ContactorTest.this, 2131297188, 0).show();
                return;
            }
            Light.closeContactorTest();
            Toast.makeText(ContactorTest.this, 2131297189, 0).show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(2131297187);
        setContentView(2130903077);
        this.mOpen = (Button) findViewById(2131493012);
        this.mClose = (Button) findViewById(2131493013);
        this.mOpen.setOnClickListener(new ButtonOnClickListener());
        this.mClose.setOnClickListener(new ButtonOnClickListener());
        this.mElectricity = (TextView) findViewById(2131493014);
        this.mPass = (TextView) findViewById(2131493015);
        this.mBatteryFilter = new IntentFilter();
        this.mBatteryFilter.addAction("android.intent.action.BATTERY_CHANGED");
    }

    protected void onResume() {
        super.onResume();
        this.mPass.setVisibility(4);
        this.mShowPass = false;
        registerReceiver(this.mBatteryStatusReceiver, this.mBatteryFilter);
        this.mHander.sendEmptyMessage(1);
    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.mBatteryStatusReceiver);
        this.mHander.removeMessages(1);
    }

    protected void onStop() {
        super.onStop();
        Light.closeContactorTest();
    }
}
