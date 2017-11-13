package com.oneplus.TriKeyManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UEventObserver;
import android.os.UEventObserver.UEvent;
import android.util.Slog;

import com.android.engineeringmode.functions.Light;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class TriKeyManager {
    private static int count = 0;
    private int Ueventcount = 0;
    private final Handler mHandler = new Handler(Looper.myLooper(), null, true) {
        public void handleMessage(Message msg) {
            Slog.v("TriKeyManager", "Message.what=" + msg.what + " msg.arg1=" + msg.arg1 + " msg.arg2=" + msg.arg2);
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    TriKeyManager.this.mMode = TriKeyMode.getTriKeyMode(msg.arg1);
                    if (TriKeyManager.this.mTriKeySwitchChangedCallback != null) {
                        TriKeyManager.this.mTriKeySwitchChangedCallback.onTriKeySwitchChanged(TriKeyManager.this.mMode);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private final Object mLock = new Object();
    private TriKeyMode mMode = TriKeyMode.MODE_UNKNOWN;
    private final TriKeySwitchObserver mObserver = new TriKeySwitchObserver();
    private TriKeySwitchChangedCallback mTriKeySwitchChangedCallback;

    public interface TriKeySwitchChangedCallback {
        void onTriKeySwitchChanged(TriKeyMode triKeyMode);
    }

    public enum TriKeyMode {
        MODE_UNKNOWN,
        MODE_MUTE,
        MODE_DO_NOT_DISTURB,
        MODE_NORMAL,
        MODE_MAX_NUM;

        public static TriKeyMode getTriKeyMode(int iTriKeyMode) {
            switch (iTriKeyMode) {
                case 0:
                    return MODE_UNKNOWN;
                case Light.MAIN_KEY_LIGHT /*1*/:
                    return MODE_MUTE;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    return MODE_DO_NOT_DISTURB;
                case Light.CHARGE_GREEN_LIGHT /*3*/:
                    return MODE_NORMAL;
                default:
                    return MODE_UNKNOWN;
            }
        }
    }

    class TriKeySwitchObserver extends UEventObserver {
        private final List<UEventInfo> mUEventInfo = makeObservedUEventList();

        private final class UEventInfo {
            private final String mDevName;

            public UEventInfo(String devName) {
                this.mDevName = devName;
            }

            public String getDevName() {
                return this.mDevName;
            }

            public String getDevPath() {
                return String.format(Locale.US, "/devices/virtual/switch/%s", new Object[]{this.mDevName});
            }

            public String getSwitchStatePath() {
                return String.format(Locale.US, "/sys/class/switch/%s/state", new Object[]{this.mDevName});
            }

            public boolean checkSwitchExists() {
                return new File(getSwitchStatePath()).exists();
            }
        }

        void init() {
            int i;
            synchronized (TriKeyManager.this.mLock) {
                Slog.v("TriKeyManager", "init()  count = " + TriKeyManager.count + "  Ueventcount = " + TriKeyManager.this.Ueventcount);
                char[] buffer = new char[1024];
                for (i = 0; i < this.mUEventInfo.size(); i++) {
                    UEventInfo uei = (UEventInfo) this.mUEventInfo.get(i);
                    try {
                        FileReader file = new FileReader(uei.getSwitchStatePath());
                        int len = file.read(buffer, 0, 1024);
                        file.close();
                        int curState = Integer.valueOf(new String(buffer, 0, len).trim()).intValue();
                        Slog.v("TriKeyManager", "path:" + uei.getSwitchStatePath() + "value:" + curState);
                        if (curState > 0) {
                            updateStateLocked(uei.getDevPath(), uei.getDevName(), curState);
                        }
                    } catch (FileNotFoundException e) {
                        Slog.w("TriKeyManager", uei.getSwitchStatePath() + " not found while attempting to determine initial switch state");
                    } catch (Exception e2) {
                        Slog.e("TriKeyManager", "", e2);
                    }
                }
            }
            if (TriKeyManager.this.Ueventcount <= 0) {
                for (i = 0; i < this.mUEventInfo.size(); i++) {
                    startObserving("DEVPATH=" + ((UEventInfo) this.mUEventInfo.get(i)).getDevPath());
                }
                TriKeyManager triKeyManager = TriKeyManager.this;
                triKeyManager.Ueventcount = triKeyManager.Ueventcount + 1;
            }
        }

        void stop() {
            Slog.v("TriKeyManager", "stop()  Ueventcount = " + TriKeyManager.this.Ueventcount);
            for (int i = 0; i < this.mUEventInfo.size(); i++) {
                UEventInfo uei = (UEventInfo) this.mUEventInfo.get(i);
                stopObserving();
            }
            TriKeyManager triKeyManager = TriKeyManager.this;
            triKeyManager.Ueventcount = triKeyManager.Ueventcount - 1;
        }

        private List<UEventInfo> makeObservedUEventList() {
            List<UEventInfo> retVal = new ArrayList();
            UEventInfo uei = new UEventInfo("tri-state-key");
            if (uei.checkSwitchExists()) {
                retVal.add(uei);
            } else {
                Slog.w("TriKeyManager", "This kernel does not have tri-state-key support");
            }
            return retVal;
        }

        public void onUEvent(UEvent event) {
            Slog.v("TriKeyManager", "TriKeySwitch UEVENT: " + event.toString());
            try {
                String devPath = event.get("DEVPATH");
                String name = event.get("SWITCH_NAME");
                int state = Integer.parseInt(event.get("SWITCH_STATE"));
                synchronized (TriKeyManager.this.mLock) {
                    updateStateLocked(devPath, name, state);
                }
            } catch (NumberFormatException e) {
                Slog.e("TriKeyManager", "Could not parse switch state from event " + event);
            }
        }

        private void updateStateLocked(String devPath, String devName, int state) {
            for (int i = 0; i < this.mUEventInfo.size(); i++) {
                if (devPath.equals(((UEventInfo) this.mUEventInfo.get(i)).getDevPath())) {
                    TriKeyManager.this.updateLocked(devName, state);
                    return;
                }
            }
        }
    }

    public TriKeyManager() {
        this.mObserver.init();
        count++;
    }

    public void setTriKeySwitchChangedCallback(TriKeySwitchChangedCallback triKeySwitchChangedCallback) {
        this.mTriKeySwitchChangedCallback = triKeySwitchChangedCallback;
    }

    public void start() {
        this.mObserver.init();
    }

    public void Stop() {
        this.mObserver.stop();
    }

    private void updateLocked(String newName, int newState) {
        Slog.v("TriKeyManager", "newName=" + newName + " newState=" + newState);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, newState, 0, newName));
    }
}
