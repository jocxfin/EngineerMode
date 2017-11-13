package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PhoneConnectTest extends Activity {
    private final String CHARGE_PATH = "/sys/class/power_supply/battery/status";
    private final String FUSB_PATH = "/sys/class/type-c-fusb301/fusb301/type";
    private final String PTN_PATH = "/sys/class/type-c-ptn5150/ptn5150/type";
    private final String TUSB_PATH = "/sys/class/type-c-tusb320/tusb320/type";
    private final String TYPE_8998_PATH = "/sys/class/dual_role_usb/otg_default/power_role";
    private int mCurTryTime;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String connectType = PhoneConnectTest.this.readStringFromFile();
                    String chargeType = PhoneConnectTest.this.readFromFile("/sys/class/power_supply/battery/status");
                    Log.d("PhoneConnectTest", "connectType: " + connectType + " chargeType: " + chargeType);
                    if ("FUSB301_SOURCE".equals(connectType) || "source".equals(connectType) || (("FUSB301_TYPE_SINK".equals(connectType) && ("Charging".equals(chargeType) || "Full".equals(chargeType))) || ("sink".equals(connectType) && ("Charging".equals(chargeType) || "Full".equals(chargeType))))) {
                        PhoneConnectTest.this.mResultTextView.setTextSize(100.0f);
                        PhoneConnectTest.this.mResultTextView.setText("PASS");
                        PhoneConnectTest.this.mResultTextView.setTextColor(-16711936);
                        PhoneConnectTest.this.mResultHintView.setVisibility(0);
                        PhoneConnectTest.this.mResultHintView.setTextColor(-16711936);
                        if (PhoneConnectTest.this.getIntent().getBooleanExtra("model_test", false)) {
                            PhoneConnectTest.this.setResult(1);
                            PhoneConnectTest.this.finish();
                            return;
                        }
                        return;
                    }
                    PhoneConnectTest phoneConnectTest = PhoneConnectTest.this;
                    phoneConnectTest.mCurTryTime = phoneConnectTest.mCurTryTime + 1;
                    PhoneConnectTest.this.mResultTextView.setTextColor(-65536);
                    if (PhoneConnectTest.this.mCurTryTime < 7) {
                        PhoneConnectTest.this.mResultTextView.setText(String.format(PhoneConnectTest.this.getString(2131297275), new Object[]{Integer.valueOf(3)}));
                        PhoneConnectTest.this.mHandler.sendEmptyMessageDelayed(0, 3000);
                        return;
                    }
                    PhoneConnectTest.this.mResultTextView.setTextSize(100.0f);
                    PhoneConnectTest.this.mResultTextView.setText("FAIL");
                    if (PhoneConnectTest.this.getIntent().getBooleanExtra("model_test", false)) {
                        PhoneConnectTest.this.setResult(3);
                        PhoneConnectTest.this.finish();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private TextView mResultHintView = null;
    private TextView mResultTextView = null;

    private String readStringFromFile() {
        IOException e;
        Throwable th;
        File file = new File("/sys/class/type-c-fusb301/fusb301/type");
        if (!file.exists()) {
            file = new File("/sys/class/type-c-tusb320/tusb320/type");
            if (!file.exists()) {
                file = new File("/sys/class/type-c-ptn5150/ptn5150/type");
                if (!file.exists()) {
                    file = new File("/sys/class/dual_role_usb/otg_default/power_role");
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
                        Log.e("PhoneConnectTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("PhoneConnectTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("PhoneConnectTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    return tempString;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("PhoneConnectTest", "readFileByLines io close exception :" + e122.getMessage());
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
            Log.e("PhoneConnectTest", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return tempString;
        }
        return tempString;
    }

    private String readFromFile(String fileName) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        String tempString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                tempString = reader.readLine();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("PhoneConnectTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("PhoneConnectTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("PhoneConnectTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    return tempString;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("PhoneConnectTest", "readFileByLines io close exception :" + e122.getMessage());
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
            Log.e("PhoneConnectTest", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return tempString;
        }
        return tempString;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903180);
        this.mResultTextView = (TextView) findViewById(2131493329);
        this.mResultHintView = (TextView) findViewById(2131493375);
        this.mCurTryTime = 0;
        this.mHandler.sendEmptyMessage(0);
    }

    protected void onPause() {
        super.onPause();
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
