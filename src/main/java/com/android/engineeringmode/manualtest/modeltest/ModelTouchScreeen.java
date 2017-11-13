package com.android.engineeringmode.manualtest.modeltest;

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

public class ModelTouchScreeen extends ModelTest3ItemActivity {
    private final String TAG = "ModelTouchScreeen";
    private String capVal = null;
    private boolean mPassTest = false;
    private int maxValue = 2300;
    private int minValue = 350;
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            boolean leftPassed = false;
            boolean rightPassed = false;
            boolean mainPassed = false;
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
                String[] tmp = ModelTouchScreeen.this.readStringFromFile("proc/s1302/touchkey_baseline_test").split("\n");
                Log.d("ModelTouchScreeen", tmp[0] + "\n" + tmp[1]);
                indexL = tmp[0].indexOf("[");
                indexR = tmp[1].indexOf("[");
                left = tmp[0].substring(5, indexL);
                right = tmp[1].substring(6, indexR);
                Log.d("ModelTouchScreeen", left + "\n" + right);
                leftVal = Integer.parseInt(left);
                rightVal = Integer.parseInt(right);
                leftResult = "left:" + left + "[" + ModelTouchScreeen.this.minValue + "," + ModelTouchScreeen.this.maxValue + "]";
                rightResult = "right:" + right + "[" + ModelTouchScreeen.this.minValue + "," + ModelTouchScreeen.this.maxValue + "]";
                leftText = (TextView) ModelTouchScreeen.this.findViewById(2131492926);
                rightText = (TextView) ModelTouchScreeen.this.findViewById(2131492927);
                if (leftVal <= ModelTouchScreeen.this.minValue || leftVal >= ModelTouchScreeen.this.maxValue) {
                    leftText.setTextColor(-65536);
                    leftText.setText(leftResult + " FAIL!!!\n");
                } else {
                    leftText.setTextColor(-16711936);
                    leftText.setText(leftResult + " PASS!!!\n");
                    leftPassed = true;
                }
                if (rightVal <= ModelTouchScreeen.this.minValue || rightVal >= ModelTouchScreeen.this.maxValue) {
                    rightText.setTextColor(-65536);
                    rightText.setText(rightResult + " FAIL!!!\n");
                } else {
                    rightText.setTextColor(-16711936);
                    rightText.setText(rightResult + " PASS!!!\n");
                    rightPassed = true;
                }
            } else if (new File("/proc/touchpanel/baseline_test").exists()) {
                indexL = ModelTouchScreeen.this.capVal.indexOf("=");
                int indexM = ModelTouchScreeen.this.capVal.indexOf(",");
                indexR = ModelTouchScreeen.this.capVal.indexOf("ht:");
                left = ModelTouchScreeen.this.capVal.substring(indexL + 1, indexM);
                right = ModelTouchScreeen.this.capVal.substring(indexR + 3, ModelTouchScreeen.this.capVal.length());
                Log.d("ModelTouchScreeen", left + "\n" + right);
                leftVal = Integer.parseInt(left);
                rightVal = Integer.parseInt(right);
                leftResult = "left:" + left + "[" + ModelTouchScreeen.this.minValue + "," + ModelTouchScreeen.this.maxValue + "]";
                rightResult = "right:" + right + "[" + ModelTouchScreeen.this.minValue + "," + ModelTouchScreeen.this.maxValue + "]";
                leftText = (TextView) ModelTouchScreeen.this.findViewById(2131492926);
                rightText = (TextView) ModelTouchScreeen.this.findViewById(2131492927);
                if (leftVal <= ModelTouchScreeen.this.minValue || leftVal >= ModelTouchScreeen.this.maxValue) {
                    leftText.setTextColor(-65536);
                    leftText.setText(leftResult + " FAIL!!!\n");
                } else {
                    leftText.setTextColor(-16711936);
                    leftText.setText(leftResult + " PASS!!!\n");
                    leftPassed = true;
                }
                if (rightVal <= ModelTouchScreeen.this.minValue || rightVal >= ModelTouchScreeen.this.maxValue) {
                    rightText.setTextColor(-65536);
                    rightText.setText(rightResult + " FAIL!!!\n");
                } else {
                    rightText.setTextColor(-16711936);
                    rightText.setText(rightResult + " PASS!!!\n");
                    rightPassed = true;
                }
            } else {
                rightPassed = true;
                leftPassed = true;
            }
            if (msg.arg1 == 1) {
                ModelTouchScreeen.this.tx.setTextColor(-16711936);
                ModelTouchScreeen.this.tx.setText("PASS!!!\n");
                mainPassed = true;
            } else {
                ModelTouchScreeen.this.tx.setTextColor(-65536);
                ModelTouchScreeen.this.tx.setText("FAIL!!!\n");
            }
            if (leftPassed && rightPassed && mainPassed) {
                ModelTouchScreeen.this.onTestPassed();
            }
        }
    };
    private TextView product_id;
    private TextView tx;

    public void onCreate(Bundle savedInstanceState) {
        Exception e;
        String strProductName;
        super.onCreate(savedInstanceState);
        setContentView(2130903158);
        setTitle(2131297028);
        this.tx = (TextView) findViewById(2131493378);
        this.product_id = (TextView) findViewById(2131493569);
        this.tx.setText("Plase don`t touch,Testing...");
        this.mPassTest = readFileByLines("/proc/touchpanel/baseline_test") == 1;
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                if (ModelTouchScreeen.this.mPassTest) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                ModelTouchScreeen.this.myHandler.sendMessageDelayed(msg, 2000);
            }
        }.start();
        int tempchar = -1;
        try {
            Reader reader = new InputStreamReader(new FileInputStream(new File("/proc/touchpanel/vendor_id")));
            try {
                tempchar = reader.read();
                Log.d("ModelTouchScreeen", "" + tempchar);
                reader.close();
                Reader reader2 = reader;
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                strProductName = getResources().getString(2131297045);
                tempchar -= 48;
                if (tempchar != -1) {
                    strProductName = strProductName + getResources().getString(2131297044);
                } else if (tempchar != 1) {
                    strProductName = strProductName + getResources().getString(2131297037);
                } else if (tempchar != 2) {
                    strProductName = strProductName + getResources().getString(2131297038);
                } else if (tempchar != 3) {
                    strProductName = strProductName + getResources().getString(2131297039);
                } else if (tempchar != 4) {
                    strProductName = strProductName + getResources().getString(2131297040);
                } else if (tempchar != 5) {
                    strProductName = strProductName + getResources().getString(2131297041);
                } else if (tempchar != 6) {
                    strProductName = strProductName + getResources().getString(2131297042);
                } else if (tempchar == 7) {
                    strProductName = strProductName + getResources().getString(2131297043);
                }
                this.product_id.setText(strProductName);
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            strProductName = getResources().getString(2131297045);
            tempchar -= 48;
            if (tempchar != -1) {
                strProductName = strProductName + getResources().getString(2131297044);
            } else if (tempchar != 1) {
                strProductName = strProductName + getResources().getString(2131297037);
            } else if (tempchar != 2) {
                strProductName = strProductName + getResources().getString(2131297038);
            } else if (tempchar != 3) {
                strProductName = strProductName + getResources().getString(2131297039);
            } else if (tempchar != 4) {
                strProductName = strProductName + getResources().getString(2131297040);
            } else if (tempchar != 5) {
                strProductName = strProductName + getResources().getString(2131297041);
            } else if (tempchar != 6) {
                strProductName = strProductName + getResources().getString(2131297042);
            } else if (tempchar == 7) {
                strProductName = strProductName + getResources().getString(2131297043);
            }
            this.product_id.setText(strProductName);
        }
        strProductName = getResources().getString(2131297045);
        tempchar -= 48;
        if (tempchar != -1) {
            strProductName = strProductName + getResources().getString(2131297044);
        } else if (tempchar != 1) {
            strProductName = strProductName + getResources().getString(2131297037);
        } else if (tempchar != 2) {
            strProductName = strProductName + getResources().getString(2131297038);
        } else if (tempchar != 3) {
            strProductName = strProductName + getResources().getString(2131297039);
        } else if (tempchar != 4) {
            strProductName = strProductName + getResources().getString(2131297040);
        } else if (tempchar != 5) {
            strProductName = strProductName + getResources().getString(2131297041);
        } else if (tempchar != 6) {
            strProductName = strProductName + getResources().getString(2131297042);
        } else if (tempchar == 7) {
            strProductName = strProductName + getResources().getString(2131297043);
        }
        this.product_id.setText(strProductName);
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
                        Log.e("ModelTouchScreeen", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("ModelTouchScreeen", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("ModelTouchScreeen", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    return tempString;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("ModelTouchScreeen", "readFileByLines io close exception :" + e122.getMessage());
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
            Log.e("ModelTouchScreeen", "readFileByLines io exception:" + e.getMessage());
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
                    Log.d("ModelTouchScreeen", tempString);
                    str = str + tempString;
                    if (tempString.contains("left")) {
                        this.capVal = tempString;
                    } else if (tempString.contains("deviceid")) {
                        String[] tmp = tempString.split("=");
                        Log.d("ModelTouchScreeen", tmp[2]);
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
