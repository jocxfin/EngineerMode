package com.android.engineeringmode.manualtest;

import android.app.ProgressDialog;
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
import android.os.ServiceManager;
import android.os.storage.IMountService;
import android.os.storage.IMountService.Stub;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.KeepScreenOnActivity;
import com.android.engineeringmode.PowerOff;
import com.android.engineeringmode.util.ExternFunction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MasterClear extends KeepScreenOnActivity implements OnClickListener {
    private final int DELAY_TO_CLEAR = 2000;
    private final int MASTER_CLEAR = 1;
    private boolean auto_start = false;
    private String faidedInfo;
    private String list = "";
    private boolean mConnected = false;
    private ExternFunction mExFunction;
    private String mExternalSdPath;
    private TextView mFormatError;
    private String mInternalSdPath;
    private Button mMastClear;
    private Messenger mMessenger;
    private Handler mNVHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                MasterClear.this.mConnected = true;
            }
        }
    };
    private ProgressDialog mProgressDialog;
    private Handler mResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (MasterClear.this.mProgressDialog != null) {
                MasterClear.this.mProgressDialog.dismiss();
            }
            Bundle bundle = msg.getData();
            Log.e("MasterClear", "bundle:" + bundle.getBoolean("KEY_LOSTFILE"));
            if (bundle != null && bundle.containsKey("KEY_LOSTFILE") && bundle.containsKey("RESULT_RENAME")) {
                Log.e("MasterClear", "KEY_LOSTFILE:" + bundle.getBoolean("KEY_LOSTFILE") + "RESULT_RENAME" + bundle.getBoolean("RESULT_RENAME"));
                if (bundle.getBoolean("KEY_LOSTFILE") || !bundle.getBoolean("RESULT_RENAME")) {
                    Toast.makeText(MasterClear.this, MasterClear.this.getString(2131296950), 5000).show();
                    return;
                }
                if (MasterClear.this.auto_start) {
                    MasterClear.this.setEngineerModeFlag(true);
                }
                MasterClear.this.rotate_logs("/cache/fileAfter8778", 10);
                MasterClear.this.listFile("/cache/fileAfter8778");
                MasterClear.this.recordInCache("/cache/8778");
                PowerOff.delFolder("/persist/time/");
                MasterClear.this.mExFunction.setProductLineTestFlagExtraByte(73, (byte) 1);
                try {
                    RebootWipeUserdata.rebootWipeUserData(MasterClear.this.getApplicationContext(), false, "MasterClearConfirm", "--delete_data");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            MasterClear.this.mMessenger = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            MasterClear.this.mMessenger = new Messenger(service);
            if (MasterClear.this.auto_start) {
                MasterClear.this.onClick(MasterClear.this.mMastClear);
            }
        }
    };
    private TextView mTextView;
    private String successInfo;
    private String unmountInfo;
    private String unmountedInfo;

    private void writeNode(String Path, String value) {
        try {
            FileWriter fr = new FileWriter(new File(Path));
            fr.write(value);
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MasterClear", "write node failed!");
        }
        Log.e("MasterClear", "write node succeed! value=" + value);
    }

    private int readFileByLines(String fileName) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        int result = 0;
        String tempString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                tempString = reader.readLine();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("MasterClear", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("MasterClear", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("MasterClear", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    try {
                        result = Integer.valueOf(tempString).intValue();
                    } catch (NumberFormatException e3) {
                        Log.e("MasterClear", "readFileByLines NumberFormatException:" + e3.getMessage());
                    }
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("MasterClear", "readFileByLines io close exception :" + e122.getMessage());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = reader;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e = e4;
            Log.e("MasterClear", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            result = Integer.valueOf(tempString).intValue();
            return result;
        }
        if (!(tempString == null || "".equals(tempString))) {
            result = Integer.valueOf(tempString).intValue();
        }
        return result;
    }

    private void recordInCache(String Path) {
        File recordFile = new File(Path);
        if (recordFile.exists()) {
            writeNode(Path, (readFileByLines(Path) + 1) + "");
            return;
        }
        try {
            recordFile.createNewFile();
        } catch (IOException e) {
            Log.e("MasterClear", "recordInCache io exception:" + e.getMessage());
        }
        writeNode(Path, "1");
    }

    private void setEngineerModeFlag(boolean enable) {
        File ff = new File("/persist/engineermode_masterclear_flag");
        Log.i("MasterClear", "setEngineerModeFlag enable : " + enable);
        try {
            ff.createNewFile();
            BufferedWriter output = new BufferedWriter(new FileWriter(ff));
            output.write(enable ? "1" : "0");
            output.close();
        } catch (IOException e) {
            Log.e("MasterClear", e.getMessage());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903147);
        this.mMastClear = (Button) findViewById(2131493353);
        this.mMastClear.setOnClickListener(this);
        this.mTextView = (TextView) findViewById(2131493354);
        this.successInfo = getString(2131296944);
        this.faidedInfo = getString(2131296945);
        this.unmountInfo = getString(2131296946);
        this.unmountedInfo = getString(2131296947);
        this.mFormatError = (TextView) findViewById(2131493355);
        this.mExternalSdPath = Environment.getExternalStorageDirectory().toString();
        this.mInternalSdPath = Environment.getExternalStorageDirectory().toString();
        this.mExFunction = new ExternFunction(this);
        this.mExFunction.registerOnServiceConnected(this.mNVHandler, 1, null);
        initService();
        if (getIntent() != null) {
            String extra_au = getIntent().getStringExtra("auto_start");
            if (extra_au != null) {
                boolean z;
                if (extra_au.equals("true")) {
                    z = true;
                } else {
                    z = false;
                }
                this.auto_start = z;
            }
        }
    }

    private void initService() {
        Log.d("MasterClear", "MasterClear---initService");
        Intent intent = new Intent("com.oppo.sdcard.command");
        intent.setPackage("com.oneplus.sdcardservice");
        bindService(intent, this.mServiceConnection, 1);
    }

    protected void onDestroy() {
        unbindService(this.mServiceConnection);
        super.onDestroy();
    }

    private void sendCommandToService() {
        Log.e("MasterClear", "sendCommandToService ");
        Message message = Message.obtain(null, 4);
        message.replyTo = new Messenger(this.mResultHandler);
        message.arg1 = 4;
        Bundle data = new Bundle();
        data.putInt("COMMAND_KEY_TYPE", 1001);
        message.setData(data);
        try {
            this.mMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void ClearKeyInfo() {
        try {
            IMountService mountService = Stub.asInterface(ServiceManager.getService("mount"));
            mountService.setField("SystemLocale", "");
            mountService.setField("PatternVisible", "");
            mountService.setField("PasswordVisible", "");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void listDir(String Path) {
        String[] children = new File(Path).list();
        if (children != null) {
            Log.d("MasterClear", Path + children);
            for (int i = 0; i < children.length; i++) {
                this.list += children[i] + "\n";
                Log.d("MasterClear", this.list + children[i]);
            }
        }
    }

    private void listFile(String dir) {
        this.list = "";
        File file = new File(dir);
        try {
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        listDir("/sdcard/Music/");
        listDir("/sdcard/Pictures/");
        listDir("/sdcard/Movies/");
        writeNode(dir, this.list);
    }

    private void rotate_logs(String dir, int max) {
        for (int i = max - 1; i >= 0; i--) {
            File old_file;
            if (i == 0) {
                old_file = new File(dir);
            } else {
                old_file = new File(dir + "." + Integer.toString(i));
            }
            File new_file = new File(dir + "." + Integer.toString(i + 1));
            if (old_file.exists()) {
                old_file.renameTo(new_file);
            }
        }
    }

    public void onClick(View v) {
        if (v.getId() == 2131493353) {
            rotate_logs("/cache/fileBefore8778", 10);
            listFile("/cache/fileBefore8778");
            v.setEnabled(false);
            this.mProgressDialog = ProgressDialog.show(this, "Please wait...", getString(2131296951), true, false);
            ClearKeyInfo();
            sendCommandToService();
        }
    }
}
