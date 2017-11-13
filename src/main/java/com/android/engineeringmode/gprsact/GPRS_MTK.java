package com.android.engineeringmode.gprsact;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.android.engineeringmode.functions.Light;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

public class GPRS_MTK extends Activity implements OnClickListener {
    private ArrayAdapter<String> SpinnerAdatper;
    private Button mBtnActivate;
    private Button mBtnAttached;
    private Button mBtnDeactivate;
    private Button mBtnDetached;
    private Button mBtnSendData;
    String[] mContextCmdStringArray = new String[]{"3,128,128,0,0,1,1500,\"le3\",\"4e3\",1,0,0", "3,128,128,0,0,1,1500,\"le4\",\"le5\",0,0,0", "3,128,128,0,0,1,1500,\"le3\",\"4e3\",1,0,0", "3,256,256,0,0,1,1500,\"le4\",\"le5\",0,0,0", "3,128,128,0,0,1,1500,\"le4\",\"le5\",0,0,0", "3,256,256,0,0,1,1500,\"le3\",\"4e3\",1,0,0", "3,256,256,0,0,1,1500,\"le3\",\"4e3\",1,0,0", "3,128,128,0,0,1,1500,\"le4\",\"le5\",0,0,0", "3,128,128,0,0,1,1500,\"le4\",\"le5\",0,0,0", "3,128,128,0,0,1,1500,\"le3\",\"4e3\",1,0,0", "3,128,128,0,0,1,1500,\"le6\",\"le5\",0,0,0", "3,128,128,0,0,1,1500,\"le6\",\"le5\",0,0,0", "3,128,128,0,0,1,1500,\"le6\",\"le5\",0,0,0", "3,128,128,0,0,1,1500,\"le4\",\"le5\",0,0,0", "3,256,256,0,0,1,1500,\"le3\",\"4e3\",1,0,0", "3,512,512,0,0,1,1500,\"le4\",\"le5\",0,0,0"};
    private EditText mEditDataLen;
    private boolean mFlag = true;
    private int mPDPContextIndex = 0;
    private int mPDPSelect = 0;
    private RadioGroup mRaGpPDPSelect;
    private RadioGroup mRaGpUsageSelect;
    private Handler mResponseHander = new Handler() {
        public void handleMessage(Message msg) {
            Builder builder;
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    if (msg.obj.exception == null) {
                        builder = new Builder(GPRS_MTK.this);
                        builder.setTitle("GPRS Attached");
                        builder.setMessage("GPRS Attached succeeded.");
                        builder.setPositiveButton("OK", null);
                        builder.create().show();
                        return;
                    }
                    builder = new Builder(GPRS_MTK.this);
                    builder.setTitle("GPRS Attached");
                    builder.setMessage("GPRS Attache failed.");
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    if (((AsyncResult) msg.obj).exception == null) {
                        builder = new Builder(GPRS_MTK.this);
                        builder.setTitle("GPRS Detached");
                        builder.setMessage("GPRS Detached succeeded.");
                        builder.setPositiveButton("OK", null);
                        builder.create().show();
                        return;
                    }
                    builder = new Builder(GPRS_MTK.this);
                    builder.setTitle("GPRS Detached");
                    builder.setMessage("GPRS Detached failed.");
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                    return;
                case Light.CHARGE_GREEN_LIGHT /*3*/:
                    if (((AsyncResult) msg.obj).exception == null && GPRS_MTK.this.mFlag) {
                        builder = new Builder(GPRS_MTK.this);
                        builder.setTitle("PDP Activate");
                        builder.setMessage("PDP Activate succeeded.");
                        builder.setPositiveButton("OK", null);
                        builder.create().show();
                        return;
                    }
                    builder = new Builder(GPRS_MTK.this);
                    builder.setTitle("PDP Activate");
                    builder.setMessage("PDP Activate failed.");
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                    return;
                case 4:
                    if (((AsyncResult) msg.obj).exception == null) {
                        builder = new Builder(GPRS_MTK.this);
                        builder.setTitle("PDP Deactivate");
                        builder.setMessage("PDP Deactivate succeeded.");
                        builder.setPositiveButton("OK", null);
                        builder.create().show();
                        return;
                    }
                    builder = new Builder(GPRS_MTK.this);
                    builder.setTitle("PDP Deactivate");
                    builder.setMessage("PDP Deactivate failed.");
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                    return;
                case 5:
                    if (((AsyncResult) msg.obj).exception == null) {
                        builder = new Builder(GPRS_MTK.this);
                        builder.setTitle("Send Data");
                        builder.setMessage("Send Data succeeded.");
                        builder.setPositiveButton("OK", null);
                        builder.create().show();
                        return;
                    }
                    builder = new Builder(GPRS_MTK.this);
                    builder.setTitle("Send Data");
                    builder.setMessage("Send Data failed.");
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                    return;
                case Light.MAIN_KEY_NORMAL /*6*/:
                    if (((AsyncResult) msg.obj).exception != null) {
                        GPRS_MTK.this.mFlag = false;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private Spinner mSPinnerPDPContext;
    private int mUsageSelect = 0;
    private Phone phone = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903098);
        this.phone = PhoneFactory.getDefaultPhone();
        this.SpinnerAdatper = new ArrayAdapter(this, 17367048);
        this.SpinnerAdatper.setDropDownViewResource(17367049);
        for (int i = 1; i < 15; i++) {
            this.SpinnerAdatper.add("PDP Context " + String.valueOf(i));
        }
        this.SpinnerAdatper.add("PDP Context 30");
        this.SpinnerAdatper.add("PDP Context 31");
        this.mBtnAttached = (Button) findViewById(2131493088);
        this.mBtnDetached = (Button) findViewById(2131493089);
        this.mRaGpPDPSelect = (RadioGroup) findViewById(2131493090);
        this.mRaGpUsageSelect = (RadioGroup) findViewById(2131493095);
        this.mSPinnerPDPContext = (Spinner) findViewById(2131493098);
        this.mBtnActivate = (Button) findViewById(2131493099);
        this.mBtnDeactivate = (Button) findViewById(2131493100);
        this.mEditDataLen = (EditText) findViewById(2131493101);
        this.mBtnSendData = (Button) findViewById(2131493102);
        this.mBtnAttached.setOnClickListener(this);
        this.mBtnDetached.setOnClickListener(this);
        this.mSPinnerPDPContext.setAdapter(this.SpinnerAdatper);
        this.mBtnActivate.setOnClickListener(this);
        this.mBtnDeactivate.setOnClickListener(this);
        this.mBtnSendData.setOnClickListener(this);
        this.mSPinnerPDPContext.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                GPRS_MTK.this.mPDPContextIndex = arg2;
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.mRaGpPDPSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (arg0.getCheckedRadioButtonId() == 2131493091) {
                    GPRS_MTK.this.mPDPSelect = 0;
                    GPRS_MTK.this.mRaGpUsageSelect.clearCheck();
                    GPRS_MTK.this.mRaGpUsageSelect.getChildAt(0).setEnabled(false);
                    GPRS_MTK.this.mRaGpUsageSelect.getChildAt(1).setEnabled(false);
                }
                if (arg0.getCheckedRadioButtonId() == 2131493092) {
                    GPRS_MTK.this.mPDPSelect = 1;
                    GPRS_MTK.this.mRaGpUsageSelect.check(2131493096);
                    GPRS_MTK.this.mRaGpUsageSelect.getChildAt(0).setEnabled(true);
                    GPRS_MTK.this.mRaGpUsageSelect.getChildAt(1).setEnabled(true);
                }
            }
        });
        this.mRaGpUsageSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (arg0.getCheckedRadioButtonId() == 2131493096) {
                    GPRS_MTK.this.mUsageSelect = 0;
                }
                if (arg0.getCheckedRadioButtonId() == 2131493097) {
                    GPRS_MTK.this.mUsageSelect = 1;
                }
            }
        });
        this.mRaGpPDPSelect.check(2131493091);
    }

    public void onClick(View arg0) {
        if (arg0.getId() == this.mBtnAttached.getId()) {
            this.phone.invokeOemRilRequestStrings(new String[]{"AT+CGATT=1", ""}, this.mResponseHander.obtainMessage(1));
        }
        if (arg0.getId() == this.mBtnDetached.getId()) {
            this.phone.invokeOemRilRequestStrings(new String[]{"AT+CGATT=0", ""}, this.mResponseHander.obtainMessage(2));
        }
        if (arg0.getId() == this.mBtnActivate.getId()) {
            String[] ActivateAT;
            this.mFlag = true;
            if (this.mPDPSelect == 0) {
                ActivateAT = new String[]{"AT+CGQMIN=1", ""};
                this.phone.invokeOemRilRequestStrings(ActivateAT, this.mResponseHander.obtainMessage(6));
                ActivateAT[0] = "AT+CGQREQ=1";
                ActivateAT[1] = "";
                this.phone.invokeOemRilRequestStrings(ActivateAT, this.mResponseHander.obtainMessage(6));
                ActivateAT[0] = "AT+CGDCONT=1,\"IP\",\"internet\",\"192.168.1.1\",0,0";
                ActivateAT[1] = "";
                this.phone.invokeOemRilRequestStrings(ActivateAT, this.mResponseHander.obtainMessage(6));
                ActivateAT[0] = "AT+CGEQREQ=1," + this.mContextCmdStringArray[this.mPDPContextIndex];
                ActivateAT[1] = "";
                this.phone.invokeOemRilRequestStrings(ActivateAT, this.mResponseHander.obtainMessage(6));
                ActivateAT[0] = "AT+ACTTEST=1,1";
                ActivateAT[1] = "";
                this.phone.invokeOemRilRequestStrings(ActivateAT, this.mResponseHander.obtainMessage(3));
            }
            if (1 == this.mPDPSelect) {
                ActivateAT = new String[]{"AT+CGQMIN=2", ""};
                this.phone.invokeOemRilRequestStrings(ActivateAT, this.mResponseHander.obtainMessage(6));
                ActivateAT[0] = "AT+CGQREQ=2";
                ActivateAT[1] = "";
                this.phone.invokeOemRilRequestStrings(ActivateAT, this.mResponseHander.obtainMessage(6));
                if (this.mUsageSelect == 0) {
                    ActivateAT[0] = "AT+CGDCONT=2,\"IP\",\"internet\",\"192.168.1.1\",0,0";
                }
                if (1 == this.mUsageSelect) {
                    ActivateAT[0] = "AT+CGDSCONT=2,1,0,0";
                }
                ActivateAT[1] = "";
                this.phone.invokeOemRilRequestStrings(ActivateAT, this.mResponseHander.obtainMessage(6));
                ActivateAT[0] = "AT+CGEQREQ=2," + this.mContextCmdStringArray[this.mPDPContextIndex];
                ActivateAT[1] = "";
                this.phone.invokeOemRilRequestStrings(ActivateAT, this.mResponseHander.obtainMessage(6));
                ActivateAT[0] = "AT+ACTTEST=1,2";
                ActivateAT[1] = "";
                this.phone.invokeOemRilRequestStrings(ActivateAT, this.mResponseHander.obtainMessage(3));
            }
        }
        if (arg0.getId() == this.mBtnDeactivate.getId()) {
            this.mFlag = true;
            String[] DeactivateAT = new String[2];
            if (this.mPDPSelect == 0) {
                DeactivateAT[0] = "AT+ACTTEST=0,1";
            }
            if (1 == this.mPDPSelect) {
                DeactivateAT[0] = "AT+ACTTEST=0,2";
            }
            DeactivateAT[1] = "";
            this.phone.invokeOemRilRequestStrings(DeactivateAT, this.mResponseHander.obtainMessage(4));
        }
        if (arg0.getId() == this.mBtnSendData.getId()) {
            String strDataLength = this.mEditDataLen.getText().toString();
            String[] SendDataAT = new String[2];
            if (this.mPDPSelect == 0) {
                SendDataAT[0] = "AT+CGSDATA=" + strDataLength + ",1";
            }
            if (1 == this.mPDPSelect) {
                SendDataAT[0] = "AT+CGSDATA=" + strDataLength + ",2";
            }
            SendDataAT[1] = "";
            this.phone.invokeOemRilRequestStrings(SendDataAT, this.mResponseHander.obtainMessage(4));
        }
    }
}
