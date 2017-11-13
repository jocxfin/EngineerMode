package com.android.engineeringmode.manualtest;

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

public final class CoverManager {
    private static int count = 0;
    private int Ueventcount = 0;
    private CoverChangedCallback mCoverChangedCallback;
    private Cover_Type mCoverType = Cover_Type.COVER_TYPE_UNKNOWN;
    private final Handler mHandler = new Handler(Looper.myLooper(), null, true) {
        public void handleMessage(Message msg) {
            Slog.v("CoverManager", "Message.what=" + msg.what + " msg.arg1=" + msg.arg1 + " msg.arg2=" + msg.arg2);
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    CoverManager.this.mCoverType = Cover_Type.getCoverType(msg.arg1);
                    if (CoverManager.this.mCoverChangedCallback != null) {
                        CoverManager.this.mCoverChangedCallback.onCoverTypeChanged(CoverManager.this.mCoverType);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private final Object mLock = new Object();
    private final CoverObserver mObserver = new CoverObserver();

    public interface CoverChangedCallback {
        void onCoverTypeChanged(Cover_Type cover_Type);
    }

    class CoverObserver extends UEventObserver {
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
            synchronized (CoverManager.this.mLock) {
                int i;
                Slog.v("CoverManager", "init()  count = " + CoverManager.count + "  Ueventcount = " + CoverManager.this.Ueventcount);
                char[] buffer = new char[1024];
                for (i = 0; i < this.mUEventInfo.size(); i++) {
                    UEventInfo uei = (UEventInfo) this.mUEventInfo.get(i);
                    try {
                        FileReader file = new FileReader(uei.getSwitchStatePath());
                        int len = file.read(buffer, 0, 1024);
                        file.close();
                        int curState = Integer.valueOf(new String(buffer, 0, len).trim()).intValue();
                        Slog.v("CoverManager", "path:" + uei.getSwitchStatePath() + "value:" + curState);
                        if (curState > 0) {
                            updateStateLocked(uei.getDevPath(), uei.getDevName(), curState);
                        }
                    } catch (FileNotFoundException e) {
                        Slog.w("CoverManager", uei.getSwitchStatePath() + " not found while attempting to determine initial switch state");
                    } catch (Exception e2) {
                        Slog.e("CoverManager", "", e2);
                    }
                }
            }
            if (CoverManager.this.Ueventcount <= 0) {
                for (i = 0; i < this.mUEventInfo.size(); i++) {
                    startObserving("DEVPATH=" + ((UEventInfo) this.mUEventInfo.get(i)).getDevPath());
                }
                CoverManager coverManager = CoverManager.this;
                coverManager.Ueventcount = coverManager.Ueventcount + 1;
            }
        }

        void stop() {
            Slog.v("CoverManager", "stop()  Ueventcount = " + CoverManager.this.Ueventcount);
            for (int i = 0; i < this.mUEventInfo.size(); i++) {
                UEventInfo uei = (UEventInfo) this.mUEventInfo.get(i);
                stopObserving();
            }
            CoverManager coverManager = CoverManager.this;
            coverManager.Ueventcount = coverManager.Ueventcount - 1;
        }

        private List<UEventInfo> makeObservedUEventList() {
            List<UEventInfo> retVal = new ArrayList();
            UEventInfo uei = new UEventInfo("switch-theme");
            if (uei.checkSwitchExists()) {
                retVal.add(uei);
            } else {
                Slog.w("CoverManager", "This kernel does not have switch-theme support");
            }
            return retVal;
        }

        public void onUEvent(UEvent event) {
            Slog.v("CoverManager", "CoverSwitch UEVENT: " + event.toString());
            try {
                String devPath = event.get("DEVPATH");
                String name = event.get("SWITCH_NAME");
                int state = Integer.parseInt(event.get("SWITCH_STATE"));
                synchronized (CoverManager.this.mLock) {
                    updateStateLocked(devPath, name, state);
                }
            } catch (NumberFormatException e) {
                Slog.e("CoverManager", "Could not parse switch state from event " + event);
            }
        }

        private void updateStateLocked(String devPath, String devName, int state) {
            for (int i = 0; i < this.mUEventInfo.size(); i++) {
                if (devPath.equals(((UEventInfo) this.mUEventInfo.get(i)).getDevPath())) {
                    CoverManager.this.updateLocked(devName, state);
                    return;
                }
            }
        }
    }

    public enum Cover_Type {
        COVER_TYPE_UNKNOWN,
        COVER_TYPE1,
        COVER_TYPE2,
        COVER_TYPE3,
        COVER_TYPE4,
        COVER_TYPE5,
        COVER_TYPE6,
        COVER_TYPE7,
        COVER_TYPE8,
        COVER_TYPE9,
        COVER_TYPE10,
        COVER_TYPE_MAX_NUM;

        public static Cover_Type getCoverType(int iCover_Type) {
            switch (iCover_Type) {
                case 0:
                    return COVER_TYPE_UNKNOWN;
                case Light.MAIN_KEY_LIGHT /*1*/:
                    return COVER_TYPE1;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    return COVER_TYPE2;
                case Light.CHARGE_GREEN_LIGHT /*3*/:
                    return COVER_TYPE3;
                case 4:
                    return COVER_TYPE4;
                case 5:
                    return COVER_TYPE5;
                case Light.MAIN_KEY_NORMAL /*6*/:
                    return COVER_TYPE6;
                case 7:
                    return COVER_TYPE7;
                case 8:
                    return COVER_TYPE8;
                case 9:
                    return COVER_TYPE9;
                case 10:
                    return COVER_TYPE10;
                default:
                    return COVER_TYPE_UNKNOWN;
            }
        }
    }

    public CoverManager() {
        this.mObserver.init();
        count++;
    }

    public void setCoverChangedCallback(CoverChangedCallback coverChangedCallback) {
        this.mCoverChangedCallback = coverChangedCallback;
    }

    public Cover_Type getCoverType() {
        return this.mCoverType;
    }

    public int getCoverTypeNum() {
        int curState = 0;
        try {
            char[] buffer = new char[1024];
            FileReader file = new FileReader("sys/class/switch/switch-theme/state");
            int len = file.read(buffer, 0, 1024);
            file.close();
            curState = Integer.valueOf(new String(buffer, 0, len).trim()).intValue();
            Slog.v("CoverManager", "path: sys/class/switch/switch-theme/state value:" + curState);
            return curState;
        } catch (FileNotFoundException e) {
            Slog.w("CoverManager", "sys/class/switch/switch-theme/state not found while attempting to determine initial switch state");
            return curState;
        } catch (Exception e2) {
            Slog.e("CoverManager", "", e2);
            return curState;
        }
    }

    public void start() {
        this.mObserver.init();
    }

    public void Stop() {
        this.mObserver.stop();
    }

    private void updateLocked(String newName, int newState) {
        Slog.v("CoverManager", "newName=" + newName + " newState=" + newState);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, newState, 0, newName));
    }
}
