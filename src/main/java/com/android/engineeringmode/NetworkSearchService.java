package com.android.engineeringmode;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.engineeringmode.nvbackup.OemHookManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.qualcomm.qcrilhook.QcRilHook.ApCmd2ModemType;

import java.lang.ref.WeakReference;

public class NetworkSearchService extends Service {
    private static String Which_Step = "";
    static boolean needDisableACL = true;
    private WakeLock mFullWakeLock;
    private NWSearchHandler mHandler = null;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String data_len_modem = "data_len_modem";
            String data_modem = "data_modem";
            int cmd = intent.getIntExtra("cmd_type", -1);
            NetworkSearchService.logd(intent.getAction() + " cmd = " + cmd);
            if (action.equals("qualcomm.intent.action.ACTION_MODEM_IND_EVT")) {
                int len = intent.getIntExtra(data_len_modem, -1);
                byte[] bytes = intent.getByteArrayExtra(data_modem);
                if (cmd == 0) {
                    NetworkSearchService.this.mHandler.sendEmptyMessage(1010);
                }
            }
        }
    };

    private static class NWSearchHandler extends Handler {
        boolean mOemHookReady = false;
        Phone mPhone;
        private final WeakReference<NetworkSearchService> mService;
        boolean mWaitforHookReady = false;

        class TriggerNWSearch implements Runnable {
            boolean stopself = false;

            TriggerNWSearch(boolean stopself) {
                this.stopself = stopself;
            }

            public void run() {
                NWSearchHandler.this.mPhone.setPreferredNetworkType(1, null);
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                }
                String rf = SystemProperties.get("ro.rf_version", "TDD_FDD_All");
                if (rf.equals("TDD_FDD_Eu")) {
                    NetworkSearchService.logd(rf + ", set NETWORK_MODE_LTE_GSM_WCDMA mode.");
                    NWSearchHandler.this.mPhone.setPreferredNetworkType(9, null);
                } else if (rf.equals("TDD_FDD_Am")) {
                    NetworkSearchService.logd(rf + ", set NETWORK_MODE_LTE_CDMA_EVDO_GSM_WCDMA mode.");
                    NWSearchHandler.this.mPhone.setPreferredNetworkType(10, null);
                } else {
                    NetworkSearchService.logd(rf + ", set NETWORK_MODE_TD_SCDMA_LTE_CDMA_EVDO_GSM_WCDMA mode.");
                    NWSearchHandler.this.mPhone.setPreferredNetworkType(22, null);
                }
                if (this.stopself) {
                    try {
                        ((NetworkSearchService) NWSearchHandler.this.mService.get()).stopSelf();
                    } catch (NullPointerException e2) {
                        NetworkSearchService.logd("Service is already stop.");
                    }
                }
            }
        }

        public NWSearchHandler(NetworkSearchService s) {
            this.mService = new WeakReference(s);
            if (TelephonyManager.getDefault().isMultiSimEnabled()) {
                TelephonyManager tm = CallManager.getPhoneInterface((Context) this.mService.get());
                int status0 = tm.getSimState(0);
                int status1 = tm.getSimState(1);
                if (status0 > 1 || status1 <= 1) {
                    this.mPhone = PhoneFactory.getPhone(0);
                    return;
                } else {
                    this.mPhone = PhoneFactory.getPhone(1);
                    return;
                }
            }
            this.mPhone = PhoneFactory.getDefaultPhone();
        }

        public void handleMessage(Message msg) {
            NetworkSearchService.logd("handleMessage what=" + msg.what);
            switch (msg.what) {
                case 55:
                    this.mOemHookReady = true;
                    if (!this.mWaitforHookReady) {
                        return;
                    }
                    break;
                case 1010:
                    NetworkSearchService.logd("EVENT_SET_ACL_DISABLE, needDisableACL=" + NetworkSearchService.needDisableACL);
                    if (NetworkSearchService.needDisableACL) {
                        OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_DISABLE_TUNER_ACL, new byte[]{(byte) 0});
                        OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_DISABLE_TUNER_ACL, new byte[]{(byte) 1});
                    }
                    removeMessages(1011);
                    new Thread(new TriggerNWSearch(true), "trigger-nw-search-2").start();
                    return;
                case 1011:
                    NetworkSearchService.logd("EVENT_SET_WG_WPERFER_MODE_DONE_FINISH");
                    try {
                        ((NetworkSearchService) this.mService.get()).stopSelf();
                        return;
                    } catch (NullPointerException e) {
                        NetworkSearchService.logd("Service is already stop.");
                        return;
                    }
                case 1012:
                    break;
                default:
                    return;
            }
            if (this.mOemHookReady) {
                if ("First".equals(NetworkSearchService.Which_Step)) {
                    NetworkSearchService.logd(NetworkSearchService.Which_Step + " 818");
                    NetworkSearchService.needDisableACL = true;
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_SET_ANTENNA, new byte[]{(byte) 0, (byte) 2});
                    SystemClock.sleep(300);
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_GET_ANTENNA_POS, new byte[]{(byte) 0, (byte) 2});
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_DISABLE_TUNER_ACL, new byte[]{(byte) 0});
                } else if ("Second".equals(NetworkSearchService.Which_Step)) {
                    NetworkSearchService.logd(NetworkSearchService.Which_Step + " 838");
                    NetworkSearchService.needDisableACL = true;
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_SET_ANTENNA, new byte[]{(byte) 1, (byte) 2});
                    SystemClock.sleep(300);
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_GET_ANTENNA_POS, new byte[]{(byte) 0, (byte) 2});
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_DISABLE_TUNER_ACL, new byte[]{(byte) 1});
                } else if ("Third".equals(NetworkSearchService.Which_Step)) {
                    NetworkSearchService.logd("Fix Prx/Tx on bottom antenna and enable ACL");
                    NetworkSearchService.needDisableACL = false;
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_SET_ANTENNA, new byte[]{(byte) 0, (byte) 2});
                    SystemClock.sleep(300);
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_GET_ANTENNA_POS, new byte[]{(byte) 0, (byte) 2});
                } else if ("Fourth".equals(NetworkSearchService.Which_Step)) {
                    NetworkSearchService.logd("Fix Prx/Tx on top antenna and enable ACL");
                    NetworkSearchService.needDisableACL = false;
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_SET_ANTENNA, new byte[]{(byte) 1, (byte) 2});
                    SystemClock.sleep(300);
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_GET_ANTENNA_POS, new byte[]{(byte) 0, (byte) 2});
                }
                new Thread(new TriggerNWSearch(false), "trigger-nw-search-1").start();
                sendEmptyMessageDelayed(1011, 20000);
                return;
            }
            this.mWaitforHookReady = true;
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.mHandler = new NWSearchHandler(this);
        OemHookManager.getInstance().registerQcRilHookReady(this.mHandler, 55, null);
        IntentFilter filter = new IntentFilter();
        filter.addAction("qualcomm.intent.action.ACTION_MODEM_IND_EVT");
        registerReceiver(this.mIntentReceiver, filter);
        this.mFullWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(26, "NetworkSearchService");
        if (!(this.mFullWakeLock == null || this.mFullWakeLock.isHeld())) {
            this.mFullWakeLock.acquire();
        }
        logd("Service is started");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Which_Step = intent.getStringExtra("Step_Num");
        logd("Enter " + Which_Step + " Station");
        this.mHandler.sendEmptyMessageDelayed(1012, 200);
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        OemHookManager.getInstance().unregisterQcRilHookReady(this.mHandler);
        unregisterReceiver(this.mIntentReceiver);
        this.mHandler = null;
        if (this.mFullWakeLock != null && this.mFullWakeLock.isHeld()) {
            this.mFullWakeLock.release();
            this.mFullWakeLock = null;
        }
        super.onDestroy();
        logd("Service is destroyed");
    }

    private static void logd(String msg) {
        Log.d("NetworkSearchService", msg);
    }
}
