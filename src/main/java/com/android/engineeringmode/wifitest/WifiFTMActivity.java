package com.android.engineeringmode.wifitest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;

public class WifiFTMActivity extends Activity {
    private static int previous_state = 0;
    private static int previous_wifi_scan_always = 0;
    private Context mContext = null;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (msg.arg1 == 1) {
                        WifiFTMActivity.this.textInfo.setText(2131296616);
                        return;
                    } else if (msg.arg1 == 2) {
                        WifiFTMActivity.this.textInfo.setText(2131296617);
                        WifiFTMActivity.this.setContentView(WifiFTMActivity.this.textInfo);
                        return;
                    } else {
                        WifiFTMActivity.this.textInfo.setText(2131296618);
                        return;
                    }
                case Light.MAIN_KEY_LIGHT /*1*/:
                    Log.e("WifiFTMTest", "state : " + WifiFTMActivity.this.mWifiManager.isWifiEnabled() + "-" + WifiFTMActivity.this.mWifiManager.isWifiApEnabled());
                    if (WifiFTMActivity.this.mWifiManager.isWifiEnabled()) {
                        WifiFTMActivity.this.mWifiManager.setWifiEnabled(false);
                        Toast.makeText(WifiFTMActivity.this.mContext, 2131296425, 1).show();
                        return;
                    } else if (WifiFTMActivity.this.mWifiManager.isWifiApEnabled()) {
                        WifiFTMActivity.this.mWifiManager.setWifiApEnabled(null, false);
                        Toast.makeText(WifiFTMActivity.this.mContext, 2131296425, 1).show();
                        return;
                    } else {
                        synchronized (this) {
                            if (Global.getInt(WifiFTMActivity.this.getContentResolver(), "wifi_scan_always_enabled", 0) == 1) {
                                Log.e("WifiFTMTest", "Disable WIFI_SCAN_ALWAYS_AVAILABLE");
                                Global.putInt(WifiFTMActivity.this.getContentResolver(), "wifi_scan_always_enabled", 0);
                                Log.e("WifiFTMTest", "delay for module load/unload");
                                WifiFTMActivity.this.mHandler.sendMessageDelayed(WifiFTMActivity.this.mHandler.obtainMessage(3), 8000);
                            } else {
                                WifiFTMActivity.this.mHandler.sendMessage(WifiFTMActivity.this.mHandler.obtainMessage(2));
                            }
                        }
                        return;
                    }
                case Light.CHARGE_RED_LIGHT /*2*/:
                    Log.e("WifiFTMTest", "enter to FTM");
                    WifiFTMActivity.this.enterTestMode();
                    return;
                case Light.CHARGE_GREEN_LIGHT /*3*/:
                    String driver_status = SystemProperties.get("wlan.driver.status");
                    Log.e("WifiFTMTest", "driver state is " + driver_status);
                    if (driver_status.equals("ok")) {
                        WifiFTMActivity.this.mHandler.sendMessageDelayed(WifiFTMActivity.this.mHandler.obtainMessage(3), 1000);
                        return;
                    } else {
                        WifiFTMActivity.this.mHandler.sendMessage(WifiFTMActivity.this.mHandler.obtainMessage(2));
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private IntentFilter mIntentFilter;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                WifiFTMActivity.this.handleWifiStateChanged(context, intent.getIntExtra("wifi_state", 4));
            } else if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
                WifiFTMActivity.this.handleWifiStateChanged(context, intent.getIntExtra("wifi_state", 14));
            }
        }
    };
    private WifiManager mWifiManager;
    private WifiFTMUtils mWifiUtils;
    private TextView textInfo;

    private void handleWifiStateChanged(Context context, int state) {
        switch (state) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                Log.e("WifiFTMTest", "wifi state change disabled");
                break;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                Log.e("WifiFTMTest", "wifi state change enabled");
                break;
            case 11:
                Log.e("WifiFTMTest", "softap state change disabled");
                break;
            case 13:
                Log.e("WifiFTMTest", "softap state change enabled");
                break;
        }
        int persistWifiState;
        switch (state) {
            case Light.MAIN_KEY_LIGHT /*1*/:
            case 11:
                persistWifiState = Global.getInt(context.getContentResolver(), "wifi_on", 0);
                Log.d("WifiFTMTest", "wifi state persistWifiState = " + persistWifiState);
                if (persistWifiState != 1) {
                    if (state == 1) {
                        Log.e("WifiFTMTest", "Wifi shutdown change to FTM");
                    } else if (state == 11) {
                        Log.e("WifiFTMTest", "Softap shutdown change to FTM");
                    }
                    if (persistWifiState == 3) {
                        Log.e("WifiFTMTest", "Wifi/Softap shutdown because of enter airplan mode");
                    }
                    if (previous_state == 1) {
                        Log.e("WifiFTMTest", "prepare to enter test mode");
                        this.mHandler.sendMessage(this.mHandler.obtainMessage(1));
                        previous_state = 0;
                        return;
                    }
                    return;
                } else if (state == 1) {
                    Log.e("WifiFTMTest", "Turn on softap when it is wifi");
                    return;
                } else if (state == 11) {
                    Log.e("WifiFTMTest", "Shut down softap and turn on wifi");
                    return;
                } else {
                    return;
                }
            case Light.CHARGE_GREEN_LIGHT /*3*/:
            case 13:
                persistWifiState = Global.getInt(context.getContentResolver(), "wifi_on", 0);
                Log.d("WifiFTMTest", "wifi state persistWifiState = " + persistWifiState);
                if (persistWifiState == 2) {
                    Log.d("WifiFTMTest", "Enable Wifi under airplane mode");
                }
                previous_state = 1;
                exitTestMode();
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(128, 128);
        this.textInfo = (TextView) LayoutInflater.from(this).inflate(2130903203, null);
        this.textInfo.setGravity(17);
        this.textInfo.setTextSize(30.0f);
        this.textInfo.setBackgroundColor(2131165187);
        setContentView(this.textInfo);
        this.mWifiUtils = new WifiFTMUtils();
        this.mContext = this;
        this.mWifiManager = (WifiManager) getSystemService("wifi");
        this.mIntentFilter = new IntentFilter();
        this.mIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        this.mIntentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        previous_wifi_scan_always = Global.getInt(getContentResolver(), "wifi_scan_always_enabled", 0);
        registerReceiver(this.mReceiver, this.mIntentFilter);
    }

    protected void onResume() {
        super.onResume();
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1));
    }

    protected void onDestroy() {
        super.onDestroy();
        exitTestMode();
        unregisterReceiver(this.mReceiver);
        if (previous_wifi_scan_always == 1 && Global.getInt(getContentResolver(), "wifi_scan_always_enabled", 0) == 0) {
            Log.e("WifiFTMTest", "Restore WIFI_SCAN_ALWAYS_AVAILABLE");
            Global.putInt(getContentResolver(), "wifi_scan_always_enabled", 1);
        }
    }

    protected void onStop() {
        super.onStop();
    }

    private boolean loadFTM() {
        boolean res;
        synchronized (this) {
            Log.e("WifiFTMTest", "enter test mode start+++");
            res = this.mWifiManager.loadFtmDriver();
            Log.e("WifiFTMTest", "enter test mode start----");
        }
        return res;
    }

    private boolean unloadFTM() {
        boolean res;
        synchronized (this) {
            Log.e("WifiFTMTest", "exit test mode start+++");
            res = this.mWifiManager.unloadFtmDriver();
            Log.e("WifiFTMTest", "exit test mode start----");
        }
        return res;
    }

    private void enterTestMode() {
        Toast.makeText(this, 2131296615, 1).show();
        new Thread(null, new Runnable() {
            public void run() {
                int i;
                boolean res = WifiFTMActivity.this.loadFTM();
                Handler - get1 = WifiFTMActivity.this.mHandler;
                if (res) {
                    i = 1;
                } else {
                    i = 0;
                }
                -get1.obtainMessage(0, i, 0).sendToTarget();
            }
        }).start();
    }

    private void exitTestMode() {
        Toast.makeText(this, "exit the ftm mode", 1).show();
        new Thread(null, new Runnable() {
            public void run() {
                try {
                    if (WifiFTMActivity.this.mWifiManager.isWifiEnabled() || WifiFTMActivity.this.mWifiManager.isWifiApEnabled()) {
                        WifiFTMActivity.this.mHandler.obtainMessage(0, 2, 0).sendToTarget();
                    } else {
                        WifiFTMActivity.this.unloadFTM();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
