package com.android.engineeringmode.manualtest;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

public class DeviceManager {
    private Context mContext = null;
    private List<DeviceInfo> mDeviceInfoList = null;
    private List<String> mDeviceNameList = null;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    AsyncResult asynResult = msg.obj;
                    if (asynResult != null) {
                        DeviceManager.this.process(asynResult.result);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private List<ModemItem> mModemItemsList = null;
    private OnGetDeviceInfoCompletedListener mOnGetDeviceInfoCompletedListener;
    private Phone mPhone = null;
    private SensorManager mSensorManager = null;

    public interface OnGetDeviceInfoCompletedListener {
        void onCompleted(List<DeviceInfo> list);
    }

    class ModemItem {
        int mid;
        String model;
        int pid;
        String vendor;
        int vid;

        ModemItem() {
        }
    }

    private void process(int[] array) {
        if (array != null && (array.length - 1) % 3 == 0) {
            for (int i = 1; i < array.length; i += 3) {
                int mid = array[i];
                int pid = array[i + 1];
                int vid = array[i + 2];
                boolean ishit = false;
                for (int j = 0; j < this.mModemItemsList.size(); j++) {
                    ModemItem modemItem = (ModemItem) this.mModemItemsList.get(j);
                    if (modemItem.mid == mid && modemItem.pid == pid && modemItem.vid == vid) {
                        Log.e("DeviceManager", "find it...");
                        this.mDeviceInfoList.add(new DeviceInfo("pa", modemItem.model, modemItem.vendor, null));
                        ishit = true;
                        break;
                    }
                }
                if (!ishit) {
                    this.mDeviceInfoList.add(new DeviceInfo("pa", "Match Fail:mid:" + mid + " pid:" + pid, null, null));
                    Toast.makeText(this.mContext, this.mContext.getResources().getString(2131297334), 1).show();
                }
            }
        }
        if (this.mOnGetDeviceInfoCompletedListener != null) {
            this.mOnGetDeviceInfoCompletedListener.onCompleted(this.mDeviceInfoList);
        }
    }

    public void setOnGetDeviceInfoCompleted(OnGetDeviceInfoCompletedListener onDataChangedListener) {
        this.mOnGetDeviceInfoCompletedListener = onDataChangedListener;
    }

    public DeviceManager(Context context) {
        this.mContext = context;
        this.mDeviceNameList = new ArrayList();
        this.mModemItemsList = new ArrayList();
        this.mDeviceInfoList = new ArrayList();
        this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        initDeviceList();
        readModemConfigList();
    }

    private void readModemConfigList() {
        XmlResourceParser xrp = this.mContext.getResources().getXml(2130968590);
        while (xrp.getEventType() != 1) {
            try {
                if (xrp.getEventType() == 2) {
                    if (xrp.getName().equals("pa")) {
                        ModemItem modemItem = new ModemItem();
                        modemItem.mid = parseHexNum(xrp.getAttributeValue(1));
                        modemItem.pid = parseHexNum(xrp.getAttributeValue(2));
                        modemItem.vid = parseHexNum(xrp.getAttributeValue(3));
                        modemItem.model = xrp.getAttributeValue(4);
                        modemItem.vendor = xrp.getAttributeValue(5);
                        this.mModemItemsList.add(modemItem);
                    }
                } else if (xrp.getEventType() != 3 && xrp.getEventType() == 4) {
                }
                xrp.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                return;
            } catch (IOException e2) {
                e2.printStackTrace();
                return;
            }
        }
    }

    private int parseHexNum(String str) {
        String temp = str;
        if (str.startsWith("0x")) {
            temp = str.substring(2);
        }
        return Integer.parseInt(temp, 16);
    }

    private void initDeviceList() {
        this.mDeviceNameList.add("mcr");
        this.mDeviceNameList.add("ddr");
        this.mDeviceNameList.add("emmc");
        this.mDeviceNameList.add("ufs");
        this.mDeviceNameList.add("f_camera");
        this.mDeviceNameList.add("r_camera");
        this.mDeviceNameList.add("second_r_camera");
        this.mDeviceNameList.add("tp");
        this.mDeviceNameList.add("lcd");
        this.mDeviceNameList.add("wcn");
        this.mDeviceNameList.add("nfc");
        this.mDeviceNameList.add("l_sensor");
        this.mDeviceNameList.add("p_sensor");
        this.mDeviceNameList.add("g_sensor");
        this.mDeviceNameList.add("m_sensor");
        this.mDeviceNameList.add("gyro");
        this.mDeviceNameList.add("backlight");
        this.mDeviceNameList.add("back_tp");
        this.mDeviceNameList.add("mainboard");
        this.mDeviceNameList.add("fingerprints");
        this.mDeviceNameList.add("touch_key");
        this.mDeviceNameList.add("pa");
        this.mDeviceNameList.add("Aboard");
        this.mDeviceNameList.add("fast_charge");
        this.mDeviceNameList.add("cpu");
        this.mDeviceNameList.add("userdata");
    }

    public void run() {
        for (int i = 0; i < this.mDeviceNameList.size(); i++) {
            String deviceName = (String) this.mDeviceNameList.get(i);
            if (deviceName.contains("pa")) {
                processModemDevice(deviceName);
            } else {
                DeviceInfo deviceInfo = processDevice(deviceName);
                if (deviceInfo != null) {
                    if (SystemProperties.get("ro.boot.bootdevice").contains("ufshc")) {
                        if (!deviceName.contains("emmc")) {
                            this.mDeviceInfoList.add(deviceInfo);
                        }
                    } else if (!deviceName.contains("ufs")) {
                        this.mDeviceInfoList.add(deviceInfo);
                    }
                }
            }
        }
        if (this.mOnGetDeviceInfoCompletedListener != null) {
            this.mOnGetDeviceInfoCompletedListener.onCompleted(this.mDeviceInfoList);
        }
    }

    private DeviceInfo processDevice(String deviceName) {
        boolean sensortype;
        if (deviceName.contains("sensor")) {
            sensortype = true;
        } else {
            sensortype = deviceName.contains("gyro");
        }
        if (sensortype) {
            return processSensorDevice(deviceName);
        }
        if (deviceName.equals("userdata")) {
            return processUserDataFs(deviceName);
        }
        return processNormalDevice(deviceName);
    }

    private DeviceInfo processUserDataFs(String deviceName) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceName(deviceName);
        deviceInfo.setDeviceVersion(SystemProperties.get("vold.datafs.type"));
        deviceInfo.setDeviceManufacture("ONEPLUS");
        return deviceInfo;
    }

    private void processModemDevice(String deviceName) {
        this.mPhone = PhoneFactory.getDefaultPhone();
        this.mPhone.oemGetRffeDevInfo(0, this.mHandler.obtainMessage(100));
    }

    private DeviceInfo processNormalDevice(String deviceName) {
        BufferedReader bufferedReader;
        String filePath = "/sys/project_info/component_info/" + deviceName;
        File file = new File(filePath);
        if (!file.exists()) {
            Log.e("DeviceManager", "File " + filePath + " is not exist...");
            return null;
        } else if (file.canRead()) {
            DeviceInfo deviceInfo = new DeviceInfo();
            try {
                deviceInfo.setDeviceName(deviceName);
                BufferedReader bufferedReader2 = new BufferedReader(new FileReader(file));
                while (true) {
                    try {
                        String line = bufferedReader2.readLine();
                        if (line == null) {
                            return deviceInfo;
                        }
                        Log.i("DeviceManager", line);
                        String[] lineSplit;
                        if (line.contains("VER:")) {
                            lineSplit = line.split(":");
                            if (lineSplit.length > 1) {
                                deviceInfo.setDeviceVersion(lineSplit[1].trim());
                            }
                        } else if (line.contains("MANU:")) {
                            lineSplit = line.split(":");
                            if (lineSplit.length > 1) {
                                deviceInfo.setDeviceManufacture(lineSplit[1].trim());
                            }
                        } else {
                            deviceInfo.setDeviceOtherInfo(line);
                        }
                    } catch (FileNotFoundException e) {
                        bufferedReader = bufferedReader2;
                    } catch (IOException e2) {
                        bufferedReader = bufferedReader2;
                    }
                }
            } catch (FileNotFoundException e3) {
                return null;
            } catch (IOException e4) {
                return null;
            }
        } else {
            Log.e("DeviceManager", "No permission to read " + filePath);
            return null;
        }
    }

    private DeviceInfo processSensorDevice(String deviceName) {
        int sensorType = -1;
        if ("l_sensor".equals(deviceName)) {
            sensorType = 5;
        } else if ("p_sensor".equals(deviceName)) {
            sensorType = 8;
        } else if ("g_sensor".equals(deviceName)) {
            sensorType = 1;
        } else if ("m_sensor".equals(deviceName)) {
            sensorType = 2;
        } else if ("gyro".equals(deviceName)) {
            sensorType = 4;
        }
        List<Sensor> sensors = this.mSensorManager.getSensorList(sensorType);
        if (sensors == null) {
            return null;
        }
        Log.e("DeviceManager", sensors.toString());
        for (int i = 0; i < sensors.size(); i++) {
            Sensor sensor = (Sensor) sensors.get(i);
            if (sensor.getType() == sensorType) {
                String[] splitSensorName = sensor.getName().split(" ");
                String sensorVersion = sensor.getName();
                if (splitSensorName.length > 1) {
                    sensorVersion = splitSensorName[0];
                }
                return new DeviceInfo(deviceName, sensorVersion, sensor.getVendor(), null);
            }
        }
        return null;
    }
}
