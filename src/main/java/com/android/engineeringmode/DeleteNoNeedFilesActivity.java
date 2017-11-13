package com.android.engineeringmode;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DeleteNoNeedFilesActivity extends Activity {
    private static final String ExternalStorageDir = Environment.getExternalStorageDirectory();
    private static String TAG = "DeleteNoNeedFilesActivity";
    private ArrayList<String> filePathList = new ArrayList();
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            DeleteNoNeedFilesActivity.this.deleteFiles();
        }
    };
    private Messenger mMessenger;
    private Handler mResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            boolean result = msg.getData().getBoolean("RESULT_DELETE");
            Log.d(DeleteNoNeedFilesActivity.TAG, "result=" + result);
            if (result) {
                Toast.makeText(DeleteNoNeedFilesActivity.this, DeleteNoNeedFilesActivity.this.getString(2131297431), 0).show();
            } else {
                Toast.makeText(DeleteNoNeedFilesActivity.this, DeleteNoNeedFilesActivity.this.getString(2131297432), 0).show();
            }
            DeleteNoNeedFilesActivity.this.finish();
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            DeleteNoNeedFilesActivity.this.mMessenger = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(DeleteNoNeedFilesActivity.TAG, "onServiceConnected");
            DeleteNoNeedFilesActivity.this.mMessenger = new Messenger(service);
            DeleteNoNeedFilesActivity.this.mHandler.sendEmptyMessage(0);
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initService();
    }

    private void initService() {
        Log.d(TAG, "initService");
        bindService(new Intent("com.oppo.sdcard.command"), this.mServiceConnection, 1);
    }

    public void deleteFiles() {
        this.filePathList.add(ExternalStorageDir + "/Movies/Love comes back.mp4");
        this.filePathList.add(ExternalStorageDir + "/Music/Love comes back.mp3");
        ArrayList list = new ArrayList();
        list.add(this.filePathList);
        sendMessageToService(this.mResultHandler, list);
    }

    private void sendMessageToService(Handler resultHandler, ArrayList path) {
        Message msg = Message.obtain(null, 5);
        msg.replyTo = new Messenger(resultHandler);
        msg.arg1 = 5;
        Bundle data = new Bundle();
        data.putParcelableArrayList("KEY_FILE_PATH", path);
        msg.setData(data);
        try {
            this.mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    protected void onDestroy() {
        Log.d(TAG, "unbindService");
        unbindService(this.mServiceConnection);
        super.onDestroy();
    }
}
