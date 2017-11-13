package com.android.engineeringmode;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.engineeringmode.util.ExternFunction;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

import java.util.ArrayList;

public class IMeiAndPcbCheck extends ListActivity {
    private boolean isMultiSimSupported = false;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mContent;
    private String mEimeiTitle;
    private ExternFunction mExFunction;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d("IMeiAndPcbCheck", "handleMessage msg = " + msg.what);
            if (msg.what == 1) {
                IMeiAndPcbCheck.this.mHandler.sendEmptyMessage(2);
            } else if (msg.what == 2) {
                String imei1 = "";
                String imei2 = "";
                String encryptIMei1 = "";
                String encryptIMei2 = "";
                IMeiAndPcbCheck.this.mContent.clear();
                if (SystemProperties.get("telephony.lteOnCdmaDevice", "").equals("1,1")) {
                    IMeiAndPcbCheck.this.mContent.add(IMeiAndPcbCheck.this.getString(2131297518) + ":");
                    String meid = IMeiAndPcbCheck.this.mExFunction.getMeidNumber();
                    ArrayList - get2 = IMeiAndPcbCheck.this.mContent;
                    if (meid == null) {
                        meid = "";
                    }
                    -get2.add(meid);
                }
                ArrayList - get22;
                Object obj;
                String str;
                if (IMeiAndPcbCheck.this.isMultiSimSupported) {
                    imei1 = IMeiAndPcbCheck.this.getImei(0);
                    imei2 = IMeiAndPcbCheck.this.getImei(1);
                    encryptIMei1 = IMeiAndPcbCheck.this.mExFunction.getEncryptIMei(0);
                    encryptIMei2 = IMeiAndPcbCheck.this.mExFunction.getEncryptIMei(1);
                    IMeiAndPcbCheck.this.mContent.add(IMeiAndPcbCheck.this.getString(2131296348) + ":");
                    -get22 = IMeiAndPcbCheck.this.mContent;
                    if (imei1 == null) {
                        obj = "";
                    } else {
                        str = imei1;
                    }
                    -get22.add(obj);
                    IMeiAndPcbCheck.this.mContent.add(IMeiAndPcbCheck.this.getString(2131296349) + ":");
                    -get22 = IMeiAndPcbCheck.this.mContent;
                    if (imei2 == null) {
                        obj = "";
                    } else {
                        str = imei2;
                    }
                    -get22.add(obj);
                    IMeiAndPcbCheck.this.mContent.add(IMeiAndPcbCheck.this.getString(2131296350) + ":");
                    -get22 = IMeiAndPcbCheck.this.mContent;
                    if (encryptIMei1 == null) {
                        obj = "";
                    } else {
                        str = encryptIMei1;
                    }
                    -get22.add(obj);
                    IMeiAndPcbCheck.this.mContent.add(IMeiAndPcbCheck.this.getString(2131296351) + ":");
                    -get22 = IMeiAndPcbCheck.this.mContent;
                    if (encryptIMei2 == null) {
                        obj = "";
                    } else {
                        str = encryptIMei2;
                    }
                    -get22.add(obj);
                } else {
                    imei1 = IMeiAndPcbCheck.this.getImei(0);
                    encryptIMei1 = IMeiAndPcbCheck.this.mExFunction.getEncryptIMei(0);
                    IMeiAndPcbCheck.this.mContent.add(IMeiAndPcbCheck.this.getString(2131296348) + ":");
                    -get22 = IMeiAndPcbCheck.this.mContent;
                    if (imei1 == null) {
                        obj = "";
                    } else {
                        str = imei1;
                    }
                    -get22.add(obj);
                    IMeiAndPcbCheck.this.mContent.add(IMeiAndPcbCheck.this.getString(2131296350) + ":");
                    -get22 = IMeiAndPcbCheck.this.mContent;
                    if (encryptIMei1 == null) {
                        obj = "";
                    } else {
                        str = encryptIMei1;
                    }
                    -get22.add(obj);
                }
                IMeiAndPcbCheck.this.mHandler.sendEmptyMessage(3);
            } else if (msg.what == 3) {
                IMeiAndPcbCheck.this.mAdapter.notifyDataSetChanged();
                IMeiAndPcbCheck.this.mAdapter.notifyDataSetInvalidated();
            }
        }
    };
    private String mImeiTitle;
    private ListView mListView;
    private Phone mPhone = null;
    private Phone mPhone1 = null;
    private Phone mPhone2 = null;
    private TelephonyManager mTelephonyManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(2131296347);
        this.mListView = getListView();
        this.mImeiTitle = getString(2131296348);
        this.mEimeiTitle = getString(2131296350);
        this.mContent = new ArrayList();
        this.mTelephonyManager = (TelephonyManager) getSystemService("phone");
        this.isMultiSimSupported = isMultiSimEnabled();
        this.mAdapter = new ArrayAdapter(this, 2130903203, 2131493145, this.mContent);
        this.mListView.setAdapter(this.mAdapter);
        this.mExFunction = new ExternFunction(this);
        this.mExFunction.registerOnServiceConnected(this.mHandler, 1, null);
        if (this.isMultiSimSupported) {
            this.mPhone1 = PhoneFactory.getPhone(0);
            this.mPhone2 = PhoneFactory.getPhone(1);
            return;
        }
        this.mPhone = PhoneFactory.getDefaultPhone();
    }

    protected void onDestroy() {
        this.mExFunction.unregisterOnServiceConnected(this.mHandler);
        this.mExFunction.dispose();
        super.onDestroy();
    }

    private boolean isMultiSimEnabled() {
        String multiSimConfig = SystemProperties.get("persist.radio.multisim.config");
        if (multiSimConfig == null) {
            return false;
        }
        boolean equals = (multiSimConfig.equals("dsds") || multiSimConfig.equals("dsda")) ? true : multiSimConfig.equals("tsts");
        return equals;
    }

    private String getImei(int slotId) {
        Log.d("IMeiAndPcbCheck", "getImei(" + slotId + ")");
        String imei = "";
        try {
            imei = (String) TelephonyManager.class.getDeclaredMethod("getImei", new Class[]{Integer.TYPE}).invoke(this.mTelephonyManager, new Object[]{Integer.valueOf(slotId)});
        } catch (Exception e) {
            Log.i("IMeiAndPcbCheck", "getImei() Exception catched");
        }
        Log.d("IMeiAndPcbCheck", "renturn Imei(" + slotId + ")" + imei);
        return imei;
    }
}
