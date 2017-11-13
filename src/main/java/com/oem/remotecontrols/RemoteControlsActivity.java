package com.oem.remotecontrols;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;
import com.oem.remotecontrols.BluetoothLeService.LocalBinder;

public class RemoteControlsActivity extends Activity {
    public static SimpleKeyService mSimpleKeyService = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private Button mButtonAlertTest = null;
    private Button mButtonLinkTest = null;
    private Button mButtonRed = null;
    private Button mButtonSignalTest = null;
    private Button mButtonStop = null;
    private Button mButtonVersionTest = null;
    private int mCount = 5;
    private boolean mIsConnected = false;
    private boolean mIsEnabled = false;
    public final Handler mMsgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    Log.d("RemoteControlsActivity", "handleMessage() DEVICE_CONNECTED");
                    RemoteControlsActivity.this.mIsConnected = true;
                    RemoteControlsActivity.this.updateButtonStatus();
                    RemoteControlsActivity.this.enableButtons();
                    RemoteControlsActivity.this.mRssiText.setVisibility(0);
                    RemoteControlsActivity.this.registerRssiUpdateWatcher();
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    Log.d("RemoteControlsActivity", "handleMessage() DEVICE_DISCONNECTED");
                    RemoteControlsActivity.this.mIsConnected = false;
                    RemoteControlsActivity.this.mRssiText.setVisibility(4);
                    RemoteControlsActivity.this.updateButtonStatus();
                    RemoteControlsActivity.this.close();
                    return;
                case Light.CHARGE_GREEN_LIGHT /*3*/:
                    Log.d("RemoteControlsActivity", "handleMessage() MSG_RSSI_UPDATE+rssi=" + msg.arg1);
                    int rssi = msg.arg1;
                    RemoteControlsActivity.this.mRssiText.setText(RemoteControlsActivity.this.getResources().getString(2131297287) + rssi);
                    if (RemoteControlsActivity.this.mSingalTest && rssi >= -60) {
                        RemoteControlsActivity.this.mCount = RemoteControlsActivity.this.mCount + 1;
                    }
                    if (RemoteControlsActivity.mSimpleKeyService != null) {
                        RemoteControlsActivity.mSimpleKeyService.readRemoteRssi();
                        return;
                    }
                    return;
                case 4:
                    Log.d("RemoteControlsActivity", "handleMessage() MSG_SIGNAL_TEST Count:" + RemoteControlsActivity.this.mCount);
                    RemoteControlsActivity.this.mSingalTest = false;
                    RemoteControlsActivity.this.mButtonVersionTest.setEnabled(true);
                    RemoteControlsActivity.this.mResuTextView.setVisibility(0);
                    if (RemoteControlsActivity.this.mCount < 2) {
                        RemoteControlsActivity.this.mResuTextView.setText("FAIL");
                        RemoteControlsActivity.this.mResuTextView.setTextColor(-16777216);
                        RemoteControlsActivity.this.mResuTextView.setBackgroundColor(-65536);
                        break;
                    }
                    RemoteControlsActivity.this.mResuTextView.setText("PASS");
                    RemoteControlsActivity.this.mResuTextView.setTextColor(-16777216);
                    RemoteControlsActivity.this.mResuTextView.setBackgroundColor(-16711936);
                    break;
                case 5:
                    Log.d("RemoteControlsActivity", "handleMessage() MSG_STOP_TEST");
                    RemoteControlsActivity.this.mButtonLinkTest.setEnabled(true);
                    return;
                case Light.MAIN_KEY_NORMAL /*6*/:
                    break;
                default:
                    return;
            }
            Bundle data = msg.getData();
            if (data != null) {
                String version = data.getString("version");
                if (version != null) {
                    CharSequence charSequence;
                    Log.d("RemoteControlsActivity", "version:" + version);
                    RemoteControlsActivity.this.mResuTextView.setVisibility(0);
                    RemoteControlsActivity.this.mResuTextView.setBackgroundColor(-16711936);
                    TextView - get3 = RemoteControlsActivity.this.mResuTextView;
                    if (version != null) {
                        charSequence = "version:" + version;
                    } else {
                        charSequence = "read version failed!";
                    }
                    -get3.setText(charSequence);
                }
            }
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                if (state == 12) {
                    RemoteControlsActivity.this.mButtonLinkTest.setEnabled(true);
                } else if (state == 10) {
                    RemoteControlsActivity.this.mButtonLinkTest.setEnabled(false);
                }
            }
        }
    };
    private TextView mResuTextView = null;
    private TextView mRssiText = null;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            RemoteControlsActivity.mSimpleKeyService = (SimpleKeyService) ((LocalBinder) service).getService();
            if (!RemoteControlsActivity.mSimpleKeyService.initialize()) {
                Log.e("RemoteControlsActivity", "Unable to initialize Bluetooth");
                RemoteControlsActivity.this.finish();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            RemoteControlsActivity.mSimpleKeyService = null;
        }
    };
    private boolean mSingalTest = false;
    private boolean mWasBluetoothEnabled = false;

    private void disableButtons() {
        this.mButtonAlertTest.setEnabled(false);
        this.mButtonAlertTest.setClickable(false);
        this.mButtonRed.setEnabled(false);
        this.mButtonStop.setEnabled(false);
        this.mButtonSignalTest.setEnabled(false);
        this.mButtonVersionTest.setEnabled(false);
    }

    private void enableButtons() {
        this.mButtonAlertTest.setEnabled(true);
        this.mButtonAlertTest.setClickable(true);
        this.mButtonRed.setEnabled(true);
        this.mButtonStop.setEnabled(true);
        this.mButtonSignalTest.setEnabled(true);
        this.mButtonVersionTest.setEnabled(true);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903065);
        this.mResuTextView = (TextView) findViewById(2131492972);
        this.mRssiText = (TextView) findViewById(2131492971);
        if (this.mBluetoothAdapter == null) {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService("bluetooth");
            if (bluetoothManager == null) {
                Log.e("RemoteControl", "Can't get bluetoothManager.");
                finish();
                return;
            }
            this.mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        if (this.mBluetoothAdapter != null) {
            this.mIsEnabled = this.mBluetoothAdapter.isEnabled();
        }
        initialButtons();
        disableButtons();
        SimpleKeyService.registerHandler(this.mMsgHandler);
        bindService(new Intent(this, SimpleKeyService.class), this.mServiceConnection, 1);
    }

    private void registerRssiUpdateWatcher() {
        Log.d("RemoteControlsActivity", "E registerRssiUpdateWatcher()");
        if (mSimpleKeyService != null) {
            mSimpleKeyService.readRemoteRssi();
        }
    }

    protected void updateButtonStatus() {
        this.mResuTextView.setVisibility(0);
        if (this.mIsConnected) {
            this.mResuTextView.setText("PASS");
            this.mResuTextView.setTextColor(-16777216);
            this.mResuTextView.setBackgroundColor(-16711936);
            this.mButtonLinkTest.setEnabled(false);
            return;
        }
        this.mResuTextView.setText("FAIL");
        this.mResuTextView.setTextColor(-16777216);
        this.mResuTextView.setBackgroundColor(-65536);
        this.mButtonLinkTest.setEnabled(true);
    }

    private void initialButtons() {
        this.mButtonLinkTest = (Button) findViewById(2131492965);
        this.mButtonLinkTest.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d("RemoteControlsActivity", "connect test button clicked");
                RemoteControlsActivity.this.goScanBLEDevice();
            }
        });
        this.mButtonAlertTest = (Button) findViewById(2131492966);
        this.mButtonAlertTest.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d("RemoteControlsActivity", "buzzer test button clicked");
                if (RemoteControlsActivity.mSimpleKeyService != null) {
                    RemoteControlsActivity.mSimpleKeyService.writeBuzzer();
                } else {
                    Log.e("RemoteControlsActivity", "write buzzer failed!!!");
                }
            }
        });
        this.mButtonRed = (Button) findViewById(2131492967);
        this.mButtonRed.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d("RemoteControlsActivity", "Red test button clicked");
                RemoteControlsActivity.mSimpleKeyService.writeRedLED();
            }
        });
        this.mButtonStop = (Button) findViewById(2131492970);
        this.mButtonStop.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d("RemoteControlsActivity", "stop test");
                RemoteControlsActivity.this.close();
                RemoteControlsActivity.this.mRssiText.setVisibility(4);
                RemoteControlsActivity.this.mResuTextView.setVisibility(4);
                RemoteControlsActivity.this.disableButtons();
            }
        });
        this.mButtonSignalTest = (Button) findViewById(2131492968);
        this.mButtonSignalTest.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RemoteControlsActivity.this.mCount = 0;
                RemoteControlsActivity.this.mSingalTest = true;
                RemoteControlsActivity.this.mButtonVersionTest.setEnabled(false);
                RemoteControlsActivity.this.mResuTextView.setVisibility(4);
                if (RemoteControlsActivity.mSimpleKeyService != null) {
                    RemoteControlsActivity.mSimpleKeyService.readRemoteRssi();
                }
                RemoteControlsActivity.this.mMsgHandler.removeMessages(4);
                RemoteControlsActivity.this.mMsgHandler.sendEmptyMessageDelayed(4, 5000);
            }
        });
        this.mButtonVersionTest = (Button) findViewById(2131492969);
        this.mButtonVersionTest.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String version = "1.0";
                RemoteControlsActivity.mSimpleKeyService.readVersion();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        if (this.mBluetoothAdapter != null) {
            this.mWasBluetoothEnabled = this.mBluetoothAdapter.isEnabled();
            if (!this.mWasBluetoothEnabled) {
                this.mBluetoothAdapter.enable();
            }
        }
        registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
        this.mButtonLinkTest.setEnabled(this.mWasBluetoothEnabled);
    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.mReceiver);
    }

    protected void onDestroy() {
        close();
        SimpleKeyService.unregisterHandler();
        unbindService(this.mServiceConnection);
        if (!(this.mBluetoothAdapter == null || !this.mBluetoothAdapter.enable() || this.mIsEnabled)) {
            this.mBluetoothAdapter.disable();
        }
        this.mBluetoothAdapter = null;
        super.onDestroy();
    }

    public synchronized void close() {
        if (this.mMsgHandler != null) {
            this.mMsgHandler.removeMessages(4);
        }
        if (mSimpleKeyService != null) {
            mSimpleKeyService.close();
            mSimpleKeyService = null;
        }
    }

    private void goScanBLEDevice() {
        Intent intent = new Intent();
        intent.setClass(this, DeviceScanActivity.class);
        intent.setFlags(268435456);
        startActivity(intent);
    }
}
