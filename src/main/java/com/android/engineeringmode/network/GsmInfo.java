package com.android.engineeringmode.network;

import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.engineeringmode.KeepScreenOnActivity;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

import java.util.ArrayList;

public class GsmInfo extends KeepScreenOnActivity {
    private CellLocation mCell;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.w("GsmInfo", "handleMessage:" + msg.what);
            AsyncResult ar = msg.obj;
            if (msg.what == 1) {
                if (ar.exception == null) {
                    ArrayList<NeighboringCellInfo> cellList = ar.result;
                    ArrayList<String> infoList = new ArrayList();
                    if (cellList != null) {
                        Log.w("GsmInfo", "listsize:" + cellList.size());
                        for (int i = 0; i < cellList.size(); i++) {
                            NeighboringCellInfo ci = (NeighboringCellInfo) cellList.get(i);
                            StringBuilder sb = new StringBuilder();
                            if (GsmInfo.this.mPhoneType == 1) {
                                sb.append("Cid:").append(ci.getCid());
                                sb.append(" Lac:").append(ci.getLac());
                            } else if (GsmInfo.this.mPhoneType == 2) {
                                sb.append(" Psc:").append(ci.getPsc());
                            }
                            sb.append(" Rssi:").append(ci.getRssi());
                            sb.append(" Type:").append(ci.getNetworkType());
                            infoList.add(sb.toString());
                        }
                        GsmInfo.this.mNeighborList.setAdapter(new ArrayAdapter(GsmInfo.this, 2130903203, 2131493145, infoList));
                        return;
                    }
                    Log.e("GsmInfo", "result is null");
                    return;
                }
                Log.e("GsmInfo", "AsyncResult has exception");
            } else if (msg.what == 2) {
                GsmInfo.this.updateNeighborList();
                if (!GsmInfo.this.mStop) {
                    GsmInfo.this.mHandler.sendEmptyMessageDelayed(2, 3000);
                }
            }
        }
    };
    private ListView mNeighborList;
    private Phone mPhone;
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            GsmInfo.this.mSignalStrength = signalStrength;
            GsmInfo.this.updateSignalStrength();
        }
    };
    private int mPhoneType;
    private SignalStrength mSignalStrength;
    private boolean mStop;
    private ListView mUbietyList;
    private TextView mUbietyRssi;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903168);
        this.mUbietyList = (ListView) findViewById(2131493449);
        this.mNeighborList = (ListView) findViewById(2131493450);
        this.mUbietyRssi = (TextView) findViewById(2131493448);
        if (TelephonyManager.getDefault().isMultiSimEnabled()) {
            this.mPhone = PhoneFactory.getPhone(getIntent().getIntExtra("sim_num", 0));
        } else {
            this.mPhone = PhoneFactory.getDefaultPhone();
        }
        this.mCell = this.mPhone.getCellLocation();
        this.mPhoneType = getIntent().getIntExtra("phone_type", 1);
        updateUbietyList(this.mCell);
        updateNeighborList();
        this.mUbietyRssi.setText("Rssi:" + this.mPhone.getSignalStrength().getGsmSignalStrength());
    }

    protected void onStart() {
        super.onStart();
        ((TelephonyManager) getSystemService("phone")).listen(this.mPhoneStateListener, 449);
        this.mHandler.sendEmptyMessageDelayed(2, 3000);
        this.mStop = false;
    }

    protected void onStop() {
        super.onStop();
        this.mStop = true;
        this.mHandler.removeMessages(2);
        ((TelephonyManager) getSystemService("phone")).listen(this.mPhoneStateListener, 0);
    }

    private void updateSignalStrength() {
        this.mUbietyRssi.setText("Rssi:" + this.mSignalStrength.getGsmSignalStrength());
    }

    public void updateUbietyList(CellLocation cell) {
        int cid = 0;
        int lac = 0;
        if (cell != null) {
            if (cell instanceof GsmCellLocation) {
                cid = ((GsmCellLocation) cell).getCid();
                lac = ((GsmCellLocation) cell).getLac();
            } else if (cell instanceof CdmaCellLocation) {
                cid = ((CdmaCellLocation) cell).getBaseStationId();
                lac = ((CdmaCellLocation) cell).getBaseStationLatitude();
            }
        }
        this.mUbietyList.setAdapter(new ArrayAdapter(this, 2130903203, 2131493145, new String[]{new String("cid:" + cid), new String("lac:" + lac)}));
    }

    public void updateNeighborList() {
        this.mPhone.getNeighboringCids(this.mHandler.obtainMessage(1));
    }
}
