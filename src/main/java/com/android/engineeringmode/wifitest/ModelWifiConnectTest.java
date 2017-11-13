package com.android.engineeringmode.wifitest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.ActionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.security.Credentials;
import android.security.KeyStore;
import android.security.KeyStore.State;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.wifitest.download.DownloadManager;

import java.util.ArrayList;
import java.util.List;

public class ModelWifiConnectTest extends PreferenceActivity implements OnClickListener, OnPreferenceChangeListener {
    private ProgressCategory mAccessPoints;
    private int mAccessPointsCount = 0;
    private Preference mAddNetwork;
    private WifiDialog mDialog;
    private DownloadManager mDownloadManager = null;
    private final IntentFilter mFilter = new IntentFilter();
    private ActionListener mForgetListener;
    private int mKeyStoreNetworkId = -1;
    private WifiInfo mLastInfo;
    private int mLastPriority;
    private DetailedState mLastState;
    private int mMinScapApCount = 5;
    private WifiDownloadPreference mPrefWifiStatus;
    private final BroadcastReceiver mReceiver;
    private boolean mResetNetworks = false;
    private final Scanner mScanner;
    private AccessPoint mSelected;
    private WifiDownloadStatusShow mWifiDownloadStatusShow;
    private boolean mWifiEnabled;
    private WifiEnabler mWifiEnabler;
    private WifiManager mWifiManager;
    private boolean mbIsBackPressed = false;
    private EditTextPreference mprefServerFilepathSet;
    private EditTextPreference mprefServerIpSet;

    private class Scanner extends Handler {
        private int mRetry;

        private Scanner() {
            this.mRetry = 0;
        }

        void resume() {
            if (!hasMessages(0)) {
                sendEmptyMessage(0);
            }
        }

        void pause() {
            this.mRetry = 0;
            ModelWifiConnectTest.this.mAccessPoints.setProgress(false);
            removeMessages(0);
        }

        public void handleMessage(Message message) {
            boolean z = true;
            if (ModelWifiConnectTest.this.mWifiManager.startScan()) {
                this.mRetry = 0;
            } else {
                int i = this.mRetry + 1;
                this.mRetry = i;
                if (i >= 3) {
                    this.mRetry = 0;
                    Toast.makeText(ModelWifiConnectTest.this, 2131296459, 1).show();
                    return;
                }
            }
            ProgressCategory - get0 = ModelWifiConnectTest.this.mAccessPoints;
            if (this.mRetry == 0) {
                z = false;
            }
            -get0.setProgress(z);
            sendEmptyMessageDelayed(0, 6000);
        }
    }

    public ModelWifiConnectTest() {
        this.mFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        this.mFilter.addAction("android.net.wifi.SCAN_RESULTS");
        this.mFilter.addAction("android.net.wifi.NETWORK_IDS_CHANGED");
        this.mFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        this.mFilter.addAction("android.net.wifi.STATE_CHANGE");
        this.mFilter.addAction("android.net.wifi.RSSI_CHANGED");
        this.mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                ModelWifiConnectTest.this.handleEvent(intent);
            }
        };
        this.mScanner = new Scanner();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mWifiManager = (WifiManager) getSystemService("wifi");
        this.mWifiEnabled = this.mWifiManager.isWifiEnabled();
        if (!this.mWifiEnabled) {
            this.mWifiManager.setWifiEnabled(true);
        }
        if (getIntent().getBooleanExtra("only_access_points", false)) {
            addPreferencesFromResource(2130968617);
        } else {
            addPreferencesFromResource(2130968621);
            this.mWifiEnabler = new WifiEnabler(this, (CheckBoxPreference) findPreference("enable_wifi"));
            this.mPrefWifiStatus = (WifiDownloadPreference) findPreference("key_wifi_download_status");
            this.mPrefWifiStatus.setBtnDownloadClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e("WifiSettings_EngineerMode", "in onClick");
                    if (ModelWifiConnectTest.this.mDownloadManager.isDownloading()) {
                        Log.d("WifiSettings_EngineerMode", "onClick(), is downloading");
                        return;
                    }
                    ModelWifiConnectTest.this.mDownloadManager.startDownloading();
                    ModelWifiConnectTest.this.mPrefWifiStatus.setButtonDownloadEnable(false);
                }
            });
            DownloadManager.init(this);
            this.mDownloadManager = DownloadManager.getInstance();
            this.mprefServerIpSet = (EditTextPreference) findPreference("key_wifi_server_ip_set");
            this.mprefServerIpSet.setSummary(this.mDownloadManager.getServerIpAdderssFromSharedPreference());
            this.mprefServerIpSet.setText(this.mDownloadManager.getServerIpAdderssFromSharedPreference());
            this.mprefServerIpSet.setOnPreferenceChangeListener(this);
            this.mprefServerFilepathSet = (EditTextPreference) findPreference("key_wifi_server_filepath_set");
            this.mprefServerFilepathSet.setSummary(this.mDownloadManager.getServerFilepathFromSharedPreference());
            this.mprefServerFilepathSet.setText(this.mDownloadManager.getServerFilepathFromSharedPreference());
            this.mprefServerFilepathSet.setOnPreferenceChangeListener(this);
            this.mWifiDownloadStatusShow = new WifiDownloadStatusShow(this.mPrefWifiStatus, this);
        }
        this.mAccessPoints = (ProgressCategory) findPreference("access_points");
        this.mAccessPoints.setOrderingAsAdded(false);
        this.mAddNetwork = findPreference("add_network");
        registerForContextMenu(getListView());
        this.mForgetListener = new ActionListener() {
            public void onSuccess() {
                Log.e("WifiSettings_EngineerMode", "forget network success");
                ModelWifiConnectTest.this.updateAccessPoints();
                ModelWifiConnectTest.this.saveNetworks();
            }

            public void onFailure(int reason) {
                Log.e("WifiSettings_EngineerMode", "forget network failed");
            }
        };
    }

    protected void onResume() {
        super.onResume();
        if (this.mWifiEnabler != null) {
            this.mWifiEnabler.resume();
        }
        registerReceiver(this.mReceiver, this.mFilter);
        if (this.mKeyStoreNetworkId != -1 && KeyStore.getInstance().state() == State.UNLOCKED) {
            connect(this.mKeyStoreNetworkId);
        }
        this.mKeyStoreNetworkId = -1;
        this.mWifiDownloadStatusShow.onResume();
    }

    protected void onPause() {
        super.onPause();
        if (this.mWifiEnabler != null) {
            this.mWifiEnabler.pause();
        }
        unregisterReceiver(this.mReceiver);
        this.mScanner.pause();
        if (this.mDialog != null) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
        if (this.mResetNetworks) {
            enableNetworks();
        }
        this.mWifiDownloadStatusShow.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mWifiManager.setWifiEnabled(false);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, 2131296464).setIcon(2130837513);
        menu.add(0, 2, 0, 2131296465).setIcon(17301570);
        menu.add(0, 6, 0, 2131296609).setIcon(17301633);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                if (this.mWifiManager.isWifiEnabled()) {
                    this.mScanner.resume();
                }
                return true;
            case Light.CHARGE_RED_LIGHT /*2*/:
                startActivity(new Intent(this, AdvancedSettings.class));
                return true;
            case Light.MAIN_KEY_NORMAL /*6*/:
                if (!this.mDownloadManager.isDownloading()) {
                    this.mDownloadManager.startDownloading();
                    this.mPrefWifiStatus.setButtonDownloadEnable(false);
                    break;
                }
                Log.e("WifiSettings_EngineerMode", "onClick(), is downloading");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo info) {
        if (info instanceof AdapterContextMenuInfo) {
            Preference preference = (Preference) getListView().getItemAtPosition(((AdapterContextMenuInfo) info).position);
            if (preference instanceof AccessPoint) {
                this.mSelected = (AccessPoint) preference;
                menu.setHeaderTitle(this.mSelected.ssid);
                if (this.mSelected.getLevel() != -1 && this.mSelected.getState() == null) {
                    menu.add(0, 3, 0, 2131296466);
                }
                if (this.mSelected.networkId != -1) {
                    menu.add(0, 4, 0, 2131296467);
                    if (this.mSelected.security != 0) {
                        menu.add(0, 5, 0, 2131296468);
                    }
                }
            }
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (this.mSelected == null) {
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                if (this.mSelected.networkId != -1) {
                    if (!requireKeyStore(this.mSelected.getConfig())) {
                        connect(this.mSelected.networkId);
                    }
                } else if (this.mSelected.security == 0) {
                    WifiConfiguration config = new WifiConfiguration();
                    config.SSID = AccessPoint.convertToQuotedString(this.mSelected.ssid);
                    config.allowedKeyManagement.set(0);
                    int networkId = this.mWifiManager.addNetwork(config);
                    this.mWifiManager.enableNetwork(networkId, false);
                    connect(networkId);
                } else {
                    showDialog(this.mSelected, false);
                }
                return true;
            case 4:
                forget(this.mSelected.networkId);
                return true;
            case 5:
                showDialog(this.mSelected, true);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen screen, Preference preference) {
        if (preference instanceof AccessPoint) {
            this.mSelected = (AccessPoint) preference;
            showDialog(this.mSelected, false);
        } else if (preference != this.mAddNetwork) {
            return super.onPreferenceTreeClick(screen, preference);
        } else {
            this.mSelected = null;
            showDialog(null, true);
        }
        return true;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == this.mprefServerIpSet) {
            String ipAddress = (String) newValue;
            if (DownloadManager.isIpAddress(ipAddress)) {
                this.mDownloadManager.setIpAddress(ipAddress);
                preference.setSummary(ipAddress);
                ((EditTextPreference) preference).setText(ipAddress);
            } else {
                Toast.makeText(this, 2131296506, 1).show();
            }
            return true;
        } else if (preference != this.mprefServerFilepathSet) {
            return true;
        } else {
            String filePath = (String) newValue;
            this.mDownloadManager.setFilePath(filePath);
            preference.setSummary(filePath);
            ((EditTextPreference) preference).setText(filePath);
            return true;
        }
    }

    public void onClick(DialogInterface dialogInterface, int button) {
        if (button == -3 && this.mSelected != null) {
            forget(this.mSelected.networkId);
        } else if (button == -1 && this.mDialog != null) {
            WifiConfiguration config = this.mDialog.getConfig();
            if (config == null) {
                if (this.mSelected != null && !requireKeyStore(this.mSelected.getConfig())) {
                    connect(this.mSelected.networkId);
                }
            } else if (config.networkId == -1) {
                int networkId = this.mWifiManager.addNetwork(config);
                if (networkId != -1) {
                    this.mWifiManager.enableNetwork(networkId, false);
                    config.networkId = networkId;
                    if (this.mDialog.edit || requireKeyStore(config)) {
                        saveNetworks();
                    } else {
                        connect(networkId);
                    }
                }
            } else if (this.mSelected != null) {
                this.mWifiManager.updateNetwork(config);
                saveNetworks();
            }
        }
    }

    private void showDialog(AccessPoint accessPoint, boolean edit) {
        if (this.mDialog != null) {
            this.mDialog.dismiss();
        }
        this.mDialog = new WifiDialog(this, this, accessPoint, edit);
        this.mDialog.show();
    }

    private boolean requireKeyStore(WifiConfiguration config) {
        if (!WifiDialog.requireKeyStore(config) || KeyStore.getInstance().state() == State.UNLOCKED) {
            return false;
        }
        this.mKeyStoreNetworkId = config.networkId;
        Credentials.getInstance().unlock(this);
        return true;
    }

    private void forget(int networkId) {
        if (networkId != -1) {
            this.mWifiManager.forget(networkId, this.mForgetListener);
        }
    }

    private void connect(int networkId) {
        if (networkId != -1) {
            WifiConfiguration config;
            if (this.mLastPriority > 1000000) {
                for (int i = this.mAccessPoints.getPreferenceCount() - 1; i >= 0; i--) {
                    AccessPoint accessPoint = (AccessPoint) this.mAccessPoints.getPreference(i);
                    if (accessPoint.networkId != -1) {
                        config = new WifiConfiguration();
                        config.networkId = accessPoint.networkId;
                        config.priority = 0;
                        this.mWifiManager.updateNetwork(config);
                    }
                }
                this.mLastPriority = 0;
            }
            config = new WifiConfiguration();
            config.networkId = networkId;
            int i2 = this.mLastPriority + 1;
            this.mLastPriority = i2;
            config.priority = i2;
            this.mWifiManager.updateNetwork(config);
            saveNetworks();
            this.mWifiManager.enableNetwork(networkId, true);
            this.mWifiManager.reconnect();
            this.mResetNetworks = true;
        }
    }

    private void enableNetworks() {
        for (int i = this.mAccessPoints.getPreferenceCount() - 1; i >= 0; i--) {
            WifiConfiguration config = ((AccessPoint) this.mAccessPoints.getPreference(i)).getConfig();
            if (!(config == null || config.status == 2)) {
                this.mWifiManager.enableNetwork(config.networkId, false);
            }
        }
        this.mResetNetworks = false;
    }

    private void saveNetworks() {
        enableNetworks();
        this.mWifiManager.saveConfiguration();
        updateAccessPoints();
    }

    private void updateAccessPoints() {
        List<AccessPoint> accessPoints = new ArrayList();
        this.mAccessPointsCount = 0;
        List<WifiConfiguration> configs = this.mWifiManager.getConfiguredNetworks();
        if (configs != null) {
            this.mLastPriority = 0;
            for (WifiConfiguration config : configs) {
                if (config.priority > this.mLastPriority) {
                    this.mLastPriority = config.priority;
                }
                if (config.status == 0) {
                    config.status = 2;
                } else if (this.mResetNetworks && config.status == 1) {
                    config.status = 0;
                }
                AccessPoint accessPoint = new AccessPoint((Context) this, config);
                accessPoint.update(this.mLastInfo, this.mLastState);
                accessPoints.add(accessPoint);
            }
        }
        List<ScanResult> results = this.mWifiManager.getScanResults();
        if (results != null) {
            for (ScanResult result : results) {
                if (!(result.SSID == null || result.SSID.length() == 0 || result.capabilities.contains("[IBSS]"))) {
                    boolean found = false;
                    for (AccessPoint accessPoint2 : accessPoints) {
                        if (accessPoint2.update(result)) {
                            found = true;
                        }
                    }
                    if (!found) {
                        accessPoints.add(new AccessPoint((Context) this, result));
                    }
                }
            }
        }
        this.mAccessPoints.removeAll();
        for (AccessPoint accessPoint22 : accessPoints) {
            this.mAccessPoints.addPreference(accessPoint22);
            if ((accessPoint22.getState() == null && accessPoint22.getLevel() == -1) || "NVRAM WARNING: Err = 0x10".equals(accessPoint22.ssid)) {
                Log.d("WifiSettings_EngineerMode", "handleEvent-wifi_not_in_range:" + accessPoint22.ssid);
            } else {
                this.mAccessPointsCount++;
            }
        }
    }

    public static boolean isWifiConnected(WifiInfo wifiInfo) {
        if (wifiInfo == null) {
            return false;
        }
        DetailedState detailState = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
        if ((wifiInfo == null ? 0 : wifiInfo.getIpAddress()) == 0 || DetailedState.OBTAINING_IPADDR != detailState) {
            return false;
        }
        return true;
    }

    private void handleEvent(Intent intent) {
        String action = intent.getAction();
        if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
            updateWifiState(intent.getIntExtra("wifi_state", 4));
        } else if ("android.net.wifi.SCAN_RESULTS".equals(action)) {
            updateAccessPoints();
            if (this.mAccessPointsCount >= this.mMinScapApCount) {
                Log.d("WifiSettings_EngineerMode", "handleEvent-SCAN_RESULTS-mAccessPointsCount=" + this.mAccessPointsCount);
                wifiInfo = this.mWifiManager.getConnectionInfo();
                if (wifiInfo == null || !isWifiConnected(wifiInfo) || wifiInfo.getRssi() <= -72) {
                    updateAccessPoints();
                    return;
                }
                Log.d("WifiSettings_EngineerMode", "handleEvent-SCAN_RESULTS-wifiInfo.getRssi()=" + wifiInfo.getRssi());
                wifiTeStPassed();
            }
        } else if ("android.net.wifi.NETWORK_IDS_CHANGED".equals(action)) {
            if (!(this.mSelected == null || this.mSelected.networkId == -1)) {
                this.mSelected = null;
            }
            updateAccessPoints();
        } else if ("android.net.wifi.supplicant.STATE_CHANGE".equals(action)) {
            updateConnectionState(WifiInfo.getDetailedStateOf((SupplicantState) intent.getParcelableExtra("newState")));
        } else if ("android.net.wifi.STATE_CHANGE".equals(action)) {
            Parcelable parcelableExtra = intent.getParcelableExtra("networkInfo");
            if (parcelableExtra != null) {
                boolean isConnected;
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                updateConnectionState(networkInfo.getDetailedState());
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    isConnected = true;
                } else {
                    isConnected = false;
                }
                if (this.mAccessPointsCount >= this.mMinScapApCount) {
                    Log.d("WifiSettings_EngineerMode", "handleEvent-NETWORK_STATE-mAccessPointsCount=" + this.mAccessPointsCount);
                    if (isConnected) {
                        wifiInfo = this.mWifiManager.getConnectionInfo();
                        if (wifiInfo.getRssi() > -72) {
                            Log.d("WifiSettings_EngineerMode", "handleEvent-NETWORK_STATE-wifiInfo.size()=" + wifiInfo.getRssi());
                            wifiTeStPassed();
                        }
                    }
                }
            }
        } else if ("android.net.wifi.RSSI_CHANGED".equals(action)) {
            updateConnectionState(null);
        }
    }

    private void wifiTeStPassed() {
        this.mWifiManager.setWifiEnabled(false);
        setResult(1);
        finish();
    }

    private void updateConnectionState(DetailedState state) {
        if (this.mWifiManager.isWifiEnabled()) {
            if (state == DetailedState.OBTAINING_IPADDR) {
                this.mScanner.pause();
            } else {
                this.mScanner.resume();
            }
            this.mLastInfo = this.mWifiManager.getConnectionInfo();
            if (state != null) {
                this.mLastState = state;
            }
            for (int i = this.mAccessPoints.getPreferenceCount() - 1; i >= 0; i--) {
                ((AccessPoint) this.mAccessPoints.getPreference(i)).update(this.mLastInfo, this.mLastState);
            }
            if (this.mResetNetworks) {
                if (!(state == DetailedState.CONNECTED || state == DetailedState.DISCONNECTED)) {
                    if (state == DetailedState.FAILED) {
                    }
                }
                updateAccessPoints();
                enableNetworks();
            }
            return;
        }
        this.mScanner.pause();
    }

    private void updateWifiState(int state) {
        if (state == 3) {
            this.mScanner.resume();
            updateAccessPoints();
            return;
        }
        this.mScanner.pause();
        this.mAccessPoints.removeAll();
    }
}
