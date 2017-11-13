package com.android.engineeringmode;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.Chronometer;

import com.android.engineeringmode.nvbackup.OemHookManager;
import com.android.internal.telephony.Phone;
import com.qualcomm.qcrilhook.QcRilHook.ApCmd2ModemType;

public class NetworkSearch_New extends Activity {
    private static String Which_Step = "";
    static boolean needDisableACL = true;
    private final int EVENT_QCRIL_HOOK_READY = 55;
    private Chronometer chronometer;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 55:
                    if ("First".equals(NetworkSearch_New.Which_Step)) {
                        NetworkSearch_New.needDisableACL = true;
                        OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_SET_ANTENNA, new byte[]{(byte) 0, (byte) 2});
                        SystemClock.sleep(300);
                        OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_GET_ANTENNA_POS, new byte[]{(byte) 0, (byte) 2});
                        OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_DISABLE_TUNER_ACL, new byte[]{(byte) 0});
                        Log.e("NetworkSearch", NetworkSearch_New.Which_Step + " 818");
                    } else if ("Second".equals(NetworkSearch_New.Which_Step)) {
                        NetworkSearch_New.needDisableACL = true;
                        OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_SET_ANTENNA, new byte[]{(byte) 1, (byte) 2});
                        SystemClock.sleep(300);
                        OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_GET_ANTENNA_POS, new byte[]{(byte) 0, (byte) 2});
                        OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_DISABLE_TUNER_ACL, new byte[]{(byte) 1});
                        Log.e("NetworkSearch", NetworkSearch_New.Which_Step + " 838");
                    } else if ("Third".equals(NetworkSearch_New.Which_Step)) {
                        NetworkSearch_New.needDisableACL = false;
                        OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_SET_ANTENNA, new byte[]{(byte) 0, (byte) 2});
                        SystemClock.sleep(300);
                        OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_GET_ANTENNA_POS, new byte[]{(byte) 0, (byte) 2});
                        Log.e("NetworkSearch", NetworkSearch_New.Which_Step + " *#*#818#*#*");
                    } else if ("Fourth".equals(NetworkSearch_New.Which_Step)) {
                        NetworkSearch_New.needDisableACL = false;
                        OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_SET_ANTENNA, new byte[]{(byte) 1, (byte) 2});
                        SystemClock.sleep(300);
                        OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_GET_ANTENNA_POS, new byte[]{(byte) 0, (byte) 2});
                        Log.e("NetworkSearch", NetworkSearch_New.Which_Step + " *#*#838#*#*");
                    }
                    Log.e("NetworkSearch", "lock  fdd lte frequenc22222.");
                    NetworkSearch_New.this.mHandler.sendEmptyMessageDelayed(1000, 1000);
                    break;
                case 100:
                    break;
                case 500:
                    if (((AsyncResult) msg.obj).exception != null) {
                        Log.e("NetworkSearch", "set W only preferred error");
                        return;
                    }
                    Log.e("NetworkSearch", "set W only preferred done. Set W/G W preferred mode in 0.1s.");
                    NetworkSearch_New.this.mHandler.sendEmptyMessageDelayed(700, 100);
                    return;
                case 600:
                    if (((AsyncResult) msg.obj).exception != null) {
                        Log.e("NetworkSearch", "set LTE mode error");
                        return;
                    }
                    Log.e("NetworkSearch", "set LTE mode done");
                    NetworkSearch_New.this.mHandler.sendEmptyMessageDelayed(1011, 20000);
                    return;
                case 700:
                    String rf = SystemProperties.get("ro.rf_version", "TDD_FDD_All");
                    Message inner_msg = NetworkSearch_New.this.mHandler.obtainMessage(600);
                    if (rf.equals("TDD_FDD_Eu")) {
                        Log.e("NetworkSearch", rf + ", set NETWORK_MODE_LTE_GSM_WCDMA mode.");
                        NetworkSearch_New.this.mPhone.setPreferredNetworkType(9, inner_msg);
                        return;
                    } else if (rf.equals("TDD_FDD_Am")) {
                        Log.e("NetworkSearch", rf + ", set NETWORK_MODE_LTE_CDMA_EVDO_GSM_WCDMA mode.");
                        NetworkSearch_New.this.mPhone.setPreferredNetworkType(10, inner_msg);
                        return;
                    } else {
                        Log.e("NetworkSearch", rf + ", set NETWORK_MODE_TD_SCDMA_LTE_CDMA_EVDO_GSM_WCDMA mode.");
                        NetworkSearch_New.this.mPhone.setPreferredNetworkType(22, inner_msg);
                        return;
                    }
                case 800:
                    if (((AsyncResult) msg.obj).exception != null) {
                        Log.e("NetworkSearch", "set G only preferred error");
                        return;
                    }
                    Log.e("NetworkSearch", "set G only preferred done. delay 1000s.");
                    NetworkSearch_New.this.mHandler.sendEmptyMessageDelayed(700, 500);
                    return;
                case 900:
                    Log.e("NetworkSearch", "send freq lock msg to modem");
                    NetworkSearch_New.this.mPhone.setBandMode(47, NetworkSearch_New.this.mHandler.obtainMessage(100));
                    return;
                case 1000:
                    Log.e("NetworkSearch", "EVENT_SET_G_ONLY_PREF_MODE");
                    NetworkSearch_New.this.mPhone.setPreferredNetworkType(1, NetworkSearch_New.this.mHandler.obtainMessage(800));
                    return;
                case 1010:
                    Log.e("NetworkSearch", "EVENT_SET_ACL_DISABLE");
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_DISABLE_TUNER_ACL, new byte[]{(byte) 0});
                    OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_DISABLE_TUNER_ACL, new byte[]{(byte) 1});
                    NetworkSearch_New.this.finish();
                    NetworkSearch_New.this.mHandler.removeMessages(1011);
                    return;
                case 1011:
                    Log.e("NetworkSearch", "set LTE mode done time out finish");
                    NetworkSearch_New.this.finish();
                    return;
                case 1012:
                    NetworkSearch_New.this.finish();
                    return;
                default:
                    return;
            }
            if (msg.obj.exception != null) {
                Log.e("NetworkSearch", "TDD freq lock error");
                return;
            }
            Log.e("NetworkSearch", "Set LTE mode in 5000s.");
            NetworkSearch_New.this.mHandler.sendEmptyMessageDelayed(700, 5000);
        }
    };
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String data_len_modem = "data_len_modem";
            String data_modem = "data_modem";
            int cmd = intent.getIntExtra("cmd_type", -1);
            Log.e("NetworkSearch", intent.getAction() + " cmd = " + cmd);
            if (action.equals("qualcomm.intent.action.ACTION_MODEM_IND_EVT")) {
                int len = intent.getIntExtra(data_len_modem, -1);
                byte[] bytes = intent.getByteArrayExtra(data_modem);
                if (cmd == 0 && NetworkSearch_New.needDisableACL) {
                    NetworkSearch_New.this.mHandler.sendEmptyMessage(1010);
                }
            }
        }
    };
    Phone mPhone;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903171);
        this.chronometer = (Chronometer) findViewById(2131493468);
        this.chronometer.start();
        Log.e("NetworkSearch", "Start time");
        Which_Step = getIntent().getStringExtra("Step_Num");
        setTitle("自动搜网 " + Which_Step);
        Log.e("NetworkSearch", "Enter " + Which_Step + " Station");
        Intent intentAction = new Intent(this, NetworkSearchService.class);
        intentAction.putExtra("Step_Num", Which_Step);
        startService(intentAction);
        this.mHandler.sendEmptyMessageDelayed(1012, 10000);
    }

    protected void onStop() {
        super.onStop();
        if (this.chronometer != null) {
            this.chronometer.stop();
            this.chronometer = null;
        }
        this.mHandler.removeMessages(1011);
        finish();
        Log.e("NetworkSearch", "onStop");
    }

    protected void onPause() {
        super.onPause();
        if (this.chronometer != null) {
            this.chronometer.stop();
            this.chronometer = null;
        }
        this.mHandler.removeMessages(1011);
        finish();
        Log.e("NetworkSearch", "onPause");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.e("NetworkSearch", "onDestroy");
    }
}
