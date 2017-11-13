package com.android.engineeringmode.nvbackup;

import android.app.Application;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Registrant;
import android.os.RegistrantList;
import android.util.Log;

import com.qualcomm.qcrilhook.QcRilHook;
import com.qualcomm.qcrilhook.QcRilHook.ApCmd2ModemType;
import com.qualcomm.qcrilhook.QcRilHookCallback;

import java.util.Arrays;

public class OemHookManager extends Application {
    private static OemHookManager sInstance;
    private QcRilHook mQcRilHook;
    private QcRilHookCallback mQcRilHookCallback = new QcRilHookCallback() {
        public void onQcRilHookReady() {
            OemHookManager.this.logd("QcRilHook is ready");
            synchronized (OemHookManager.this.mQcRilHookReadyRegistrants) {
                OemHookManager.this.mQcRilHookReady = true;
                OemHookManager.this.mQcRilHookReadyRegistrants.notifyRegistrants();
            }
        }

        public void onQcRilHookDisconnected() {
        }
    };
    private boolean mQcRilHookReady = false;
    private RegistrantList mQcRilHookReadyRegistrants = new RegistrantList();

    public OemHookManager() {
        sInstance = this;
    }

    public static OemHookManager getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        throw new IllegalStateException("no OemHookManager");
    }

    public void onCreate() {
        super.onCreate();
        this.mQcRilHook = new QcRilHook(this, this.mQcRilHookCallback);
    }

    public void registerQcRilHookReady(Handler handler, int what, Object obj) {
        Registrant r = new Registrant(handler, what, obj);
        synchronized (this.mQcRilHookReadyRegistrants) {
            this.mQcRilHookReadyRegistrants.add(r);
            if (this.mQcRilHookReady) {
                r.notifyRegistrant();
            }
        }
    }

    public void unregisterQcRilHookReady(Handler handler) {
        synchronized (this.mQcRilHookReadyRegistrants) {
            this.mQcRilHookReadyRegistrants.remove(handler);
        }
    }

    private void logd(String msg) {
        Log.d("OemHookManager", msg);
    }

    public AsyncResult sendOEMcmd(ApCmd2ModemType cmd, byte[] param) {
        logd("sendOEMcmd, cmd:" + cmd + " param:" + Arrays.toString(param));
        if (this.mQcRilHookReady) {
            return this.mQcRilHook.oemAPSendRequest2Modem(cmd, param, param.length);
        }
        return null;
    }

    public int getSimLockStatus() {
        if (!this.mQcRilHookReady) {
            return -1;
        }
        try {
            return this.mQcRilHook.oemGetQcomSimLockStatus() ? 1 : 0;
        } catch (Exception e) {
            try {
                return this.mQcRilHook.oemGetQcomSimLockStatus() ? 1 : 0;
            } catch (Exception e2) {
                try {
                    return this.mQcRilHook.oemGetQcomSimLockStatus() ? 1 : 0;
                } catch (Exception e22) {
                    e22.printStackTrace();
                    Log.e("OemHookManager", "get oemGetQcomSimLockStatus error");
                    return -1;
                }
            }
        }
    }
}
