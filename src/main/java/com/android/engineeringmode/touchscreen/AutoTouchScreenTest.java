package com.android.engineeringmode.touchscreen;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.oem.util.Feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Calendar;

public class AutoTouchScreenTest extends Activity {
    private String capVal = null;
    Boolean isPassed = Boolean.valueOf(false);
    int line;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int indexL;
            int indexR;
            String left;
            String right;
            int leftVal;
            int rightVal;
            String leftResult;
            String rightResult;
            TextView leftText;
            TextView rightText;
            if (Feature.isTouchKeyBaselineTestSupported()) {
                String[] tmp = AutoTouchScreenTest.this.readStringFromFile("proc/s1302/touchkey_baseline_test").split("\n");
                Log.d("AutoTouchScreenTest", tmp[0] + "\n" + tmp[1]);
                indexL = tmp[0].indexOf("[");
                indexR = tmp[1].indexOf("[");
                left = tmp[0].substring(5, indexL);
                right = tmp[1].substring(6, indexR);
                Log.d("AutoTouchScreenTest", left + "\n" + right);
                leftVal = Integer.parseInt(left);
                rightVal = Integer.parseInt(right);
                leftResult = "left:" + left + "[" + AutoTouchScreenTest.this.minValue + "," + AutoTouchScreenTest.this.maxValue + "]";
                rightResult = "right:" + right + "[" + AutoTouchScreenTest.this.minValue + "," + AutoTouchScreenTest.this.maxValue + "]";
                leftText = (TextView) AutoTouchScreenTest.this.findViewById(2131492926);
                rightText = (TextView) AutoTouchScreenTest.this.findViewById(2131492927);
                if (leftVal <= AutoTouchScreenTest.this.minValue || leftVal >= AutoTouchScreenTest.this.maxValue) {
                    leftText.setTextColor(-65536);
                    leftText.setText(leftResult + " FAIL!!!\n");
                } else {
                    leftText.setTextColor(-16711936);
                    leftText.setText(leftResult + " PASS!!!\n");
                }
                if (rightVal <= AutoTouchScreenTest.this.minValue || rightVal >= AutoTouchScreenTest.this.maxValue) {
                    rightText.setTextColor(-65536);
                    rightText.setText(rightResult + " FAIL!!!\n");
                } else {
                    rightText.setTextColor(-16711936);
                    rightText.setText(rightResult + " PASS!!!\n");
                }
            } else if (new File("/proc/touchpanel/baseline_test").exists()) {
                indexL = AutoTouchScreenTest.this.capVal.indexOf("=");
                int indexM = AutoTouchScreenTest.this.capVal.indexOf(",");
                indexR = AutoTouchScreenTest.this.capVal.indexOf("ht:");
                left = AutoTouchScreenTest.this.capVal.substring(indexL + 1, indexM);
                right = AutoTouchScreenTest.this.capVal.substring(indexR + 3, AutoTouchScreenTest.this.capVal.length());
                Log.d("AutoTouchScreenTest", left + "\n" + right);
                leftVal = Integer.parseInt(left);
                rightVal = Integer.parseInt(right);
                leftResult = "left:" + left + "[" + AutoTouchScreenTest.this.minValue + "," + AutoTouchScreenTest.this.maxValue + "]";
                rightResult = "right:" + right + "[" + AutoTouchScreenTest.this.minValue + "," + AutoTouchScreenTest.this.maxValue + "]";
                leftText = (TextView) AutoTouchScreenTest.this.findViewById(2131492926);
                rightText = (TextView) AutoTouchScreenTest.this.findViewById(2131492927);
                if (leftVal <= AutoTouchScreenTest.this.minValue || leftVal >= AutoTouchScreenTest.this.maxValue) {
                    leftText.setTextColor(-65536);
                    leftText.setText(leftResult + " FAIL!!!\n");
                } else {
                    leftText.setTextColor(-16711936);
                    leftText.setText(leftResult + " PASS!!!\n");
                }
                if (rightVal <= AutoTouchScreenTest.this.minValue || rightVal >= AutoTouchScreenTest.this.maxValue) {
                    rightText.setTextColor(-65536);
                    rightText.setText(rightResult + " FAIL!!!\n");
                } else {
                    rightText.setTextColor(-16711936);
                    rightText.setText(rightResult + " PASS!!!\n");
                }
            }
            if (AutoTouchScreenTest.this.line == 1) {
                AutoTouchScreenTest.this.tv.setTextColor(-16711936);
                AutoTouchScreenTest.this.tv.setText(AutoTouchScreenTest.this.result + "\nPASS\n");
                return;
            }
            AutoTouchScreenTest.this.tv.setTextColor(-65536);
            AutoTouchScreenTest.this.tv.setText(AutoTouchScreenTest.this.result + "\nFAIL\n");
        }
    };
    private int maxValue = 2300;
    private int minValue = 350;
    private String result = "";
    TextView tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903213);
        this.tv = (TextView) findViewById(2131493378);
        this.tv.setText("Plase don`t touch,Testing...");
        new Thread(new Runnable() {
            public void run() {
                Exception e;
                String strProductName;
                AutoTouchScreenTest autoTouchScreenTest;
                int tempchar = -1;
                try {
                    Reader reader = new InputStreamReader(new FileInputStream(new File("/proc/touchpanel/vendor_id")));
                    try {
                        tempchar = reader.read();
                        Log.d("AutoTouchScreenTest", "" + tempchar);
                        reader.close();
                        Reader reader2 = reader;
                    } catch (Exception e2) {
                        e = e2;
                        e.printStackTrace();
                        strProductName = AutoTouchScreenTest.this.getResources().getString(2131297045);
                        tempchar -= 48;
                        if (tempchar != -1) {
                            strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297044);
                        } else if (tempchar != 1) {
                            strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297037);
                        } else if (tempchar != 2) {
                            strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297038);
                        } else if (tempchar != 3) {
                            strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297039);
                        } else if (tempchar != 4) {
                            strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297040);
                        } else if (tempchar != 5) {
                            strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297041);
                        } else if (tempchar != 6) {
                            strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297042);
                        } else if (tempchar == 7) {
                            strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297043);
                        }
                        autoTouchScreenTest = AutoTouchScreenTest.this;
                        autoTouchScreenTest.result = autoTouchScreenTest.result + strProductName;
                        AutoTouchScreenTest.this.line = AutoTouchScreenTest.this.readFileByLines("/proc/touchpanel/baseline_test");
                        AutoTouchScreenTest.this.mHandler.sendEmptyMessage(0);
                    }
                } catch (Exception e3) {
                    e = e3;
                    e.printStackTrace();
                    strProductName = AutoTouchScreenTest.this.getResources().getString(2131297045);
                    tempchar -= 48;
                    if (tempchar != -1) {
                        strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297044);
                    } else if (tempchar != 1) {
                        strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297037);
                    } else if (tempchar != 2) {
                        strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297038);
                    } else if (tempchar != 3) {
                        strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297039);
                    } else if (tempchar != 4) {
                        strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297040);
                    } else if (tempchar != 5) {
                        strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297041);
                    } else if (tempchar != 6) {
                        strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297042);
                    } else if (tempchar == 7) {
                        strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297043);
                    }
                    autoTouchScreenTest = AutoTouchScreenTest.this;
                    autoTouchScreenTest.result = autoTouchScreenTest.result + strProductName;
                    AutoTouchScreenTest.this.line = AutoTouchScreenTest.this.readFileByLines("/proc/touchpanel/baseline_test");
                    AutoTouchScreenTest.this.mHandler.sendEmptyMessage(0);
                }
                strProductName = AutoTouchScreenTest.this.getResources().getString(2131297045);
                tempchar -= 48;
                if (tempchar != -1) {
                    strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297044);
                } else if (tempchar != 1) {
                    strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297037);
                } else if (tempchar != 2) {
                    strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297038);
                } else if (tempchar != 3) {
                    strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297039);
                } else if (tempchar != 4) {
                    strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297040);
                } else if (tempchar != 5) {
                    strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297041);
                } else if (tempchar != 6) {
                    strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297042);
                } else if (tempchar == 7) {
                    strProductName = strProductName + AutoTouchScreenTest.this.getResources().getString(2131297043);
                }
                autoTouchScreenTest = AutoTouchScreenTest.this;
                autoTouchScreenTest.result = autoTouchScreenTest.result + strProductName;
                AutoTouchScreenTest.this.line = AutoTouchScreenTest.this.readFileByLines("/proc/touchpanel/baseline_test");
                AutoTouchScreenTest.this.mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    protected void onStop() {
        super.onStop();
        Calendar now = Calendar.getInstance();
        String time = now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13);
        String content;
        if (this.isPassed.booleanValue()) {
            content = time + "--AutoTouchScreenTest--" + "PASS";
        } else {
            content = time + "--AutoTouchScreenTest--" + "FAIL";
        }
    }

    private String readStringFromFile(String fileName) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        String tempString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                tempString = reader.readLine() + "\n" + reader.readLine();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("AutoTouchScreenTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("AutoTouchScreenTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("AutoTouchScreenTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    return tempString;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("AutoTouchScreenTest", "readFileByLines io close exception :" + e122.getMessage());
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
            Log.e("AutoTouchScreenTest", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return tempString;
        }
        return tempString;
    }

    private int readFileByLines(String fileName) {
        IOException e;
        BufferedReader bufferedReader = null;
        int line = 0;
        String str = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            String str2 = null;
            while (true) {
                try {
                    String tempString = reader.readLine();
                    if (tempString == null) {
                        break;
                    }
                    System.out.println("line " + line + ": " + tempString);
                    line++;
                    Log.d("AutoTouchScreenTest", tempString);
                    str = str + tempString;
                    if (tempString.contains("left")) {
                        this.capVal = tempString;
                    } else if (tempString.contains("deviceid")) {
                        String[] tmp = tempString.split("=");
                        Log.d("AutoTouchScreenTest", tmp[2]);
                        if (tmp[2].startsWith("0xeb101")) {
                            this.minValue = 1600;
                            this.maxValue = 3100;
                        }
                    }
                    str2 = tempString;
                } catch (IOException e2) {
                    e = e2;
                    bufferedReader = reader;
                } catch (Throwable th) {
                    bufferedReader = reader;
                }
            }
            if (str2 != null) {
                if (str2.startsWith("0")) {
                    line = 1;
                }
            }
            reader.close();
            if (reader != null) {
                try {
                    reader.close();
                    return line;
                } catch (IOException e3) {
                }
            }
            return 0;
        } catch (IOException e4) {
            e = e4;
            try {
                e.printStackTrace();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                        return line;
                    } catch (IOException e5) {
                        return 0;
                    }
                }
                return 0;
            } catch (Throwable th2) {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                        return line;
                    } catch (IOException e6) {
                        return 0;
                    }
                }
                return 0;
            }
        }
    }
}
