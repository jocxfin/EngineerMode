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
import android.widget.TextView;

import com.oem.util.Feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class OtgTest extends Activity {
    private final String FUSB_PATH = "/sys/class/type-c-fusb301/fusb301/CC_state";
    private final String PTN_PATH = "/sys/class/type-c-ptn5150/ptn5150/CC_state";
    private final String TUSB_PATH = "/sys/class/type-c-tusb320/tusb320/CC_state";
    private final String TYPE_8998_PATH = "/sys/class/power_supply/usb/oem_cc_orientation";
    private int mCurTryTime;
    private int mCurrentTime = 0;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 4096:
                    Message message = Message.obtain(null, 1);
                    message.replyTo = new Messenger(OtgTest.this.mResultHandler);
                    message.arg1 = 1;
                    Bundle data = new Bundle();
                    data.putString("KEY_RENAME_FROM", "/storage/UDiskA");
                    message.setData(data);
                    try {
                        OtgTest.this.mMessenger.send(message);
                        return;
                    } catch (RemoteException e) {
                        Log.e("OtgTest", "Messenger send message failed for exception:" + e.getMessage());
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private Messenger mMessenger;
    private String mResult = "";
    private Handler mResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle res = msg.getData();
            Log.d("OtgTest", "OtgTest mResultHandler recv msg");
            Log.d("OtgTest", "OtgTest mCurrentTime: " + OtgTest.this.mCurrentTime + " mCurTryTime: " + OtgTest.this.mCurTryTime);
            if (res == null || res.getInt("otgresult") <= 0) {
                OtgTest otgTest = OtgTest.this;
                otgTest.mCurTryTime = otgTest.mCurTryTime + 1;
                OtgTest.this.mResultTextView.setTextColor(-65536);
                if (OtgTest.this.mCurTryTime < 7) {
                    OtgTest.this.mResultTextView.setText(String.format(OtgTest.this.getString(2131297275), new Object[]{Integer.valueOf(3)}));
                    OtgTest.this.mHandler.sendEmptyMessageDelayed(4096, 3000);
                    return;
                }
                OtgTest.this.mResultTextView.setTextSize(100.0f);
                OtgTest.this.mResultTextView.setText("FAIL");
                boolean booleanExtra = OtgTest.this.getIntent().getBooleanExtra("model_test", false);
                return;
            }
            if (!OtgTest.this.otgPositiveNegativePlug) {
                otgTest = OtgTest.this;
                otgTest.mCurrentTime = otgTest.mCurrentTime + 1;
            } else if (OtgTest.this.mCurrentTime == 0) {
                OtgTest.this.otgResultFisrt = OtgTest.this.readStringFromFile();
                Log.d("OtgTest", "mCurrentTime = 0, otgResultFisrt : " + OtgTest.this.otgResultFisrt);
                if (OtgTest.this.otgResultFisrt.equals("cc1") || OtgTest.this.otgResultFisrt.equals("cc2")) {
                    otgTest = OtgTest.this;
                    otgTest.mCurrentTime = otgTest.mCurrentTime + 1;
                }
            } else if (OtgTest.this.mCurrentTime == 1) {
                String otgResult = OtgTest.this.readStringFromFile();
                Log.d("OtgTest", "mCurrentTime = 1, otgResult : " + otgResult);
                if ((OtgTest.this.otgResultFisrt.equals("cc1") && otgResult.equals("cc2")) || (OtgTest.this.otgResultFisrt.equals("cc2") && otgResult.equals("cc1"))) {
                    otgTest = OtgTest.this;
                    otgTest.mCurrentTime = otgTest.mCurrentTime + 1;
                }
            }
            if (OtgTest.this.usbMicroB || OtgTest.this.mCurrentTime >= 2) {
                OtgTest.this.mResultTextView.setTextSize(100.0f);
                OtgTest.this.mResultTextView.setText("PASS");
                OtgTest.this.mResultTextView.setTextColor(-16711936);
                if (!OtgTest.this.usbMicroB) {
                    otgTest = OtgTest.this;
                    otgTest.mResult = otgTest.mResult + OtgTest.this.getString(2131297279) + "PASS";
                    OtgTest.this.mResultTextView1.setText(OtgTest.this.mResult);
                    OtgTest.this.mResultTextView1.setTextColor(-16711936);
                }
                if (OtgTest.this.getIntent().getBooleanExtra("model_test", false)) {
                    OtgTest.this.setResult(1);
                    OtgTest.this.finish();
                }
            } else if (OtgTest.this.mCurrentTime == 1) {
                OtgTest.this.mResultTextView.setText(2131297277);
                OtgTest.this.mResultTextView.setTextColor(-16711936);
                OtgTest.this.mResult = OtgTest.this.getString(2131297278) + "PASS\n";
                OtgTest.this.mResultTextView1.setText(OtgTest.this.mResult);
                OtgTest.this.mResultTextView1.setTextColor(-16711936);
                OtgTest.this.mCurTryTime = 0;
                OtgTest.this.mHandler.sendEmptyMessageDelayed(4096, 3000);
            } else if (OtgTest.this.mCurrentTime == 0) {
                Log.d("OtgTest", "mCurrentTime = 0 //disconnect");
                OtgTest.this.mCurTryTime = 0;
                OtgTest.this.mHandler.sendEmptyMessageDelayed(4096, 3000);
            }
        }
    };
    private TextView mResultTextView = null;
    private TextView mResultTextView1 = null;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            OtgTest.this.mMessenger = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            OtgTest.this.mMessenger = new Messenger(service);
            OtgTest.this.mHandler.sendEmptyMessage(4096);
        }
    };
    private boolean otgPositiveNegativePlug = false;
    private String otgResultFisrt = "";
    private boolean usbMicroB = false;

    private String readStringFromFile() {
        IOException e;
        Throwable th;
        File file = new File("/sys/class/type-c-fusb301/fusb301/CC_state");
        if (!file.exists()) {
            file = new File("/sys/class/type-c-tusb320/tusb320/CC_state");
            if (!file.exists()) {
                file = new File("/sys/class/type-c-ptn5150/ptn5150/CC_state");
                if (!file.exists()) {
                    file = new File("/sys/class/power_supply/usb/oem_cc_orientation");
                    if (!file.exists()) {
                        return null;
                    }
                }
            }
        }
        BufferedReader bufferedReader = null;
        String tempString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            try {
                tempString = reader.readLine();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("OtgTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("OtgTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("OtgTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    return tempString;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("OtgTest", "readFileByLines io close exception :" + e122.getMessage());
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
        } catch (IOException e3) {
            e = e3;
            Log.e("OtgTest", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return tempString;
        }
        return tempString;
    }

    private void initService() {
        Log.d("OtgTest", "OtgTest---initService");
        Intent intent = new Intent("com.oppo.sdcard.command");
        intent.setPackage("com.oneplus.sdcardservice");
        bindService(intent, this.mServiceConnection, 1);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903175);
        this.usbMicroB = Feature.isUsbMicroBSupported(this);
        this.otgPositiveNegativePlug = Feature.isOtgPositiveNegativePlugSupported(this);
        if (this.usbMicroB) {
            ((TextView) findViewById(2131493472)).setText(2131297274);
        }
        this.mResultTextView = (TextView) findViewById(2131493329);
        this.mResultTextView1 = (TextView) findViewById(2131493473);
        this.mResultTextView1.setText("");
        this.mCurTryTime = 0;
        initService();
    }

    protected void onPause() {
        super.onPause();
        finish();
    }

    protected void onDestroy() {
        Log.d("OtgTest", "OtgTest---unbindService(mServiceConnection)");
        unbindService(this.mServiceConnection);
        super.onDestroy();
    }
}
