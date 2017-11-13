package com.android.engineeringmode.manualtest.modeltest;

import android.app.Activity;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ModelBTSearch extends Activity implements OnCancelListener {
    private static final CharSequence TARGET_DEVICE_NAME_LOWER_CASE = "fqc";
    private SimpleAdapter adapter;
    private ArrayList<Map<String, String>> devices;
    private boolean isToFindAudio;
    OnClickListener judgeClickLisenter = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case 2131493236:
                    ModelBTSearch.this.setResult(2);
                    ModelBTSearch.this.finish();
                    return;
                case 2131493237:
                    ModelBTSearch.this.setResult(3);
                    ModelBTSearch.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private ListView listView;
    private boolean mBlueOpened;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.w("ModelBTSearch", "action = " + action);
            if (action.equals("android.bluetooth.device.action.FOUND")) {
                Log.v("ModelBTSearch", "Receive action ACTION_FOUND");
                String name = intent.getStringExtra("android.bluetooth.device.extra.NAME");
                short rssi = intent.getShortExtra("android.bluetooth.device.extra.RSSI", Short.MIN_VALUE);
                BluetoothClass btClass = (BluetoothClass) intent.getParcelableExtra("android.bluetooth.device.extra.CLASS");
                if (btClass != null) {
                    int devClass = btClass.getMajorDeviceClass();
                    String address = ((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress();
                    Log.w("ModelBTSearch", "in onReceive, address = " + address);
                    if (!ModelBTSearch.this.isToFindAudio) {
                        ModelBTSearch.this.appendNewDevice(name, address, rssi);
                    } else if (devClass == 1024) {
                        ModelBTSearch.this.appendNewDevice(name, address, rssi);
                    }
                    if (name != null && name.toLowerCase(Locale.getDefault()).contains(ModelBTSearch.TARGET_DEVICE_NAME_LOWER_CASE)) {
                        ModelBTSearch.this.mHandler.sendEmptyMessage(1);
                        ModelBTSearch.this.mHandler.sendEmptyMessageDelayed(8, 200);
                    }
                }
                return;
            }
            if (action.equals("android.bluetooth.device.action.NAME_CHANGED")) {
                ModelBTSearch.this.refreshNewDevice(intent.getStringExtra("android.bluetooth.device.extra.NAME"), ((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress());
            } else if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                Log.w("ModelBTSearch", "Receive action BLUETOOTH_STATE_CHANGED_ACTION, state = " + state);
                if (12 == state) {
                    Log.w("ModelBTSearch", "Receive action BLUETOOTH_STATE_CHANGED_ACTION, state = BLUETOOTH_STATE_TURNING_ON");
                    ModelBTSearch.this.mHandler.sendEmptyMessage(0);
                } else if (11 == state) {
                    Log.w("ModelBTSearch", "Receive action BLUETOOTH_STATE_CHANGED_ACTION, state = BLUETOOTH_STATE_TURNING_ON");
                } else if (13 != state) {
                    if (10 == state) {
                        Log.w("ModelBTSearch", "Receive action BLUETOOTH_STATE_CHANGED_ACTION, state = BLUETOOTH_STATE_OFF");
                        ModelBTSearch.this.showShortMessage(ModelBTSearch.this, 2131296624);
                        if (ModelBTSearch.this.mbIsExited) {
                            ModelBTSearch.this.mHandler.sendEmptyMessage(2);
                        }
                    } else if (Integer.MIN_VALUE == state) {
                        Log.w("ModelBTSearch", "Receive action BLUETOOTH_STATE_CHANGED_ACTION, state = BluetoothError.ERROR");
                    }
                }
            } else if (action.equals("android.bluetooth.adapter.action.DISCOVERY_STARTED")) {
                Log.v("ModelBTSearch", "Receive action DISCOVERY_STARTED_ACTION");
            } else if (action.equals("android.bluetooth.adapter.action.DISCOVERY_FINISHED")) {
                Log.v("ModelBTSearch", "Receive action DISCOVERY_COMPLETED_ACTION");
                ModelBTSearch.this.mHandler.sendEmptyMessage(1);
                ModelBTSearch.this.showShortMessage(ModelBTSearch.this, 2131296628);
            }
        }
    };
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (3 == msg.what) {
                if (ModelBTSearch.this.openDevice()) {
                    Log.d("ModelBTSearch", "open Bluetooth device sucessful");
                    return;
                }
                Log.w("ModelBTSearch", "openDevice Failed");
                sendEmptyMessage(5);
            } else if (msg.what == 0) {
                Log.w("ModelBTSearch", "handler message BEGIN_SEARCH");
                ModelBTSearch.this.beginToSearchDevice();
                ModelBTSearch.this.setProgressMessage(2131296629);
            } else if (1 == msg.what) {
                Log.w("ModelBTSearch", "handler message END_SEARCH");
                ModelBTSearch.this.endSearchDevice();
                ModelBTSearch.this.dismissProgress();
                if (ModelBTSearch.this.devices.size() == 0) {
                    Log.w("ModelBTSearch", "Nothing finded!");
                    ModelBTSearch.this.showShortMessage(ModelBTSearch.this, 2131296631);
                } else {
                    ModelBTSearch.this.showShortMessage(ModelBTSearch.this, 2131296628);
                }
            } else if (2 == msg.what) {
                ModelBTSearch.this.dismissProgress();
                ModelBTSearch.this.endActivity();
                Log.w("ModelBTSearch", "handler message END_ACTIVITY");
            } else if (5 == msg.what) {
                Log.w("ModelBTSearch", "handler message OPEN_DEVICE_FAILED, open bluetooth failed");
                ModelBTSearch.this.showShortMessage(ModelBTSearch.this, 2131296620);
                ModelBTSearch.this.mHandler.sendEmptyMessage(2);
                ModelBTSearch.this.setProgress(10);
            } else if (4 == msg.what) {
                ModelBTSearch.this.showShortMessage(ModelBTSearch.this, 2131296619);
            } else if (6 == msg.what) {
                Log.w("ModelBTSearch", "handler message CLOSE_DEVICE_FAILED");
                ModelBTSearch.this.showShortMessage(ModelBTSearch.this, 2131296625);
                ModelBTSearch.this.mHandler.sendEmptyMessage(2);
            } else if (7 == msg.what) {
                ModelBTSearch.this.showShortMessage(ModelBTSearch.this, 2131296623);
            } else if (8 == msg.what) {
                ModelBTSearch.this.passBtTest();
            }
        }
    };
    private ProgressDialog mProgessSearch = null;
    private boolean mbIsExited = false;

    public void onCreate(Bundle icicle) {
        requestWindowFeature(2);
        super.onCreate(icicle);
        setContentView(2130903149);
        setTitle(2131296626);
        setLisentenersForJudgeButtons();
        this.listView = (ListView) findViewById(2131493362);
        this.devices = new ArrayList();
        this.adapter = new SimpleAdapter(this, this.devices, 17367053, new String[]{"name", "address"}, new int[]{16908308, 16908309});
        this.listView.setAdapter(this.adapter);
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
        filter.addAction("android.bluetooth.device.action.NAME_CHANGED");
        registerReceiver(this.mBroadcastReceiver, filter);
        if (getIntent().getIntExtra("audio_device", 0) == 1024) {
            this.isToFindAudio = true;
        }
    }

    private void setLisentenersForJudgeButtons() {
        if (getIntent().getBooleanExtra("model_test", false)) {
            ((ViewStub) findViewById(2131493190)).setVisibility(0);
            Button pass = (Button) findViewById(2131493015);
            pass.setOnClickListener(this.judgeClickLisenter);
            pass.setVisibility(8);
            ((Button) findViewById(2131493236)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493237)).setOnClickListener(this.judgeClickLisenter);
        }
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
        Log.w("ModelBTSearch", "openDevice");
        if (this.mBluetoothAdapter == null) {
            Log.w("ModelBTSearch", "mBluetoothDev = null");
            return false;
        }
        if (this.mBluetoothAdapter.isEnabled()) {
            Log.w("ModelBTSearch", "Bluetooth is opened already");
            showProgress(2131296262, 2131296629);
            this.mHandler.sendEmptyMessage(0);
        } else {
            Log.w("ModelBTSearch", "Bluetooth is not opened mBluetoothAdapter.getState()=" + this.mBluetoothAdapter.getState());
            if (this.mBluetoothAdapter.getState() != 10) {
                Log.w("ModelBTSearch", "Bluetooth is not disable waiting disable");
                this.mHandler.sendEmptyMessageDelayed(3, 200);
            } else {
                Log.w("ModelBTSearch", "Bluetooth enable");
                showProgress(2131296262, 2131296619);
                return this.mBluetoothAdapter.enable();
            }
        }
        return true;
    }

    private boolean closeBluetooth() {
        Log.w("ModelBTSearch", "closeBluetooth");
        if (!this.mBlueOpened && this.mBluetoothAdapter.isEnabled()) {
            return this.mBluetoothAdapter.disable();
        }
        return true;
    }

    private boolean beginToSearchDevice() {
        Log.w("ModelBTSearch", "begin to search the Device");
        return this.mBluetoothAdapter.startDiscovery();
    }

    private void appendNewDevice(String deviceName, String address, short rssi) {
        Log.d("ModelBTSearch", "devicename: " + deviceName + " address:" + address);
        Map<String, String> map = new HashMap();
        map.put("name", deviceName);
        map.put("address", address);
        this.devices.add(map);
        if (this.devices.size() >= 15) {
            this.mBluetoothAdapter.cancelDiscovery();
        }
        this.adapter.notifyDataSetChanged();
    }

    private void refreshNewDevice(String newdeviceName, String newaddress) {
        for (int i = 0; i < this.devices.size(); i++) {
            Map<String, String> map = (Map) this.devices.get(i);
            if (newaddress.equals((String) map.get("address"))) {
                map.remove("name");
                map.put("name", newdeviceName);
                this.adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    private boolean endSearchDevice() {
        Log.w("ModelBTSearch", "endSearchDevice");
        if (!(this.mBlueOpened || closeBluetooth())) {
            this.mHandler.sendEmptyMessage(6);
        }
        return true;
    }

    public void onCancel(DialogInterface dialog) {
        Log.w("ModelBTSearch", "onCancel");
        dismissProgress();
        if (this.mBluetoothAdapter.isDiscovering()) {
            this.mBluetoothAdapter.cancelDiscovery();
            showProgress(2131296262, 2131296628);
        }
    }

    private void endActivity() {
        Log.w("ModelBTSearch", "endActivity");
        finish();
    }

    private void showShortMessage(Context context, int nResId) {
        new Toast(context).setView(getLayoutInflater().inflate(2130903166, null));
        Toast.makeText(context, context.getResources().getString(nResId), 0).show();
    }

    private void showProgress(int nTitleRes, int nMessageRes) {
        dismissProgress();
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

    private void passBtTest() {
        setResult(1);
        finish();
    }
}
