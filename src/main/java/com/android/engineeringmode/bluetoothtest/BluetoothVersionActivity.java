package com.android.engineeringmode.bluetoothtest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemProperties;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.Log;

public class BluetoothVersionActivity extends Activity {
    private boolean mBlueEnabled;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.w("BluetoothVersionActivity", "onReceive(), action = " + action);
            if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                Log.w("BluetoothVersionActivity", "Receive action BLUETOOTH_STATE_CHANGED_ACTION, state = " + state);
                if (12 == state) {
                    BluetoothVersionActivity.this.updateUI();
                }
            }
        }
    };
    private TextView mtvBtAddress = null;
    private TextView mtvBtChipVersion = null;
    private TextView mtvBtLmpVersion = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903066);
        this.mtvBtChipVersion = (TextView) findViewById(2131492974);
        this.mtvBtLmpVersion = (TextView) findViewById(2131492976);
        this.mtvBtAddress = (TextView) findViewById(2131492977);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBlueEnabled = this.mBluetoothAdapter.isEnabled();
    }

    public String getBTChipVersion() {
        return SystemProperties.get("ro.bluetooth.version", "unknown");
    }

    public String getBTLmpVersion() {
        return "BLLMPVersion";
    }

    public String getAddress() {
        return this.mBluetoothAdapter.getAddress();
    }

    protected void onResume() {
        super.onResume();
        if (this.mBluetoothAdapter.isEnabled()) {
            updateUI();
        } else {
            Toast.makeText(this, 2131296619, 1).show();
            this.mBluetoothAdapter.enable();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        registerReceiver(this.mBroadcastReceiver, filter);
    }

    protected void onPause() {
        Log.w("BluetoothVersionActivity", "onStop()");
        unregisterReceiver(this.mBroadcastReceiver);
        super.onPause();
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (this.mBluetoothAdapter != null && !this.mBlueEnabled && this.mBluetoothAdapter.isEnabled()) {
            this.mBluetoothAdapter.disable();
            Toast.makeText(this, 2131296623, 0).show();
        }
    }

    public void updateUI() {
        this.mtvBtChipVersion.setText(getBTChipVersion());
        this.mtvBtChipVersion.setVisibility(4);
        this.mtvBtLmpVersion.setText(getBTLmpVersion());
        this.mtvBtAddress.setText(getAddress());
    }
}
