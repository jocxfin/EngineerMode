package com.android.engineeringmode.call;

import android.app.Service;
import android.app.StatusBarManager;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.engineeringmode.call.ExCallManager.Callbacks;
import com.android.engineeringmode.functions.Light;

public class CallTestService extends Service {
    private int call_state = 0;
    private LinearLayout innerLayout = null;
    private boolean isAutoTestMode = false;
    private Object lock = new Object();
    private Button mButton;
    private ExCallManager mExCallManager;
    private ExitCallBack mExitCallBack = null;
    private String mExtraNumber = "";
    private InnerHandler mInnerHandler;
    private IBinder mLocalBinder = new LocalBinder();
    private PowerManager mPowerManager;
    private StatusBarManager mStatusBarManager;
    private TelephonyManager mTelephonyManager;
    private WakeLock mWakelock;
    private LayoutParams params;
    private boolean set_mode_done = false;
    private WindowManager wm = null;

    public interface ExitCallBack {
        void exit();
    }

    private class InnerHandler extends Handler implements Callbacks {
        private InnerHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    CallTestService.this.mExCallManager.endCall();
                    return;
                case 99:
                    CallTestService.this.mExCallManager.call(CallTestService.this.mExtraNumber);
                    return;
                default:
                    return;
            }
        }

        public void onIdle() {
            Log.d("CallTestService", "InnerHandler  onIdle");
            if (CallTestService.this.isAutoTestMode) {
                CallTestService.this.mStatusBarManager.disable(0);
            }
            CallTestService.this.call_state = 2;
        }

        public void onOffhook() {
            Log.d("CallTestService", "InnerHandler  onOffhook");
            if (CallTestService.this.isAutoTestMode && !hasMessages(1)) {
                sendEmptyMessageDelayed(1, 15000);
            }
            CallTestService.this.call_state = 1;
        }
    }

    public class LocalBinder extends Binder {
        public CallTestService getService() {
            return CallTestService.this;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setAutoTestMode(boolean r9) {
        /*
        r8 = this;
        r7 = r8.lock;
        monitor-enter(r7);
        r0 = r8.set_mode_done;	 Catch:{ all -> 0x008a }
        if (r0 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r7);
        return;
    L_0x0009:
        r8.isAutoTestMode = r9;	 Catch:{ all -> 0x008a }
        r0 = r8.isAutoTestMode;	 Catch:{ all -> 0x008a }
        if (r0 == 0) goto L_0x0088;
    L_0x000f:
        r0 = "statusbar";
        r0 = r8.getSystemService(r0);	 Catch:{ all -> 0x008a }
        r0 = (android.app.StatusBarManager) r0;	 Catch:{ all -> 0x008a }
        r8.mStatusBarManager = r0;	 Catch:{ all -> 0x008a }
        r0 = "window";
        r0 = r8.getSystemService(r0);	 Catch:{ all -> 0x008a }
        r0 = (android.view.WindowManager) r0;	 Catch:{ all -> 0x008a }
        r8.wm = r0;	 Catch:{ all -> 0x008a }
        r0 = new android.widget.LinearLayout;	 Catch:{ all -> 0x008a }
        r0.<init>(r8);	 Catch:{ all -> 0x008a }
        r8.innerLayout = r0;	 Catch:{ all -> 0x008a }
        r0 = new android.widget.Button;	 Catch:{ all -> 0x008a }
        r0.<init>(r8);	 Catch:{ all -> 0x008a }
        r8.mButton = r0;	 Catch:{ all -> 0x008a }
        r0 = r8.mButton;	 Catch:{ all -> 0x008a }
        r1 = new com.android.engineeringmode.call.CallTestService$1;	 Catch:{ all -> 0x008a }
        r1.<init>();	 Catch:{ all -> 0x008a }
        r0.setOnTouchListener(r1);	 Catch:{ all -> 0x008a }
        r0 = r8.mButton;	 Catch:{ all -> 0x008a }
        r1 = "CALL TEST\nClick Here To Quit";
        r0.setText(r1);	 Catch:{ all -> 0x008a }
        r0 = r8.mButton;	 Catch:{ all -> 0x008a }
        r1 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r0.setTextSize(r1);	 Catch:{ all -> 0x008a }
        r0 = r8.mButton;	 Catch:{ all -> 0x008a }
        r1 = 17;
        r0.setGravity(r1);	 Catch:{ all -> 0x008a }
        r0 = r8.mButton;	 Catch:{ all -> 0x008a }
        r1 = -1;
        r0.setTextColor(r1);	 Catch:{ all -> 0x008a }
        r6 = new android.widget.LinearLayout$LayoutParams;	 Catch:{ all -> 0x008a }
        r0 = -2;
        r1 = -2;
        r6.<init>(r0, r1);	 Catch:{ all -> 0x008a }
        r0 = r8.innerLayout;	 Catch:{ all -> 0x008a }
        r1 = 16;
        r0.setGravity(r1);	 Catch:{ all -> 0x008a }
        r0 = r8.innerLayout;	 Catch:{ all -> 0x008a }
        r1 = r8.mButton;	 Catch:{ all -> 0x008a }
        r0.addView(r1, r6);	 Catch:{ all -> 0x008a }
        r4 = 1064; // 0x428 float:1.491E-42 double:5.257E-321;
        r0 = new android.view.WindowManager$LayoutParams;	 Catch:{ all -> 0x008a }
        r1 = -1;
        r2 = -1;
        r3 = 2002; // 0x7d2 float:2.805E-42 double:9.89E-321;
        r5 = -3;
        r0.<init>(r1, r2, r3, r4, r5);	 Catch:{ all -> 0x008a }
        r8.params = r0;	 Catch:{ all -> 0x008a }
        r0 = r8.wm;	 Catch:{ all -> 0x008a }
        r1 = r8.innerLayout;	 Catch:{ all -> 0x008a }
        r2 = r8.params;	 Catch:{ all -> 0x008a }
        r0.addView(r1, r2);	 Catch:{ all -> 0x008a }
        r0 = 1;
        r8.set_mode_done = r0;	 Catch:{ all -> 0x008a }
    L_0x0088:
        monitor-exit(r7);
        return;
    L_0x008a:
        r0 = move-exception;
        monitor-exit(r7);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.engineeringmode.call.CallTestService.setAutoTestMode(boolean):void");
    }

    public void onCreate() {
        super.onCreate();
        Log.d("CallTestService", "onCreate()");
        this.mPowerManager = (PowerManager) getSystemService("power");
        this.mWakelock = this.mPowerManager.newWakeLock(805306394, "CallTestService");
        this.mExCallManager = new ExCallManager(this);
        this.mInnerHandler = new InnerHandler();
        this.mTelephonyManager = (TelephonyManager) getSystemService("phone");
    }

    public boolean dialNumber(String number) {
        this.mInnerHandler.removeMessages(99);
        this.mInnerHandler.removeMessages(1);
        this.mInnerHandler.sendEmptyMessageDelayed(99, 1000);
        this.mExtraNumber = number;
        if (this.isAutoTestMode) {
            this.mStatusBarManager.disable(65536);
        }
        return true;
    }

    public void endCall() {
        this.mInnerHandler.removeMessages(99);
        this.mInnerHandler.removeMessages(1);
        this.mExCallManager.endCall();
        this.mExtraNumber = "";
    }

    public void onDestroy() {
        Log.d("CallTestService", "onDestroy()");
        this.mExCallManager.stop();
        if (this.isAutoTestMode) {
            this.wm.removeView(this.innerLayout);
        }
        if (this.isAutoTestMode) {
            this.mStatusBarManager.disable(0);
        }
        if (this.mWakelock != null && this.mWakelock.isHeld()) {
            this.mWakelock.release();
        }
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        Log.d("CallTestService", "onBind");
        this.mWakelock.acquire();
        this.mExCallManager.start();
        this.mExCallManager.setPhoneStaeteCallBacks(this.mInnerHandler);
        this.mExtraNumber = "";
        return this.mLocalBinder;
    }

    public void setExitCallBack(ExitCallBack cb) {
        this.mExitCallBack = cb;
    }
}
