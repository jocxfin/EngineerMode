package com.android.engineeringmode.network;

import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.engineeringmode.KeepScreenOnListActivity;
import com.android.engineeringmode.functions.Light;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneStateIntentReceiver;
import com.qualcomm.qcnvitems.QcNvItems;

import java.io.IOException;
import java.util.ArrayList;

public class NetWorkInfo extends KeepScreenOnListActivity {
    private String CS_PSCover;
    private String Lac;
    private String Plmnid;
    private String Rat;
    String[] cs_ps = new String[]{"CS_ONLY ", "PS_ONLY ", "CS_PS "};
    private ArrayList<String> list;
    private ArrayAdapter<String> mAdapter;
    private CellLocation mCell;
    private Handler mHandler = new Handler() {
        int NETWORK_MODE_GSM_ONLY = 1;
        int NETWORK_MODE_GSM_UMTS = 3;
        int NETWORK_MODE_WCDMA_ONLY = 2;
        int NETWORK_MODE_WCDMA_PREF = 0;

        public void handleMessage(Message msg) {
            Log.w("NetWorkInfo", "handleMessage:" + msg.what);
            AsyncResult ar = msg.obj;
            if (msg.what == 1) {
                if (ar.exception == null) {
                    int type = ((int[]) ar.result)[0];
                    String[] rat_type = new String[]{"WCDMA_PREF", "GSM_ONLY", "WCDMA_ONLY", "GSM_UMTS"};
                    if (type < 0 || type > 3) {
                        NetWorkInfo.this.Rat = "unknown";
                    } else {
                        NetWorkInfo.this.Rat = rat_type[type];
                    }
                } else {
                    Log.e("NetWorkInfo", "AsyncResult has exception");
                    NetWorkInfo.this.Rat = "unknown";
                }
                NetWorkInfo.this.mAdapter.add("Rat:" + NetWorkInfo.this.Rat);
            }
        }
    };
    private ListView mListView;
    private Phone mPhone;
    private PhoneStateIntentReceiver mPhoneStateReceiver;
    private QcNvItems mQcNvItems;
    private TelephonyManager mTelephonyManager;
    private Handler myNVHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.d("NetWorkInfo", "handleMessage");
                try {
                    int pref = NetWorkInfo.this.mQcNvItems.getSrvDomainPref();
                    if (pref < 0 || pref > 2) {
                        NetWorkInfo.this.CS_PSCover = new String("unknown");
                        NetWorkInfo.this.CS_PSCover = NetWorkInfo.this.cs_ps[2];
                        Log.d("NetWorkInfo", "" + NetWorkInfo.this.CS_PSCover);
                        NetWorkInfo.this.list.clear();
                        NetWorkInfo.this.list.add("Plmnid:" + NetWorkInfo.this.Plmnid);
                        NetWorkInfo.this.list.add("Lac:" + NetWorkInfo.this.Lac);
                        NetWorkInfo.this.list.add("CS_PSCover:" + NetWorkInfo.this.CS_PSCover);
                        NetWorkInfo.this.mAdapter.notifyDataSetChanged();
                        NetWorkInfo.this.mAdapter.notifyDataSetInvalidated();
                        NetWorkInfo.this.updateServiceState();
                        NetWorkInfo.this.updateDataState();
                    }
                    NetWorkInfo.this.CS_PSCover = NetWorkInfo.this.cs_ps[pref];
                    Log.d("NetWorkInfo", "" + NetWorkInfo.this.CS_PSCover);
                    NetWorkInfo.this.CS_PSCover = NetWorkInfo.this.cs_ps[2];
                    Log.d("NetWorkInfo", "" + NetWorkInfo.this.CS_PSCover);
                    NetWorkInfo.this.list.clear();
                    NetWorkInfo.this.list.add("Plmnid:" + NetWorkInfo.this.Plmnid);
                    NetWorkInfo.this.list.add("Lac:" + NetWorkInfo.this.Lac);
                    NetWorkInfo.this.list.add("CS_PSCover:" + NetWorkInfo.this.CS_PSCover);
                    NetWorkInfo.this.mAdapter.notifyDataSetChanged();
                    NetWorkInfo.this.mAdapter.notifyDataSetInvalidated();
                    NetWorkInfo.this.updateServiceState();
                    NetWorkInfo.this.updateDataState();
                } catch (IOException e) {
                    Log.e("NetWorkInfo", "Can't read the CS_PSCover");
                    NetWorkInfo.this.CS_PSCover = new String("unknown");
                }
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mListView = getListView();
        int mSimNum = getIntent().getIntExtra("sim_num", 0);
        if (TelephonyManager.getDefault().isMultiSimEnabled()) {
            this.mPhone = PhoneFactory.getPhone(mSimNum);
        } else {
            this.mPhone = PhoneFactory.getDefaultPhone();
        }
        this.mCell = this.mPhone.getCellLocation();
        this.mTelephonyManager = (TelephonyManager) getSystemService("phone");
        this.Plmnid = this.mTelephonyManager.getNetworkOperatorForPhone(mSimNum);
        if ("".equals(this.Plmnid)) {
            this.Plmnid = "-1";
        }
        if (this.mCell != null) {
            if (this.mCell instanceof GsmCellLocation) {
                this.Lac = String.valueOf(((GsmCellLocation) this.mCell).getLac());
            } else if (this.mCell instanceof CdmaCellLocation) {
                this.Lac = String.valueOf(((CdmaCellLocation) this.mCell).getBaseStationLatitude());
            }
        }
        this.mPhone.getPreferredNetworkType(this.mHandler.obtainMessage(1));
        this.mQcNvItems = new QcNvItems(this);
        this.mQcNvItems.registerOnServiceConnected(this.myNVHandler, 1, null);
        this.CS_PSCover = new String("unknown");
        this.list = new ArrayList();
        this.list.add("Plmnid:" + this.Plmnid);
        this.list.add("Lac:" + this.Lac);
        this.list.add("CS_PSCover:" + this.CS_PSCover);
        this.mAdapter = new ArrayAdapter(this, 2130903203, 2131493145, this.list);
        this.mListView.setAdapter(this.mAdapter);
        this.mPhoneStateReceiver = new PhoneStateIntentReceiver(this, this.mHandler);
        this.mPhoneStateReceiver.notifySignalStrength(200);
        this.mPhoneStateReceiver.notifyServiceState(300);
        this.mPhoneStateReceiver.notifyPhoneCallState(100);
    }

    protected void onDestroy() {
        this.mQcNvItems.unregisterOnServiceConnected(this.myNVHandler);
        this.mQcNvItems.dispose();
        super.onDestroy();
    }

    private final void updateServiceState() {
        int state = this.mPhoneStateReceiver.getServiceState().getState();
        Resources r = getResources();
        String display = r.getString(2131296586);
        switch (state) {
            case 0:
                display = r.getString(2131296587);
                break;
            case Light.MAIN_KEY_LIGHT /*1*/:
                display = "   " + r.getString(2131296589);
                break;
            case Light.CHARGE_RED_LIGHT /*2*/:
                display = r.getString(2131296589);
                break;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                display = r.getString(2131296590);
                break;
        }
        this.mAdapter.add("CS_Reg:" + display);
    }

    private final void updateDataState() {
        int state = this.mTelephonyManager.getDataState();
        Resources r = getResources();
        String display = r.getString(2131296586);
        switch (state) {
            case 0:
                display = r.getString(2131296591);
                break;
            case Light.MAIN_KEY_LIGHT /*1*/:
                display = r.getString(2131296592);
                break;
            case Light.CHARGE_RED_LIGHT /*2*/:
                display = r.getString(2131296593);
                break;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                display = r.getString(2131296594);
                break;
        }
        this.mAdapter.add("PS_Reg:" + display);
    }
}
