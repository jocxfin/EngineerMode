package com.android.engineeringmode.wifitest;

import android.content.Context;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.Preference;
import android.view.View;
import android.widget.ImageView;

public class AccessPoint extends Preference {
    private static final int[] STATE_NONE = new int[0];
    private static final int[] STATE_SECURED = new int[]{2130771968};
    private WifiConfiguration mConfig;
    private WifiInfo mInfo;
    private ScanResult mResult;
    private int mRssi;
    private ImageView mSignal;
    private DetailedState mState;
    final int networkId;
    final int security;
    final String ssid;

    static int getSecurity(WifiConfiguration config) {
        int i = 1;
        if (config.allowedKeyManagement.get(1)) {
            return 2;
        }
        if (config.allowedKeyManagement.get(2) || config.allowedKeyManagement.get(3)) {
            return 3;
        }
        if (config.wepKeys[0] == null) {
            i = 0;
        }
        return i;
    }

    private static int getSecurity(ScanResult result) {
        if (result.capabilities.contains("WEP")) {
            return 1;
        }
        if (result.capabilities.contains("PSK")) {
            return 2;
        }
        if (result.capabilities.contains("EAP")) {
            return 3;
        }
        return 0;
    }

    AccessPoint(Context context, WifiConfiguration config) {
        super(context);
        setWidgetLayoutResource(2130903186);
        this.ssid = config.SSID == null ? "" : removeDoubleQuotes(config.SSID);
        this.security = getSecurity(config);
        this.networkId = config.networkId;
        this.mConfig = config;
        this.mRssi = Integer.MAX_VALUE;
    }

    AccessPoint(Context context, ScanResult result) {
        super(context);
        setWidgetLayoutResource(2130903186);
        this.ssid = result.SSID;
        this.security = getSecurity(result);
        this.networkId = -1;
        this.mRssi = result.level;
        this.mResult = result;
    }

    protected void onBindView(View view) {
        setTitle(this.ssid);
        this.mSignal = (ImageView) view.findViewById(2131493492);
        if (this.mRssi == Integer.MAX_VALUE) {
            this.mSignal.setImageDrawable(null);
        } else {
            this.mSignal.setImageResource(2130837533);
            this.mSignal.setImageState(this.security != 0 ? STATE_SECURED : STATE_NONE, true);
        }
        refresh();
        super.onBindView(view);
    }

    public int compareTo(Preference preference) {
        int i = -1;
        if (!(preference instanceof AccessPoint)) {
            return 1;
        }
        AccessPoint other = (AccessPoint) preference;
        if (this.mInfo != other.mInfo) {
            if (this.mInfo == null) {
                i = 1;
            }
            return i;
        } else if ((this.mRssi ^ other.mRssi) < 0) {
            if (this.mRssi == Integer.MAX_VALUE) {
                i = 1;
            }
            return i;
        } else if ((this.networkId ^ other.networkId) < 0) {
            if (this.networkId == -1) {
                i = 1;
            }
            return i;
        } else {
            int difference = WifiManager.compareSignalLevel(other.mRssi, this.mRssi);
            if (difference != 0) {
                return difference;
            }
            return this.ssid.compareToIgnoreCase(other.ssid);
        }
    }

    boolean update(ScanResult result) {
        if (!this.ssid.equals(result.SSID) || this.security != getSecurity(result)) {
            return false;
        }
        if (WifiManager.compareSignalLevel(result.level, this.mRssi) > 0) {
            this.mRssi = result.level;
        }
        return true;
    }

    void update(WifiInfo info, DetailedState state) {
        boolean reorder = false;
        if (info != null && this.networkId != -1 && this.networkId == info.getNetworkId()) {
            reorder = this.mInfo == null;
            this.mRssi = info.getRssi();
            this.mInfo = info;
            this.mState = state;
            refresh();
        } else if (this.mInfo != null) {
            reorder = true;
            this.mInfo = null;
            this.mState = null;
            refresh();
        }
        if (reorder) {
            notifyHierarchyChanged();
        }
    }

    int getLevel() {
        if (this.mRssi == Integer.MAX_VALUE) {
            return -1;
        }
        return WifiManager.calculateSignalLevel(this.mRssi, 5) - 1;
    }

    WifiConfiguration getConfig() {
        return this.mConfig;
    }

    WifiInfo getInfo() {
        return this.mInfo;
    }

    DetailedState getState() {
        return this.mState;
    }

    static String removeDoubleQuotes(String string) {
        int length = string.length();
        if (length > 1 && string.charAt(0) == '\"' && string.charAt(length - 1) == '\"') {
            return string.substring(1, length - 1);
        }
        return string;
    }

    public static String convertToQuotedString(String string) {
        return "\"" + string + "\"";
    }

    public static boolean isHexWepKey(String wepKey) {
        int len = wepKey.length();
        if (len == 10 || len == 26 || len == 58) {
            return isHex(wepKey);
        }
        return false;
    }

    private static boolean isHex(String key) {
        for (int i = key.length() - 1; i >= 0; i--) {
            char c = key.charAt(i);
            if ((c < '0' || c > '9') && ((c < 'A' || c > 'F') && (c < 'a' || c > 'f'))) {
                return false;
            }
        }
        return true;
    }

    private void refresh() {
        if (this.mSignal != null) {
            Context context = getContext();
            this.mSignal.setImageLevel(getLevel());
            StringBuilder sb = new StringBuilder();
            if (this.mState != null) {
                sb.append(Summary.get(context, this.mState));
            } else {
                String status = null;
                if (this.mRssi == Integer.MAX_VALUE) {
                    status = context.getString(2131296487);
                } else if (this.mConfig != null) {
                    status = context.getString(this.mConfig.status == 1 ? 2131296486 : 2131296485);
                }
                if (this.security == 0) {
                    sb.append(status);
                } else {
                    String format = context.getString(status == null ? 2131296488 : 2131296489);
                    String[] type = context.getResources().getStringArray(2131099675);
                    sb.append(String.format(format, new Object[]{type[this.security], status}));
                }
            }
            if (this.mConfig != null) {
                sb.append(",networkId:");
                sb.append(this.mConfig.networkId);
            }
            if (this.mResult != null) {
                sb.append(",");
                sb.append("level:");
                sb.append(this.mResult.level).append("dBm,Frequency:");
                sb.append(this.mResult.frequency).append("MHz");
            }
            if (this.mInfo != null) {
                sb.append(",LinkSpeed:");
                sb.append(this.mInfo.getLinkSpeed()).append("Mbps");
            }
            try {
                if (sb.toString().startsWith("null")) {
                    setSummary(sb.toString().substring(5));
                } else {
                    setSummary(sb.toString());
                }
            } catch (Exception e) {
                setSummary(sb.toString());
            }
        }
    }
}
