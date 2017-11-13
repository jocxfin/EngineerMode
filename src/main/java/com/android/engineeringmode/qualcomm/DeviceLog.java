package com.android.engineeringmode.qualcomm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemService;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;

import java.io.File;

public class DeviceLog extends Activity implements OnClickListener, OnCheckedChangeListener {
    public static final String TAG = DeviceLog.class;
    private static final String[] typeRGItems = new String[]{"sim卡", "语音", "协议", "数据", "所有log"};
    private Button mDeviceLogBt;
    private Button mDeviceLogCloseBt;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    Message message = Message.obtain(null, 2);
                    message.replyTo = new Messenger(DeviceLog.this.mResultHandler);
                    message.arg1 = 2;
                    Bundle data = new Bundle();
                    data.putString("KEY_RENAME_FROM", Environment.getExternalStorageDirectory().getPath() + "/diag_logs/Diag.cfg");
                    data.putInt("type", DeviceLog.this.typeRG.getCheckedRadioButtonId());
                    message.setData(data);
                    try {
                        DeviceLog.this.mMessenger.send(message);
                        return;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        return;
                    }
                case Light.CHARGE_RED_LIGHT /*2*/:
                    Log.d(DeviceLog.TAG, "message2");
                    Message message2 = Message.obtain(null, 2);
                    message2.replyTo = new Messenger(DeviceLog.this.mResultHandler2);
                    message2.arg1 = 2;
                    Bundle data2 = new Bundle();
                    data2.putString("KEY_RENAME_FROM", Environment.getExternalStorageDirectory().getPath() + "/diag_logs");
                    data2.putInt("type", 10);
                    message2.setData(data2);
                    try {
                        DeviceLog.this.mMessenger.send(message2);
                        return;
                    } catch (RemoteException e2) {
                        e2.printStackTrace();
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private Messenger mMessenger;
    private Handler mResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle res = msg.getData();
            Log.d(DeviceLog.TAG, "mResultHandler");
            if (res.getBoolean("RESULT_RENAME")) {
                Toast.makeText(DeviceLog.this, DeviceLog.this.getResources().getString(2131297232), 0).show();
                if (DeviceLog.this.isFileExists("/system/bin/diag_mdlog")) {
                    SystemService.start("diag_mdlog_start");
                    return;
                } else {
                    Toast.makeText(DeviceLog.this, "Feture not avaiable as exec file not exists", 0).show();
                    return;
                }
            }
            Toast.makeText(DeviceLog.this, DeviceLog.this.getResources().getString(2131297231), 0).show();
        }
    };
    private Handler mResultHandler2 = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle res = msg.getData();
            Log.d(DeviceLog.TAG, "mResultHandler2");
            if (res.getBoolean("RESULT_RENAME")) {
                Toast.makeText(DeviceLog.this, DeviceLog.this.getResources().getString(2131297234), 0).show();
            } else {
                Toast.makeText(DeviceLog.this, DeviceLog.this.getResources().getString(2131297233), 0).show();
            }
            DeviceLog.this.mRmOldLogBt.setEnabled(true);
        }
    };
    private Button mRmOldLogBt;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            DeviceLog.this.mMessenger = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            DeviceLog.this.mMessenger = new Messenger(service);
        }
    };
    private EditText numberEt;
    private EditText sizeEt;
    private RadioGroup typeRG;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903084);
        setupUI();
        initService();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        if (FileTransactionUtil.readDataFromFile(655360, this) != -1) {
            this.mDeviceLogBt.setEnabled(false);
            this.mDeviceLogCloseBt.setEnabled(true);
            this.mRmOldLogBt.setEnabled(false);
            this.sizeEt.setEnabled(false);
            this.numberEt.setEnabled(false);
            this.typeRG.setVisibility(8);
            return;
        }
        this.mDeviceLogBt.setEnabled(true);
        this.mDeviceLogCloseBt.setEnabled(false);
        this.mRmOldLogBt.setEnabled(true);
        this.sizeEt.setEnabled(true);
        this.numberEt.setEnabled(true);
        this.typeRG.setVisibility(0);
    }

    protected void onStop() {
        super.onStop();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void setupUI() {
        this.mRmOldLogBt = (Button) findViewById(2131493038);
        this.mDeviceLogBt = (Button) findViewById(2131493039);
        this.mDeviceLogCloseBt = (Button) findViewById(2131493040);
        this.sizeEt = (EditText) findViewById(2131493034);
        this.numberEt = (EditText) findViewById(2131493036);
        this.mRmOldLogBt.setOnClickListener(this);
        this.mDeviceLogBt.setOnClickListener(this);
        this.mDeviceLogCloseBt.setOnClickListener(this);
        this.typeRG = (RadioGroup) findViewById(2131493037);
        this.typeRG.setOnCheckedChangeListener(this);
        insertTypes();
        int sizeFromFile = getSizeFromFile();
        int numberFromFile = getNumberFromFile();
        int typeFromFile = getTypeFromFile();
        if (sizeFromFile != -1) {
            this.sizeEt.setText(sizeFromFile + "");
        } else {
            this.sizeEt.setText("1000");
        }
        if (numberFromFile != -1) {
            this.numberEt.setText(numberFromFile + "");
        } else {
            this.numberEt.setText("5");
        }
        if (typeFromFile != -1) {
            ((RadioButton) this.typeRG.getChildAt(typeFromFile)).setChecked(true);
        } else {
            ((RadioButton) this.typeRG.getChildAt(4)).setChecked(true);
        }
    }

    private void insertTypes() {
        this.typeRG.removeAllViews();
        for (int i = 0; i < typeRGItems.length; i++) {
            RadioButton typeButton = new RadioButton(this);
            typeButton.setChecked(false);
            typeButton.setText(typeRGItems[i]);
            typeButton.setId(i);
            if (i != typeRGItems.length - 1) {
                typeButton.setVisibility(8);
            }
            this.typeRG.addView(typeButton);
        }
        this.typeRG.invalidate();
    }

    private int getSizeFromFile() {
        Log.i(TAG, "get size from file");
        return FileTransactionUtil.readDataFromFile(655361, this);
    }

    private int getNumberFromFile() {
        Log.i(TAG, "get number from file");
        return FileTransactionUtil.readDataFromFile(655362, this);
    }

    private int getTypeFromFile() {
        Log.i(TAG, "get type from file");
        return FileTransactionUtil.readDataFromFile(655363, this);
    }

    private boolean checkSizeFormat() {
        if (this.sizeEt == null || TextUtils.isEmpty(this.sizeEt.getText().toString().trim())) {
            return false;
        }
        return true;
    }

    private boolean checkNumberFormat() {
        if (this.numberEt == null || TextUtils.isEmpty(this.numberEt.getText().toString().trim())) {
            return false;
        }
        return true;
    }

    private void writeSizeToFile() {
        if (checkSizeFormat()) {
            String str;
            if (this.sizeEt.getText().toString().trim().equals(getString(2131297257))) {
                str = "-1";
            } else {
                str = this.sizeEt.getText().toString().trim();
            }
            FileTransactionUtil.writeDataToFile(655361, Integer.parseInt(str), this);
            return;
        }
        Toast.makeText(this, getString(2131297261), 0).show();
    }

    private void writeNumberToFile() {
        if (checkNumberFormat()) {
            String str;
            if (this.numberEt.getText().toString().trim().equals(getString(2131297258))) {
                str = "-1";
            } else {
                str = this.numberEt.getText().toString().trim();
            }
            FileTransactionUtil.writeDataToFile(655362, Integer.parseInt(str), this);
            return;
        }
        Toast.makeText(this, getString(2131297262), 0).show();
    }

    private void writeTypeToFile() {
        FileTransactionUtil.writeDataToFile(655363, this.typeRG.getCheckedRadioButtonId(), this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131493038:
                this.mHandler.sendEmptyMessageDelayed(2, 2000);
                this.mRmOldLogBt.setEnabled(false);
                return;
            case 2131493039:
                writeSizeToFile();
                this.sizeEt.setEnabled(false);
                writeNumberToFile();
                this.numberEt.setEnabled(false);
                writeTypeToFile();
                this.typeRG.setVisibility(8);
                FileTransactionUtil.writeDataToFile(655360, 1, this);
                this.mDeviceLogBt.setEnabled(false);
                this.mDeviceLogCloseBt.setEnabled(true);
                this.mRmOldLogBt.setEnabled(false);
                this.mHandler.sendEmptyMessageDelayed(1, 2000);
                return;
            case 2131493040:
                Toast.makeText(this, "device_log_close", 0).show();
                this.mDeviceLogCloseBt.setEnabled(false);
                this.mDeviceLogBt.setEnabled(true);
                this.mRmOldLogBt.setEnabled(true);
                this.sizeEt.setEnabled(true);
                this.numberEt.setEnabled(true);
                this.typeRG.setVisibility(0);
                FileTransactionUtil.writeDisableMsg(this);
                if (isFileExists("/system/bin/diag_mdlog")) {
                    SystemService.start("diag_mdlog_stop");
                    return;
                } else {
                    Toast.makeText(this, "Feture not avaiable as exec file not exists", 0).show();
                    return;
                }
            default:
                return;
        }
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
    }

    private void initService() {
        Log.d(TAG, "DeviceLog---initService");
        Intent intent = new Intent("com.oppo.sdcard.command");
        intent.setPackage("com.oneplus.sdcardservice");
        bindService(intent, this.mServiceConnection, 1);
    }

    protected void onDestroy() {
        Log.d(TAG, "DeviceLog---unbindService(mServiceConnection)");
        unbindService(this.mServiceConnection);
        super.onDestroy();
    }

    private boolean isFileExists(String filePath) {
        return new File(filePath).exists();
    }
}
