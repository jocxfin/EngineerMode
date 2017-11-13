package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WriteLogTest extends Activity implements OnClickListener {
    private static String TAG = "WriteLogTest";
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Message message = Message.obtain(null, 3);
            message.arg1 = 3;
            message.replyTo = new Messenger(WriteLogTest.this.mResultHandler);
            Bundle bundle = new Bundle();
            bundle.putInt("type", msg.what);
            message.setData(bundle);
            try {
                WriteLogTest.this.mMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
    private EditText mLogBox;
    private Messenger mMessenger;
    private Button mReadCriticalDataButton;
    private Handler mResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (bundle != null) {
                int type = bundle.getInt("type");
                if (bundle.getBoolean("issdcardok")) {
                    boolean result = bundle.getBoolean("writeLogResult");
                    if (type == 1) {
                        if (result) {
                            Toast.makeText(WriteLogTest.this, "Save_Log_To_SDcard success", 1).show();
                        } else {
                            Toast.makeText(WriteLogTest.this, "Save_Log_To_SDcard failed", 1).show();
                        }
                        String content = bundle.getString("content");
                        if (!content.isEmpty()) {
                            WriteLogTest.this.mLogBox.setText(content);
                        }
                        WriteLogTest.this.mReadCriticalDataButton.setEnabled(true);
                    } else if (type == 2) {
                        if (result) {
                            Toast.makeText(WriteLogTest.this, "Save_Critical_Data_To_SDcard success", 1).show();
                        } else {
                            Toast.makeText(WriteLogTest.this, "Save_Critical_Data_To_SDcard failed", 1).show();
                        }
                        WriteLogTest.this.mSaveButton.setEnabled(true);
                    }
                } else {
                    if (type == 1) {
                        WriteLogTest.this.mReadCriticalDataButton.setEnabled(true);
                    }
                    if (type == 2) {
                        WriteLogTest.this.mSaveButton.setEnabled(true);
                    }
                    Toast.makeText(WriteLogTest.this, "Can not save log as sdcard unmounted", 1).show();
                }
            }
        }
    };
    private Button mSaveButton;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            WriteLogTest.this.mMessenger = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            WriteLogTest.this.mMessenger = new Messenger(service);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903236);
        this.mSaveButton = (Button) findViewById(2131493700);
        this.mSaveButton.setOnClickListener(this);
        this.mReadCriticalDataButton = (Button) findViewById(2131493699);
        this.mReadCriticalDataButton.setOnClickListener(this);
        this.mLogBox = (EditText) findViewById(2131493701);
        this.mLogBox.setHint("show some logs here!");
        initService();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131493699:
                this.mReadCriticalDataButton.setEnabled(false);
                this.mHandler.sendEmptyMessage(1);
                return;
            case 2131493700:
                Log.e(TAG, "log total size:" + 0);
                this.mHandler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(WriteLogTest.this.getApplicationContext(), "no logs,please write something!", 1).show();
                    }
                });
                return;
            default:
                return;
        }
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
}
