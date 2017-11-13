package com.android.engineeringmode.wifitest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.IpConfiguration.IpAssignment;
import android.net.LinkAddress;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.StaticIpConfiguration;
import android.net.wifi.ScanResult;
import android.net.wifi.ScanSettings;
import android.net.wifi.WifiChannel;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.ActionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.provider.Settings.Global;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.nvbackup.OemHookManager;
import com.qualcomm.qcrilhook.QcRilHook.ApCmd2ModemType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class WifiSocketHelper extends Activity {
    private final int EVENT_QCRIL_HOOK_READY = 55;
    private String current_ssid = "";
    private boolean inited = false;
    private BroadcastReceiver mBroadcastReceiver;
    private TextView mChannelTextView;
    private ConnectivityManager mConnectivityManager;
    private ContentResolver mContentResolver;
    private TextView mDiagDaemonTextView;
    private TextView mDnsTextView;
    private TextView mGatewayTextView;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.i("WifiSocketHelper", "handleMessage msg.what : " + WifiSocketHelper.this.getMsgSummary(msg.what));
            switch (msg.what) {
                case 0:
                    if (!WifiSocketHelper.this.mWifiManager.isWifiEnabled()) {
                        WifiSocketHelper.this.mWifiManager.setWifiEnabled(true);
                        return;
                    }
                    return;
                case Light.MAIN_KEY_LIGHT /*1*/:
                    if (WifiSocketHelper.this.mWifiManager.isWifiEnabled()) {
                        WifiSocketHelper.this.mWifiManager.setWifiEnabled(false);
                    }
                    if (Global.getInt(WifiSocketHelper.this.mContentResolver, "wifi_scan_always_enabled", 0) != 1) {
                        Log.i("WifiSocketHelper", "DISABLE_WIFI_MSG target_ap_connected set WIFI_SCAN_ALWAYS_AVAILABLE to 1");
                        Global.putInt(WifiSocketHelper.this.mContentResolver, "wifi_scan_always_enabled", 1);
                        return;
                    }
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    WifiSocketHelper.this.connectToSSID(WifiSocketHelper.this.target_ssid);
                    return;
                case 5:
                    if (SystemProperties.get("service.wifi.socket.enable", "0").equals("1")) {
                        Log.i("WifiSocketHelper", "wifi socket already on");
                    } else {
                        Log.i("WifiSocketHelper", "truning on wifi socket");
                        SystemProperties.set("service.wifi.socket.enable", "1");
                    }
                    new serviceStatusThread(WifiSocketHelper.this, "init.svc.wifisocket").start();
                    return;
                case Light.MAIN_KEY_NORMAL /*6*/:
                    if (SystemProperties.get("service.wifi.socket.enable", "0").equals("0")) {
                        Log.i("WifiSocketHelper", "wifi socket already off");
                        return;
                    }
                    Log.i("WifiSocketHelper", "truning off wifi socket");
                    SystemProperties.set("service.wifi.socket.enable", "0");
                    return;
                case 7:
                    WifiSocketHelper.this.mWifiStatusTextView.setText(2131297623);
                    Toast.makeText(WifiSocketHelper.this, "connect to AP(" + WifiSocketHelper.this.target_ssid + ") fail ", 3000).show();
                    return;
                case 8:
                    if (msg.getData().getBoolean("value")) {
                        new autoQuitThread(WifiSocketHelper.this).start();
                        WifiSocketHelper.this.mDiagDaemonTextView.setText(2131297627);
                        Toast.makeText(WifiSocketHelper.this, "config success! ", 3000).show();
                        return;
                    }
                    WifiSocketHelper.this.mDiagDaemonTextView.setText(2131297628);
                    Toast.makeText(WifiSocketHelper.this, "config failed! ", 3000).show();
                    return;
                case 55:
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_DISABLE_TUNER_ACL, new byte[]{(byte) 0});
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_DISABLE_TUNER_ACL, new byte[]{(byte) 1});
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_SET_ANT_NS_MODE, new byte[]{(byte) 1});
                    return;
                default:
                    return;
            }
        }
    };
    private TextView mIpAddressTextView;
    private TextView mSSIDTextView;
    private StringBuilder mShellCommandSB = new StringBuilder();
    private TextView mSignalTextView;
    private int mWifiAlwaysAvailable = -1;
    private WifiManager mWifiManager;
    private TextView mWifiStatusTextView;
    private int networkId;
    private Button quitBtn;
    private boolean rutils_daemon_on = false;
    private boolean target_ap_connected = false;
    private boolean target_ap_connecting = false;
    private int target_channel = 11;
    private String target_ip_address = "181.157.1.101/24";
    private String target_network_dns = "181.157.1.1";
    private String target_network_gateway = "181.157.1.1";
    private int target_signal = -45;
    private String target_ssid = "TE-NonSignal";
    private boolean wifi_socket_on = false;

    private static class autoQuitThread extends Thread {
        private int mInterval = 1000;
        private final WifiSocketHelper mWifiSocketHelper;

        public autoQuitThread(WifiSocketHelper wifiSocketHelper) {
            super("WifiSocketHelper");
            this.mWifiSocketHelper = wifiSocketHelper;
        }

        public void run() {
            while (true) {
                try {
                    String val = SystemProperties.get("service.wifi.socket.quit", "0");
                    if (val.equals("2")) {
                        break;
                    } else if (val.equals("1")) {
                        break;
                    } else {
                        Thread.sleep((long) this.mInterval);
                    }
                } catch (Exception e) {
                    Log.e("WifiSocketHelper", "error");
                }
            }
            if (this.mWifiSocketHelper != null) {
                this.mWifiSocketHelper.finish();
            }
            Log.e("WifiSocketHelper", "autoQuitThread exit");
        }
    }

    private static class serviceStatusThread extends Thread {
        private int mInterval = 1000;
        private int mMaxTimes = 5;
        private String mProcessName = "unkown";
        private final WifiSocketHelper mWifiSocketHelper;

        public serviceStatusThread(WifiSocketHelper wifiSocketHelper, String processName) {
            super("WifiSocketHelper");
            this.mWifiSocketHelper = wifiSocketHelper;
            this.mProcessName = processName;
        }

        public void run() {
            boolean z = false;
            while (this.mMaxTimes > 0) {
                try {
                    z = this.mWifiSocketHelper.isServiceRunning(this.mProcessName);
                    if (z) {
                        Log.e("WifiSocketHelper", "Sevice " + this.mProcessName + " is running");
                        break;
                    } else {
                        this.mMaxTimes--;
                        Thread.sleep((long) this.mInterval);
                    }
                } catch (Exception e) {
                    z = false;
                    Log.e("WifiSocketHelper", "error");
                }
            }
            this.mWifiSocketHelper.sendServiceStatusMessage(z);
        }
    }

    private static InetAddress ipAddress(String addr) {
        return InetAddress.parseNumericAddress(addr);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        this.mWifiManager = (WifiManager) getSystemService("wifi");
        this.mConnectivityManager = (ConnectivityManager) getSystemService("connectivity");
        setContentView(2130903232);
        this.quitBtn = (Button) findViewById(2131493659);
        this.mSSIDTextView = (TextView) findViewById(2131493651);
        this.mSignalTextView = (TextView) findViewById(2131493652);
        this.mChannelTextView = (TextView) findViewById(2131493653);
        this.mIpAddressTextView = (TextView) findViewById(2131493654);
        this.mGatewayTextView = (TextView) findViewById(2131493655);
        this.mDnsTextView = (TextView) findViewById(2131493656);
        this.mWifiStatusTextView = (TextView) findViewById(2131493657);
        this.mDiagDaemonTextView = (TextView) findViewById(2131493658);
        this.quitBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.i("WifiSocketHelper", "quit btn click");
                WifiSocketHelper.this.finish();
            }
        });
        if (!this.mWifiManager.isWifiEnabled()) {
            this.mHandler.sendEmptyMessage(0);
        }
        SystemProperties.set("persist.sys.adddevdiag", "1");
        SystemProperties.set("service.wifi.socket.quit", "0");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("oem.intent.action.QUIT_NON_SIGNAL_TEST");
        this.mContentResolver = getContentResolver();
        this.mWifiAlwaysAvailable = Global.getInt(this.mContentResolver, "wifi_scan_always_enabled", 0);
        Log.i("WifiSocketHelper", "mWifiAlwaysAvailable = " + this.mWifiAlwaysAvailable);
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.i("WifiSocketHelper", "onReceive action : " + action);
                if (action != null) {
                    if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                        int wifi_state = intent.getIntExtra("wifi_state", 4);
                        Log.i("WifiSocketHelper", "WIFI_STATE_CHANGED_ACTION : wifi_state=" + wifi_state);
                        if (wifi_state == 3) {
                            if (!WifiSocketHelper.this.inited) {
                                WifiSocketHelper.this.mWifiStatusTextView.setText(2131297619);
                                List<WifiConfiguration> config = WifiSocketHelper.this.mWifiManager.getConfiguredNetworks();
                                if (config != null) {
                                    for (int i = 0; i < config.size(); i++) {
                                        WifiSocketHelper.this.mWifiManager.removeNetwork(((WifiConfiguration) config.get(i)).networkId);
                                    }
                                    WifiSocketHelper.this.mWifiManager.saveConfiguration();
                                } else {
                                    Log.d("WifiSocketHelper", "getConfiguredNetworks return null, forget network fail!");
                                }
                                Global.putInt(WifiSocketHelper.this.getContentResolver(), "wifi_auto_change_access_point", 0);
                                WifiSocketHelper.this.inited = true;
                            }
                            WifiSocketHelper.this.startCustomizedScan();
                            WifiSocketHelper.this.mWifiStatusTextView.setText(2131297620);
                        }
                    } else if ("android.net.wifi.SCAN_RESULTS".equals(action)) {
                        Log.i("WifiSocketHelper", "inited=" + WifiSocketHelper.this.inited + ", target_ap_connected=" + WifiSocketHelper.this.target_ap_connected + ", target_ap_connecting=" + WifiSocketHelper.this.target_ap_connecting);
                        if (WifiSocketHelper.this.inited && !WifiSocketHelper.this.target_ap_connected && !WifiSocketHelper.this.target_ap_connecting) {
                            List<ScanResult> results = WifiSocketHelper.this.mWifiManager.getScanResults();
                            if (results != null) {
                                int best_signal_level = -127;
                                for (ScanResult result : results) {
                                    if (result.SSID != null && result.SSID.contains(WifiSocketHelper.this.target_ssid)) {
                                        if (result.level > best_signal_level) {
                                            best_signal_level = result.level;
                                        }
                                        int signal_level = result.level;
                                        Log.i("WifiSocketHelper", "ap found! signal_level is " + best_signal_level);
                                        WifiSocketHelper.this.mWifiStatusTextView.setText(WifiSocketHelper.this.getString(2131297621, new Object[]{Integer.valueOf(best_signal_level)}));
                                        if (signal_level > WifiSocketHelper.this.target_signal) {
                                            WifiSocketHelper.this.connectToSSID(WifiSocketHelper.this.target_ssid);
                                            break;
                                        }
                                    }
                                }
                            }
                            WifiSocketHelper.this.startCustomizedScan();
                        }
                    } else if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                        Parcelable parcelableExtra = intent.getParcelableExtra("networkInfo");
                        if (parcelableExtra != null) {
                            networkInfo = (NetworkInfo) parcelableExtra;
                            State state = networkInfo.getState();
                            int network_type = networkInfo.getType();
                            boolean isWifiEnabled = WifiSocketHelper.this.mWifiManager.isWifiEnabled();
                            Log.i("WifiSocketHelper", "networkInfo : " + networkInfo.toString() + "; isWifiEnabled = " + isWifiEnabled);
                            if (isWifiEnabled && 1 == network_type && state != State.DISCONNECTED && state != State.DISCONNECTING && state == State.CONNECTED) {
                                WifiInfo wifiInfo = WifiSocketHelper.this.mWifiManager.getConnectionInfo();
                                Log.i("WifiSocketHelper", "wifiInfo : " + wifiInfo.toString());
                                WifiSocketHelper.this.current_ssid = wifiInfo.getSSID();
                                if (WifiSocketHelper.this.inited && WifiSocketHelper.this.current_ssid.contains(WifiSocketHelper.this.target_ssid) && state == State.CONNECTED) {
                                    WifiSocketHelper.this.mWifiStatusTextView.setText(2131297624);
                                    WifiSocketHelper.this.target_ap_connecting = false;
                                    WifiSocketHelper.this.target_ap_connected = true;
                                    WifiSocketHelper.this.mHandler.removeMessages(7);
                                }
                            }
                        }
                    } else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                        networkInfo = (NetworkInfo) intent.getExtras().get("networkInfo");
                        Log.i("WifiSocketHelper", "networkInfo : " + networkInfo.toString());
                        if (WifiSocketHelper.this.target_ap_connected && networkInfo != null && networkInfo.isAvailable()) {
                            if (Global.getInt(WifiSocketHelper.this.mContentResolver, "wifi_scan_always_enabled", 0) != 0) {
                                Log.i("WifiSocketHelper", "target_ap_connected set WIFI_SCAN_ALWAYS_AVAILABLE to 0");
                                Global.putInt(WifiSocketHelper.this.mContentResolver, "wifi_scan_always_enabled", 0);
                            }
                            WifiSocketHelper.this.mHandler.sendEmptyMessageDelayed(5, 1000);
                            OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_SET_ANT_NS_MODE, new byte[]{(byte) 1});
                        }
                    } else if ("oem.intent.action.QUIT_NON_SIGNAL_TEST".equals(action)) {
                        Log.i("WifiSocketHelper", "oem.intent.action.QUIT_NON_SIGHNAL_TEST recived, quit!");
                        WifiSocketHelper.this.finish();
                    }
                }
            }
        };
        loadConfigFromFile();
        this.mSSIDTextView.setText(this.target_ssid);
        this.mSignalTextView.setText("" + this.target_signal);
        this.mChannelTextView.setText("" + this.target_channel);
        this.mIpAddressTextView.setText(this.target_ip_address);
        this.mGatewayTextView.setText(this.target_network_gateway);
        this.mDnsTextView.setText(this.target_network_dns);
        registerReceiver(this.mBroadcastReceiver, intentFilter);
        OemHookManager.getInstance().registerQcRilHookReady(this.mHandler, 55, null);
    }

    protected void onResume() {
        super.onResume();
        Log.i("WifiSocketHelper", "onResume");
        Intent intent = new Intent("com.oem.intent.action.KEY_LOCK_MODE");
        intent.putExtra("KeyLockMode", 2);
        intent.putExtra("ProcessName", "com.android.engineeringmode");
        sendBroadcast(intent);
    }

    private void loadConfigFromFile() {
        IOException e;
        Throwable th;
        File config = new File("/sdcard/wifi_socket_config.ini");
        if (config.exists()) {
            BufferedReader bufferedReader = null;
            try {
                BufferedReader reader = new BufferedReader(new FileReader(config));
                while (true) {
                    try {
                        String tempString = reader.readLine();
                        if (tempString == null) {
                            break;
                        }
                        Log.i("WifiSocketHelper", "read from config : " + tempString);
                        if (tempString.length() > 0 && tempString.contains("=")) {
                            String[] parameter = tempString.trim().split("=");
                            if (parameter != null && parameter.length > 1) {
                                if (parameter[0].equalsIgnoreCase("target_ssid")) {
                                    this.target_ssid = parameter[1];
                                    Log.i("WifiSocketHelper", "target_ssid = " + this.target_ssid);
                                } else if (parameter[0].equalsIgnoreCase("target_signal")) {
                                    try {
                                        this.target_signal = Integer.valueOf(parameter[1]).intValue();
                                    } catch (NumberFormatException e2) {
                                        Log.e("WifiSocketHelper", "loadConfigFromFile NumberFormatException:" + e2.getMessage());
                                        this.target_signal = -45;
                                    }
                                    Log.i("WifiSocketHelper", "target_signal = " + this.target_signal);
                                } else if (parameter[0].equalsIgnoreCase("target_channel")) {
                                    try {
                                        this.target_channel = Integer.valueOf(parameter[1]).intValue();
                                    } catch (NumberFormatException e22) {
                                        Log.e("WifiSocketHelper", "loadConfigFromFile NumberFormatException:" + e22.getMessage());
                                        this.target_channel = 11;
                                    }
                                    Log.i("WifiSocketHelper", "target_channel = " + this.target_channel);
                                } else if (parameter[0].equalsIgnoreCase("target_ip_address")) {
                                    this.target_ip_address = parameter[1];
                                    Log.i("WifiSocketHelper", "target_ip_address = " + this.target_channel);
                                } else if (parameter[0].equalsIgnoreCase("target_network_gateway")) {
                                    this.target_network_gateway = parameter[1];
                                    Log.i("WifiSocketHelper", "target_network_gateway = " + this.target_network_gateway);
                                } else if (parameter[0].equalsIgnoreCase("target_network_dns")) {
                                    this.target_network_dns = parameter[1];
                                    Log.i("WifiSocketHelper", "target_network_dns = " + this.target_network_dns);
                                }
                            }
                        }
                    } catch (IOException e3) {
                        e = e3;
                        bufferedReader = reader;
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedReader = reader;
                    }
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("WifiSocketHelper", "loadConfigFromFile io close exception :" + e1.getMessage());
                    }
                }
            } catch (IOException e4) {
                e = e4;
                try {
                    Log.e("WifiSocketHelper", "loadConfigFromFile io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("WifiSocketHelper", "loadConfigFromFile io close exception :" + e12.getMessage());
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("WifiSocketHelper", "loadConfigFromFile io close exception :" + e122.getMessage());
                        }
                    }
                    throw th;
                }
            }
        }
    }

    protected void onPause() {
        super.onPause();
        Log.i("WifiSocketHelper", "onPause");
        Intent intent = new Intent("com.oem.intent.action.KEY_LOCK_MODE");
        intent.putExtra("KeyLockMode", 0);
        intent.putExtra("ProcessName", "com.android.engineeringmode");
        sendBroadcast(intent);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("WifiSocketHelper", "onKeyDown KEY:" + keyCode);
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("WifiSocketHelper", "onKeyUp KEY:" + keyCode);
        if (keyCode != 4) {
            return true;
        }
        finish();
        return true;
    }

    private String getMsgSummary(int msg) {
        switch (msg) {
            case 0:
                return "ENABLE_WIFI_MSG";
            case Light.MAIN_KEY_LIGHT /*1*/:
                return "DISABLE_WIFI_MSG";
            case Light.CHARGE_RED_LIGHT /*2*/:
                return "CONNECT_TO_FIX_AP_MSG";
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                return "ENABLE_RUTILS_DAEMON_MSG";
            case 4:
                return "DISABLE_RUTILS_DAEMON_MSG";
            case 5:
                return "START_WIFI_SOCKET_SERVICE_MSG";
            case Light.MAIN_KEY_NORMAL /*6*/:
                return "STOP_WIFI_SOCKET_SERVICE_MSG";
            case 7:
                return "CONNECT_TO_FIX_AP_TIMEOUT_MSG";
            case 8:
                return "SERVICE_RUNNING_MSG";
            default:
                return "UNKNOWN_MSG";
        }
    }

    private boolean isServiceRunning(String processName) {
        if (SystemProperties.get(processName, "stopped").equals("running")) {
            return true;
        }
        return false;
    }

    private void sendServiceStatusMessage(boolean result) {
        Message msg = Message.obtain();
        msg.what = 8;
        Bundle data = new Bundle();
        data.putBoolean("value", result);
        msg.setData(data);
        this.mHandler.sendMessage(msg);
    }

    private int getChannelFrequency(int channel) {
        switch (channel) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                return 2412;
            case Light.CHARGE_RED_LIGHT /*2*/:
                return 2417;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                return 2422;
            case 4:
                return 2427;
            case 5:
                return 2432;
            case Light.MAIN_KEY_NORMAL /*6*/:
                return 2437;
            case 7:
                return 2442;
            case 8:
                return 2447;
            case 9:
                return 2452;
            case 10:
                return 2457;
            case 11:
                return 2462;
            case 12:
                return 2467;
            case 13:
                return 2472;
            case 14:
                return 2484;
            case 149:
                return 5745;
            case 153:
                return 5765;
            case 157:
                return 5785;
            case 161:
                return 5805;
            case 165:
                return 5825;
            default:
                return -1;
        }
    }

    private void startCustomizedScan() {
        Log.i("WifiSocketHelper", "startCustomizedScan");
        WifiChannel channel = new WifiChannel();
        channel.channelNum = this.target_channel;
        channel.freqMHz = getChannelFrequency(this.target_channel);
        ScanSettings settings = new ScanSettings();
        settings.channelSet = new ArrayList(1);
        settings.channelSet.add(channel);
        this.mWifiManager.startCustomizedScan(settings, new WorkSource());
    }

    private void connectToSSID(String ssid) {
        Log.i("WifiSocketHelper", "connectToSSID " + ssid);
        if (!this.target_ap_connecting) {
            this.target_ap_connecting = true;
            this.mWifiStatusTextView.setText(2131297622);
            StaticIpConfiguration staticIpConfig = new StaticIpConfiguration();
            staticIpConfig.ipAddress = new LinkAddress(this.target_ip_address);
            staticIpConfig.gateway = ipAddress(this.target_network_gateway);
            staticIpConfig.dnsServers.add(ipAddress(this.target_network_dns));
            WifiConfiguration config = new WifiConfiguration();
            config.SSID = "\"" + ssid + "\"";
            config.allowedKeyManagement.set(0);
            config.setIpAssignment(IpAssignment.STATIC);
            config.setStaticIpConfiguration(staticIpConfig);
            this.networkId = this.mWifiManager.addNetwork(config);
            this.mWifiManager.enableNetwork(this.networkId, true);
            this.mWifiManager.reconnect();
            if (!this.mHandler.hasMessages(7)) {
                this.mHandler.sendEmptyMessageDelayed(7, 10000);
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        SystemProperties.set("service.wifi.socket.quit", "2");
        this.mHandler.removeMessages(7);
        this.mHandler.removeMessages(5);
        this.mHandler.removeMessages(8);
        unregisterReceiver(this.mBroadcastReceiver);
        this.mHandler.sendEmptyMessage(6);
        OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_SET_ANT_NS_MODE, new byte[]{(byte) 0});
        if (this.networkId >= 0) {
            this.mWifiManager.forget(this.networkId, new ActionListener() {
                public void onSuccess() {
                    Log.i("WifiSocketHelper", "forget ap success");
                }

                public void onFailure(int reason) {
                    Log.i("WifiSocketHelper", "forget ap failed : reson = " + reason);
                }
            });
        }
        this.mHandler.sendEmptyMessage(1);
        SystemProperties.set("persist.sys.adddevdiag", "0");
    }
}
