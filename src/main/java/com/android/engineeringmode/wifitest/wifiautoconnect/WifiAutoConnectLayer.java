package com.android.engineeringmode.wifitest.wifiautoconnect;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

import com.android.engineeringmode.Log;
import com.android.engineeringmode.wifitest.AccessPoint;
import com.android.engineeringmode.wifitest.WifiStatus;

import java.util.List;

public class WifiAutoConnectLayer {
    private BluetoothAdapter mBtDevice = null;
    private Callback mCallBack = null;
    private Context mContext = null;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.i("WifiAutoConnectLayer", "handler message RECONNECT");
                    if (!WifiAutoConnectLayer.this.connectToNetwork()) {
                        WifiAutoConnectLayer.this.mHandler.removeMessages(0);
                        WifiAutoConnectLayer.this.mHandler.sendEmptyMessageDelayed(0, 1000);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private int mNetworkId = -1;
    private WifiConfiguration mWifiConfig = null;
    private WifiManager mWifiManager = null;
    private final BroadcastReceiver mWifiStatusReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                Log.i("WifiAutoConnectLayer", "receive action NETWORK_STATE_CHANGED_ACTION");
                NetworkInfo netInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                Log.i("WifiAutoConnectLayer", "in receive action NETWORK_STATE_CHANGED_ACTION, bssid = " + intent.getStringExtra("bssid"));
                String summary = WifiStatus.getStatus(WifiAutoConnectLayer.this.mContext, WifiAutoConnectLayer.this.mWifiManager.getConnectionInfo().getSSID(), netInfo.getDetailedState());
                if (WifiAutoConnectLayer.this.mCallBack != null) {
                    WifiAutoConnectLayer.this.mCallBack.onSummaryChanged(summary);
                }
                if (!WifiStatus.isLiveConnection(netInfo.getDetailedState())) {
                    if (WifiStatus.isConnectting(netInfo.getDetailedState())) {
                        Log.i("WifiAutoConnectLayer", "is connectting");
                    } else {
                        Log.i("WifiAutoConnectLayer", "wifi is disconnected, so we reconnect to it");
                        WifiAutoConnectLayer.this.mHandler.removeMessages(0);
                        WifiAutoConnectLayer.this.mHandler.sendEmptyMessage(0);
                    }
                }
                if (WifiStatus.isConnected(netInfo.getDetailedState())) {
                    WifiAutoConnectLayer.this.mHandler.removeMessages(0);
                }
            } else if ("android.net.wifi.NETWORK_IDS_CHANGED".equals(action)) {
                Log.i("WifiAutoConnectLayer", "receive action NETWORK_IDS_CHANGED_ACTION");
            } else if ("android.net.wifi.SCAN_RESULTS".equals(action)) {
                Log.i("WifiAutoConnectLayer", "receive action SCAN_RESULTS_AVAILABLE_ACTION");
                List<ScanResult> lstResult = WifiAutoConnectLayer.this.mWifiManager.getScanResults();
                if (lstResult != null) {
                    int nSize = lstResult.size();
                    for (int i = 0; i < nSize; i++) {
                        if (WifiAutoConnectLayer.this.mstrSsid.equals(((ScanResult) lstResult.get(i)).SSID)) {
                            WifiAutoConnectLayer.this.mstrBssid = ((ScanResult) lstResult.get(i)).BSSID;
                            Log.i("WifiAutoConnectLayer", "in receive action SCAN_RESULTS_AVAILABLE_ACTION, we found bssid = " + WifiAutoConnectLayer.this.mstrBssid);
                        }
                    }
                } else {
                    Log.i("WifiAutoConnectLayer", "in receive action SCAN_RESULTS_AVAILABLE_ACTION, lstResult = null");
                }
                if (!WifiAutoConnectLayer.this.mbHasScaned) {
                    WifiAutoConnectLayer.this.mHandler.removeMessages(0);
                    WifiAutoConnectLayer.this.mHandler.sendEmptyMessage(0);
                    WifiAutoConnectLayer.this.mbHasScaned = true;
                }
            } else if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                Log.i("WifiAutoConnectLayer", "receive action WIFI_STATE_CHANGED_ACTION");
                int wifiStatus = intent.getIntExtra("wifi_state", 4);
                if (3 == wifiStatus) {
                    Log.i("WifiAutoConnectLayer", "Wifi is Enabled");
                    if (!WifiAutoConnectLayer.this.mbHasScaned) {
                        WifiAutoConnectLayer.this.startScanWifi();
                    }
                } else if (1 == wifiStatus) {
                    Log.i("WifiAutoConnectLayer", "Wifi is Disabled");
                    WifiAutoConnectLayer.this.mHandler.removeMessages(0);
                    if (WifiAutoConnectLayer.this.mCallBack != null) {
                        WifiAutoConnectLayer.this.mCallBack.onWifiColsed();
                    }
                } else if (wifiStatus == 0) {
                    Log.i("WifiAutoConnectLayer", "Wifi is Disabling");
                    WifiAutoConnectLayer.this.mHandler.removeMessages(0);
                }
            } else {
                if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                    int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                    if (10 == state) {
                        Log.w("WifiAutoConnectLayer", "Receive action BLUETOOTH_STATE_CHANGED_ACTION, state = BLUETOOTH_STATE_OFF");
                        if (WifiAutoConnectLayer.this.mCallBack != null) {
                            WifiAutoConnectLayer.this.mCallBack.onSummaryChanged(WifiAutoConnectLayer.this.mContext.getString(2131296624));
                        }
                        if (WifiAutoConnectLayer.this.mWifiManager.isWifiEnabled()) {
                            WifiAutoConnectLayer.this.startScanWifi();
                        } else {
                            WifiAutoConnectLayer.this.mWifiManager.setWifiEnabled(true);
                        }
                        return;
                    } else if (Integer.MIN_VALUE == state) {
                        Log.w("WifiAutoConnectLayer", "Receive action BLUETOOTH_STATE_CHANGED_ACTION, state = BluetoothError.ERROR");
                        if (WifiAutoConnectLayer.this.mCallBack != null) {
                            WifiAutoConnectLayer.this.mCallBack.onCloseBtError(state);
                        }
                        return;
                    }
                }
                Log.i("WifiAutoConnectLayer", "receive action: " + action);
            }
        }
    };
    private boolean mbHasScaned = false;
    private String mstrBssid = "any";
    private String mstrSsid = "OPPO MT8860C";

    interface Callback {
        void onCloseBtError(int i);

        void onScanFailed();

        void onSummaryChanged(String str);

        void onWifiColsed();
    }

    public WifiAutoConnectLayer(Context context, Callback callBack) {
        this.mContext = context;
        this.mCallBack = callBack;
    }

    public void onCreate() {
        this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        this.mBtDevice = BluetoothAdapter.getDefaultAdapter();
    }

    public void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.wifi.NETWORK_IDS_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
        filter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        filter.addAction("supplicantError");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.RSSI_CHANGED");
        filter.addAction("android.net.wifi.SCAN_RESULTS");
        this.mContext.registerReceiver(this.mWifiStatusReceiver, filter);
        if (this.mBtDevice == null) {
            openWifi();
        } else if (this.mBtDevice.isEnabled()) {
            Log.d("WifiAutoConnectLayer", "begin to close the Buletooth");
            this.mBtDevice.disable();
            if (this.mCallBack != null) {
                this.mCallBack.onSummaryChanged(this.mContext.getString(2131296623));
            }
        } else {
            openWifi();
        }
    }

    public void onPause() {
        this.mContext.unregisterReceiver(this.mWifiStatusReceiver);
    }

    public void onDestory() {
        this.mHandler.removeMessages(0);
        if (this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(false);
        }
    }

    private void openWifi() {
        if (!this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(true);
        } else if (startScanWifi()) {
            this.mHandler.removeMessages(0);
            this.mHandler.sendEmptyMessageDelayed(0, 1000);
        } else if (this.mCallBack != null) {
            this.mCallBack.onSummaryChanged(this.mContext.getString(2131296429));
        }
    }

    private boolean startScanWifi() {
        if (this.mWifiManager != null) {
            boolean bSuc = this.mWifiManager.startScan();
            if (bSuc) {
                if (this.mCallBack != null) {
                    Log.d("WifiAutoConnectLayer", "startScanWifi(), start scanning");
                    this.mCallBack.onSummaryChanged(this.mContext.getString(2131296440));
                }
            } else if (this.mCallBack != null) {
                Log.d("WifiAutoConnectLayer", "startScanWifi(), error scanning");
                this.mCallBack.onScanFailed();
            }
            return bSuc;
        }
        if (this.mCallBack != null) {
            this.mCallBack.onSummaryChanged(this.mContext.getString(2131296440));
        }
        return false;
    }

    public boolean setWifiEnable(boolean bEnable) {
        return this.mWifiManager.setWifiEnabled(bEnable);
    }

    public boolean isWifiEnabled() {
        if (this.mWifiManager == null) {
            return false;
        }
        return this.mWifiManager.isWifiEnabled();
    }

    public boolean connectToNetwork() {
        ScanResult scanResult = new ScanResult();
        if (this.mWifiConfig == null || -1 == this.mNetworkId) {
            Log.i("WifiAutoConnectLayer", "in connectToNetwork(), make config");
            this.mWifiConfig = new WifiConfiguration();
            setupSecurity(this.mWifiConfig, "", "Open");
            this.mWifiConfig.BSSID = scanResult.BSSID;
            Log.i("WifiAutoConnectLayer", "before convert, SSID = " + scanResult.SSID);
            this.mWifiConfig.SSID = AccessPoint.convertToQuotedString(scanResult.SSID);
            Log.i("WifiAutoConnectLayer", "after convert, SSID = " + this.mWifiConfig.SSID);
            this.mNetworkId = this.mWifiManager.addNetwork(this.mWifiConfig);
            Log.i("WifiAutoConnectLayer", "networkId = " + this.mNetworkId);
        }
        if (-1 == this.mNetworkId) {
            return false;
        }
        Log.i("WifiAutoConnectLayer", "enableNetwork, networkId = " + this.mNetworkId);
        return this.mWifiManager.enableNetwork(this.mNetworkId, true);
    }

    public void setupSecurity(WifiConfiguration config, String passWd, String security) {
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        if (security.equals("Open")) {
            config.allowedKeyManagement.set(0);
        } else if (security.equals("WEP")) {
            if (AccessPoint.isHexWepKey(passWd)) {
                config.wepKeys[0] = passWd;
            } else {
                config.wepKeys[0] = AccessPoint.convertToQuotedString(passWd);
            }
            config.wepTxKeyIndex = 0;
            config.allowedAuthAlgorithms.set(0);
            config.allowedAuthAlgorithms.set(1);
            config.allowedKeyManagement.set(0);
            config.allowedGroupCiphers.set(0);
            config.allowedGroupCiphers.set(1);
        }
    }
}
