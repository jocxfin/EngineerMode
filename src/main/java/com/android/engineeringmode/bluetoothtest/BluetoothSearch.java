package com.android.engineeringmode.bluetoothtest;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.engineeringmode.KeepScreenOnPreActivity;

import java.util.Calendar;

public class BluetoothSearch extends KeepScreenOnPreActivity implements OnCancelListener {
    private final int MAX_DEVICE_TO_SEARCH = 15;
    private boolean isToFindAudio;
    OnClickListener judgeClickLisenter = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case 2131493236:
                    BluetoothSearch.this.finish();
                    Intent intent = new Intent("com.android.engineeringmode.bluetoothtest.BluetoothSearch");
                    if (BluetoothSearch.this.isToFindAudio) {
                        intent.putExtra("audio_device", 1024);
                    }
                    BluetoothSearch.this.startActivityForResult(intent, 0);
                    return;
                default:
                    return;
            }
        }
    };
    private boolean mBlueOpened;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.w("BluetoothSearch", "action = " + action);
            if (action.equals("android.bluetooth.device.action.FOUND")) {
                Log.v("BluetoothSearch", "Receive action ACTION_FOUND");
                String name = intent.getStringExtra("android.bluetooth.device.extra.NAME");
                short rssi = intent.getShortExtra("android.bluetooth.device.extra.RSSI", Short.MIN_VALUE);
                String address = ((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress();
                Log.w("BluetoothSearch", "in onReceive, address = " + address);
                if (BluetoothSearch.this.isToFindAudio) {
                    BluetoothClass btClass = (BluetoothClass) intent.getParcelableExtra("android.bluetooth.device.extra.CLASS");
                    if (btClass != null && btClass.getMajorDeviceClass() == 1024) {
                        BluetoothSearch.this.appendNewDevice(name, address, rssi);
                    }
                } else {
                    BluetoothSearch.this.appendNewDevice(name, address, rssi);
                }
            } else if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                Log.w("BluetoothSearch", "Receive action BLUETOOTH_STATE_CHANGED_ACTION, state = " + state);
                if (12 == state) {
                    Log.w("BluetoothSearch", "Receive action BLUETOOTH_STATE_CHANGED_ACTION, state = BLUETOOTH_STATE_TURNING_ON");
                    BluetoothSearch.this.mHandler.sendEmptyMessage(0);
                } else if (11 == state) {
                    Log.w("BluetoothSearch", "Receive action BLUETOOTH_STATE_CHANGED_ACTION, state = BLUETOOTH_STATE_TURNING_ON");
                } else if (13 != state) {
                    if (10 == state) {
                        Log.w("BluetoothSearch", "Receive action BLUETOOTH_STATE_CHANGED_ACTION, state = BLUETOOTH_STATE_OFF");
                        BluetoothSearch.showShortMessage(BluetoothSearch.this, 2131296624);
                        if (BluetoothSearch.this.mbIsExited) {
                            BluetoothSearch.this.mHandler.sendEmptyMessage(2);
                        }
                    } else if (Integer.MIN_VALUE == state) {
                        Log.w("BluetoothSearch", "Receive action BLUETOOTH_STATE_CHANGED_ACTION, state = BluetoothError.ERROR");
                    }
                }
            } else if (action.equals("android.bluetooth.adapter.action.DISCOVERY_STARTED")) {
                Log.v("BluetoothSearch", "Receive action DISCOVERY_STARTED_ACTION");
                BluetoothSearch.this.mbIsStart = true;
            } else if (action.equals("android.bluetooth.adapter.action.DISCOVERY_FINISHED")) {
                Log.v("BluetoothSearch", "Receive action DISCOVERY_COMPLETED_ACTION");
                BluetoothSearch.this.mbIsStart = false;
                BluetoothSearch.this.mHandler.sendEmptyMessage(1);
                BluetoothSearch.showShortMessage(BluetoothSearch.this, 2131296628);
            }
        }
    };
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (3 == msg.what) {
                if (BluetoothSearch.this.openDevice()) {
                    Log.d("BluetoothSearch", "open Bluetooth device sucessful");
                    return;
                }
                Log.w("BluetoothSearch", "openDevice Failed");
                sendEmptyMessage(5);
            } else if (msg.what == 0) {
                Log.w("BluetoothSearch", "handler message BEGIN_SEARCH");
                BluetoothSearch.this.beginToSearchDevice();
                BluetoothSearch.this.setProgressMessage(2131296629);
            } else if (1 == msg.what) {
                Log.w("BluetoothSearch", "handler message END_SEARCH");
                BluetoothSearch.this.endSearchDevice();
                BluetoothSearch.this.dismissProgress();
                if (BluetoothSearch.this.mPrefScreen.getPreferenceCount() == 0) {
                    Log.w("BluetoothSearch", "Nothing finded!");
                    BluetoothSearch.showShortMessage(BluetoothSearch.this, 2131296631);
                } else {
                    BluetoothSearch.showShortMessage(BluetoothSearch.this, 2131296628);
                }
            } else if (2 == msg.what) {
                BluetoothSearch.this.dismissProgress();
                BluetoothSearch.this.endActivity();
                Log.w("BluetoothSearch", "handler message END_ACTIVITY");
            } else if (5 == msg.what) {
                Log.w("BluetoothSearch", "handler message OPEN_DEVICE_FAILED, open bluetooth failed");
                BluetoothSearch.showShortMessage(BluetoothSearch.this, 2131296620);
                BluetoothSearch.this.mHandler.sendEmptyMessage(2);
                BluetoothSearch.this.setProgress(10);
            } else if (4 == msg.what) {
                BluetoothSearch.showShortMessage(BluetoothSearch.this, 2131296619);
            } else if (6 == msg.what) {
                Log.w("BluetoothSearch", "handler message CLOSE_DEVICE_FAILED");
                BluetoothSearch.showShortMessage(BluetoothSearch.this, 2131296625);
                BluetoothSearch.this.mHandler.sendEmptyMessage(2);
            } else if (7 == msg.what) {
                BluetoothSearch.showShortMessage(BluetoothSearch.this, 2131296623);
            }
        }
    };
    private PreferenceScreen mPrefScreen = null;
    private ProgressDialog mProgessSearch = null;
    private int mSearchedDevice;
    private boolean mbIsExited = false;
    private boolean mbIsStart = false;

    public void onCreate(Bundle icicle) {
        requestWindowFeature(2);
        super.onCreate(icicle);
        addPreferencesFromResource(2130968585);
        setContentView(2130903150);
        setLisentenersForJudgeButtons();
        this.mPrefScreen = getPreferenceScreen();
        this.mSearchedDevice = 0;
        this.mBlueOpened = false;
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBluetoothAdapter != null) {
            this.mBlueOpened = this.mBluetoothAdapter.isEnabled();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.DISCOVERY_STARTED");
        filter.addAction("android.bluetooth.device.action.FOUND");
        registerReceiver(this.mBroadcastReceiver, filter);
        if (getIntent().getIntExtra("audio_device", 0) == 1024) {
            this.isToFindAudio = true;
            setTitle(2131296627);
        }
    }

    private void setLisentenersForJudgeButtons() {
        ((Button) findViewById(2131493236)).setOnClickListener(this.judgeClickLisenter);
    }

    protected void onResume() {
        super.onResume();
        this.mHandler.sendEmptyMessageDelayed(3, 200);
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mBroadcastReceiver);
        closeBluetooth();
    }

    private boolean openDevice() {
        Log.w("BluetoothSearch", "openDevice");
        if (this.mBluetoothAdapter == null) {
            Log.w("BluetoothSearch", "mBluetoothDev = null");
            return false;
        } else if (this.mBluetoothAdapter.isEnabled()) {
            Log.w("BluetoothSearch", "Bluetooth is opened already");
            showProgress(2131296262, 2131296629);
            this.mHandler.sendEmptyMessage(0);
            return true;
        } else {
            Log.w("BluetoothSearch", "Bluetooth is not opened");
            showProgress(2131296262, 2131296619);
            return this.mBluetoothAdapter.enable();
        }
    }

    public boolean closeBluetooth() {
        Log.w("BluetoothSearch", "closeBluetooth");
        Calendar now = Calendar.getInstance();
        String time = now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13);
        String content;
        if (this.mPrefScreen.getPreferenceCount() == 0) {
            content = time + "--BluetoothSearch--" + "FAIL";
        } else {
            content = time + "--BluetoothSearch--" + "PASS";
        }
        if (!this.mBlueOpened && this.mBluetoothAdapter.isEnabled()) {
            return this.mBluetoothAdapter.disable();
        }
        return true;
    }

    private boolean beginToSearchDevice() {
        Log.w("BluetoothSearch", "begin to search the Device");
        return this.mBluetoothAdapter.startDiscovery();
    }

    private void appendNewDevice(String deviceName, String address, short rssi) {
        if (this.mPrefScreen.findPreference(address) == null) {
            Log.w("BluetoothSearch", "add the device: " + deviceName);
            Preference pref = new Preference(this);
            pref.setKey(address);
            pref.setTitle(deviceName);
            pref.setSummary(address);
            this.mPrefScreen.addPreference(pref);
            this.mSearchedDevice++;
            if (this.mSearchedDevice >= 15) {
                this.mBluetoothAdapter.cancelDiscovery();
                return;
            }
            return;
        }
        Log.w("BluetoothSearch", "this address is already finded");
    }

    private boolean endSearchDevice() {
        Log.w("BluetoothSearch", "endSearchDevice");
        if (!(this.mBlueOpened || closeBluetooth())) {
            this.mHandler.sendEmptyMessage(6);
        }
        return true;
    }

    public void onCancel(DialogInterface dialog) {
        Log.w("BluetoothSearch", "onCancel");
        dismissProgress();
        if (this.mBluetoothAdapter.isDiscovering()) {
            this.mBluetoothAdapter.cancelDiscovery();
            showProgress(2131296262, 2131296628);
        }
    }

    private void endActivity() {
        Log.w("BluetoothSearch", "endActivity");
        finish();
    }

    public static void showShortMessage(Context context, int nResId) {
        Toast.makeText(context, context.getResources().getString(nResId), 0).show();
    }

    private void showProgress(int nTitleRes, int nMessageRes) {
        this.mProgessSearch = ProgressDialog.show(this, getString(nTitleRes), getString(nMessageRes), false, true, this);
    }

    private void dismissProgress() {
        if (this.mProgessSearch != null) {
            this.mProgessSearch.dismiss();
        }
    }

    private void setProgressMessage(int resID) {
        if (this.mProgessSearch != null) {
            this.mProgessSearch.setMessage(getString(resID));
        }
    }

    public void onBackPressed() {
        if (!(this.mBluetoothAdapter == null || this.mBlueOpened || !this.mBluetoothAdapter.isEnabled())) {
            closeBluetooth();
            this.mbIsExited = true;
            showShortMessage(this, 2131296623);
        }
        super.onBackPressed();
    }
}
