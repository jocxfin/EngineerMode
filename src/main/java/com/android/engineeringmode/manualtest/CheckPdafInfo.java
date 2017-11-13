package com.android.engineeringmode.manualtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.manualtest.modeltest.ModelTest3ItemActivity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CheckPdafInfo extends ModelTest3ItemActivity {
    private String CAM_EEPROM_INFO_MTK = "MTK";
    private String CAM_EEPROM_INFO_QCOM = "Qualcomm";
    private String CAM_EEPROM_INFO_SENSOR = "Sensor";
    private String CAM_EEPROM_INFO_UNKNOWN = "UNKNOWN";
    private String PDAF_CALIBRATED_FAILED = "FAIL";
    private String PDAF_CALIBRATED_NOT_SUPPORTED = "NOT_SUPPORTED";
    private String PDAF_CALIBRATED_SUCCESSED = "PASS";
    private int cam_eeprom_info;
    private String cam_eeprom_info_parameter = "";
    TextView cam_eeprom_info_tv;
    private boolean isInModelTest = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                CheckPdafInfo.this.pdaf_info = CheckPdafInfo.this.readFile("/proc/pdaf_info");
                CheckPdafInfo.this.cam_eeprom_info = CheckPdafInfo.this.readFile("/proc/imx298_eeprom_info");
                String pdaf_info_text = CheckPdafInfo.this.getPdafInfo(CheckPdafInfo.this.pdaf_info);
                String cam_eeprom_info_text = CheckPdafInfo.this.getCamEepromInfo(CheckPdafInfo.this.cam_eeprom_info);
                if (CheckPdafInfo.this.pdaf_info == 3) {
                    CheckPdafInfo.this.pdaf_info_tv.setTextColor(-16711936);
                } else {
                    CheckPdafInfo.this.pdaf_info_tv.setTextColor(-65536);
                }
                if (CheckPdafInfo.this.cam_eeprom_info_parameter == CheckPdafInfo.this.CAM_EEPROM_INFO_UNKNOWN || cam_eeprom_info_text.indexOf(CheckPdafInfo.this.cam_eeprom_info_parameter, 0) == -1) {
                    CheckPdafInfo.this.cam_eeprom_info_tv.setTextColor(-65536);
                } else {
                    CheckPdafInfo.this.cam_eeprom_info_tv.setTextColor(-16711936);
                }
                CheckPdafInfo.this.pdaf_info_tv.setText(pdaf_info_text);
                CheckPdafInfo.this.cam_eeprom_info_tv.setText(cam_eeprom_info_text);
                if (CheckPdafInfo.this.isInModelTest && !CheckPdafInfo.this.mHandler.hasMessages(1) && CheckPdafInfo.this.pdaf_info == 3 && CheckPdafInfo.this.cam_eeprom_info_parameter != CheckPdafInfo.this.CAM_EEPROM_INFO_UNKNOWN && cam_eeprom_info_text.indexOf(CheckPdafInfo.this.cam_eeprom_info_parameter, 0) != -1) {
                    CheckPdafInfo.this.mHandler.sendEmptyMessageDelayed(1, 1000);
                }
            } else if (msg.what == 1) {
                CheckPdafInfo.this.onTestPassed();
            }
        }
    };
    private int pdaf_info;
    TextView pdaf_info_tv;
    private String platform = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903179);
        this.pdaf_info_tv = (TextView) findViewById(2131493485);
        this.cam_eeprom_info_tv = (TextView) findViewById(2131493486);
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
        if (getPackageManager().hasSystemFeature("oem.hw.manufacturer.mtk")) {
            this.cam_eeprom_info_parameter = this.CAM_EEPROM_INFO_MTK;
        } else if (getPackageManager().hasSystemFeature("oem.hw.manufacturer.qualcomm")) {
            this.cam_eeprom_info_parameter = this.CAM_EEPROM_INFO_QCOM;
        } else {
            this.cam_eeprom_info_parameter = this.CAM_EEPROM_INFO_UNKNOWN;
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.mHandler != null && !this.mHandler.hasMessages(0)) {
            this.mHandler.sendEmptyMessage(0);
        }
    }

    protected void onStop() {
        super.onStop();
        if (this.mHandler != null) {
            this.mHandler.removeMessages(0);
            this.mHandler.removeMessages(1);
        }
    }

    private String getPdafInfo(int type) {
        String info = this.PDAF_CALIBRATED_NOT_SUPPORTED;
        switch (type) {
            case Light.CHARGE_RED_LIGHT /*2*/:
                return this.PDAF_CALIBRATED_FAILED;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                return this.PDAF_CALIBRATED_SUCCESSED;
            default:
                return this.PDAF_CALIBRATED_NOT_SUPPORTED;
        }
    }

    private String getCamEepromInfo(int type) {
        String info = this.CAM_EEPROM_INFO_UNKNOWN;
        switch (type) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                return this.CAM_EEPROM_INFO_QCOM;
            case Light.CHARGE_RED_LIGHT /*2*/:
                return this.CAM_EEPROM_INFO_MTK;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                return this.CAM_EEPROM_INFO_SENSOR;
            case 4:
                return this.CAM_EEPROM_INFO_MTK + "&" + this.CAM_EEPROM_INFO_QCOM;
            default:
                return this.CAM_EEPROM_INFO_UNKNOWN;
        }
    }

    private int readFile(String path) {
        IOException e;
        NumberFormatException e2;
        Throwable th;
        int result = -1;
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new FileReader(path));
            try {
                String line = bufferedReader2.readLine();
                if (line != null) {
                    Log.i("CheckPdafInfo", line);
                    result = Integer.parseInt(line);
                }
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e3) {
                        Log.e("CheckPdafInfo", e3.getMessage());
                    }
                }
                bufferedReader = bufferedReader2;
            } catch (IOException e4) {
                e3 = e4;
                bufferedReader = bufferedReader2;
                Log.e("CheckPdafInfo", e3.getMessage());
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e32) {
                        Log.e("CheckPdafInfo", e32.getMessage());
                    }
                }
                return result;
            } catch (NumberFormatException e5) {
                e2 = e5;
                bufferedReader = bufferedReader2;
                try {
                    Log.e("CheckPdafInfo", e2.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e322) {
                            Log.e("CheckPdafInfo", e322.getMessage());
                        }
                    }
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e3222) {
                            Log.e("CheckPdafInfo", e3222.getMessage());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = bufferedReader2;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (IOException e6) {
            e3222 = e6;
            Log.e("CheckPdafInfo", e3222.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return result;
        } catch (NumberFormatException e7) {
            e2 = e7;
            Log.e("CheckPdafInfo", e2.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return result;
        }
        return result;
    }
}
