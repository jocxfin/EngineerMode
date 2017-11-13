package com.android.engineeringmode.wifimtk;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;

public class WiFi_Tx extends Activity implements OnClickListener {
    private final int HANDLER_EVENT_GO = 1;
    private final int HANDLER_EVENT_STOP = 2;
    private final int HANDLER_EVENT_TIMER = 3;
    private final long MAX_VALUE = -1;
    private final int MIN_VALUE = 0;
    private final String TAG = "EM_WiFi_Tx";
    private Handler eventHandler;
    private CheckBox mALCCheck;
    private ChannelInfo mChannel;
    private ArrayAdapter<String> mChannelAdatper;
    private Spinner mChannelSpinner;
    private Button mGoBtn;
    private myHandler mHandler;
    String[] mMode = new String[]{"continuous packet tx", "tx output power", "carrier suppression", "local leakage", "enter power off"};
    private ArrayAdapter<String> mModeAdatper;
    private int mModeIndex = 0;
    private Spinner mModeSpinner;
    private EditText mPktEdit;
    private EditText mPktcntEdit;
    private RateInfo mRate;
    private ArrayAdapter<String> mRateAdatper;
    private Spinner mRateSpinner;
    private Button mReadBtn;
    private Button mStopBtn;
    private EditText mTxEdit;
    private Button mWriteBtn;
    private EditText mXTEdit;
    private int u4Antenna = 0;

    class RateInfo {
        private final short EEPROM_RATE_GROUP_CCK = (short) 0;
        private final short EEPROM_RATE_GROUP_OFDM_12_18M = (short) 2;
        private final short EEPROM_RATE_GROUP_OFDM_24_36M = (short) 3;
        private final short EEPROM_RATE_GROUP_OFDM_48_54M = (short) 4;
        private final short EEPROM_RATE_GROUP_OFDM_6_9M = (short) 1;
        private final int[] mRateCfg = new int[]{2, 4, 11, 22, 12, 18, 24, 36, 48, 72, 96, 108};
        int mRateIndex = 0;
        private final short[] mUcRateGroupEep = new short[]{(short) 0, (short) 0, (short) 0, (short) 0, (short) 1, (short) 1, (short) 2, (short) 2, (short) 3, (short) 3, (short) 4, (short) 4};
        private final String[] mpszRate = new String[]{"1M", "2M", "5.5M", "11M", "6M", "9M", "12M", "18M", "24M", "36M", "48M", "54M"};

        RateInfo() {
        }

        int getRateNumber() {
            return this.mpszRate.length;
        }

        int getRateCfg() {
            return this.mRateCfg[this.mRateIndex];
        }

        int getUcRateGroupEep() {
            return this.mUcRateGroupEep[this.mRateIndex];
        }
    }

    class myHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    EMWifi.setATParam(1, 1);
                    WiFi_Tx.this.eventHandler.sendEmptyMessage(3);
                    Log.i("EM_WiFi_Tx", "The Handle event is : HANDLER_EVENT_GO");
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    long[] u4Value = new long[1];
                    Log.i("EM_WiFi_Tx", "The Handle event is : HANDLER_EVENT_STOP");
                    for (int i = 0; i < 100; i++) {
                        EMWifi.setATParam(1, 0);
                        EMWifi.getATParam(1, u4Value);
                        if (u4Value[0] == 0) {
                            WiFi_Tx.this.eventHandler.removeMessages(3);
                            WiFi_Tx.this.mGoBtn.setEnabled(true);
                            return;
                        }
                        SystemClock.sleep(1);
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        int i;
        super.onCreate(savedInstanceState);
        setContentView(2130903235);
        this.mChannelSpinner = (Spinner) findViewById(2131493681);
        this.mPktEdit = (EditText) findViewById(2131493683);
        this.mPktcntEdit = (EditText) findViewById(2131493685);
        this.mTxEdit = (EditText) findViewById(2131493687);
        this.mRateSpinner = (Spinner) findViewById(2131493689);
        this.mModeSpinner = (Spinner) findViewById(2131493691);
        this.mXTEdit = (EditText) findViewById(2131493693);
        this.mWriteBtn = (Button) findViewById(2131493695);
        this.mReadBtn = (Button) findViewById(2131493694);
        this.mALCCheck = (CheckBox) findViewById(2131493696);
        this.mGoBtn = (Button) findViewById(2131493697);
        this.mStopBtn = (Button) findViewById(2131493698);
        this.mWriteBtn.setOnClickListener(this);
        this.mReadBtn.setOnClickListener(this);
        this.mGoBtn.setOnClickListener(this);
        this.mStopBtn.setOnClickListener(this);
        this.mChannel = new ChannelInfo();
        this.mRate = new RateInfo();
        this.mPktcntEdit.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent Event) {
                if (TextUtils.equals(WiFi_Tx.this.mPktcntEdit.getText(), "0")) {
                    Toast.makeText(WiFi_Tx.this, "Do not accept packet count = 0, unlimited on Android", 0).show();
                    WiFi_Tx.this.mPktcntEdit.setText("3000");
                }
                return false;
            }
        });
        this.mHandler = new myHandler();
        this.eventHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Light.CHARGE_GREEN_LIGHT /*3*/:
                        long[] u4Value = new long[1];
                        Log.i("EM_WiFi_Tx", "The Handle event is : HANDLER_EVENT_TIMER");
                        try {
                            if (Long.parseLong(WiFi_Tx.this.mPktcntEdit.getText().toString()) != 0) {
                                EMWifi.getATParam(1, u4Value);
                                if (u4Value[0] == 0) {
                                    removeMessages(3);
                                    WiFi_Tx.this.mGoBtn.setEnabled(true);
                                    break;
                                }
                            }
                            sendEmptyMessageDelayed(3, 1000);
                            break;
                        } catch (NumberFormatException e) {
                            Toast.makeText(WiFi_Tx.this, "invalid input value", 0).show();
                            return;
                        }
                }
            }
        };
        this.mChannelAdatper = new ArrayAdapter(this, 17367048);
        this.mChannelAdatper.setDropDownViewResource(17367049);
        for (i = 0; i < this.mChannel.getChannelNumber(); i++) {
            this.mChannelAdatper.add(this.mChannel.mChannelName[i]);
        }
        this.mChannelSpinner.setAdapter(this.mChannelAdatper);
        this.mChannelSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                WiFi_Tx.this.mChannel.mChannelIndex = arg2;
                Log.i("EM_WiFi_Tx", "The mChannelIndex is : " + arg2);
                EMWifi.setChannel((long) WiFi_Tx.this.mChannel.getChannelFreq());
                Log.i("EM_WiFi_Tx", "Them channel freq =" + WiFi_Tx.this.mChannel.getChannelFreq());
                WiFi_Tx.this.uiUpdateTxPower();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.mRateAdatper = new ArrayAdapter(this, 17367048);
        this.mRateAdatper.setDropDownViewResource(17367049);
        for (i = 0; i < this.mRate.getRateNumber(); i++) {
            this.mRateAdatper.add(this.mRate.mpszRate[i]);
        }
        this.mRateSpinner.setAdapter(this.mRateAdatper);
        this.mRateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                WiFi_Tx.this.mRate.mRateIndex = arg2;
                Log.i("EM_WiFi_Tx", "The mRateIndex is : " + arg2);
                WiFi_Tx.this.uiUpdateTxPower();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.mModeAdatper = new ArrayAdapter(this, 17367048);
        this.mModeAdatper.setDropDownViewResource(17367049);
        for (Object add : this.mMode) {
            this.mModeAdatper.add(add);
        }
        this.mModeSpinner.setAdapter(this.mModeAdatper);
        this.mModeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                WiFi_Tx.this.mModeIndex = arg2;
                Log.i("EM_WiFi_Tx", "The mModeIndex is : " + arg2);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void onClick(View view) {
        Log.d("EM_WiFi_Tx", "view_id = " + view.getId());
        switch (view.getId()) {
            case 2131493694:
                onClickBtnXtalTrimRead();
                return;
            case 2131493695:
                onClickBtnXtaltrimWrite();
                return;
            case 2131493697:
                onClickBtnTxGo();
                return;
            case 2131493698:
                onClickBtnTxStop();
                return;
            default:
                return;
        }
    }

    protected void onStop() {
        super.onStop();
        this.mHandler.removeMessages(3);
    }

    private void uiUpdateTxPower() {
        long[] Gain = new long[3];
        EMWifi.readTxPowerFromEEPromEx((long) this.mChannel.getChannelFreq(), (long) this.mRate.getRateCfg(), Gain, 3);
        long i4TxPwrGain = Gain[0];
        long i4OutputPower = Gain[1];
        long i4targetAlc = Gain[2];
        Log.i("EM_WiFi_Tx", "i4TxPwrGain from uiUpdateTxPower is " + i4TxPwrGain);
        short ucGain = (short) ((int) (511 & i4TxPwrGain));
        if (ucGain == (short) 0 || ucGain == (short) 255) {
            int ucRateGroupEep = this.mRate.getUcRateGroupEep();
            this.mRate.getClass();
            if (ucRateGroupEep <= 0) {
                this.mTxEdit.setText("20");
                return;
            } else {
                this.mTxEdit.setText("22");
                return;
            }
        }
        this.mTxEdit.setText(Long.toHexString((long) ucGain));
    }

    private void onClickBtnXtalTrimRead() {
        long[] val = new long[1];
        EMWifi.getXtalTrimToCr(val);
        Log.d("EM_WiFi_Tx", "VAL=" + val[0]);
        this.mXTEdit.setText(String.valueOf(val[0]));
    }

    private void onClickBtnXtaltrimWrite() {
        try {
            long u4XtalTrim = Long.parseLong(this.mXTEdit.getText().toString());
            Log.d("EM_WiFi_Tx", "u4XtalTrim =" + u4XtalTrim);
            EMWifi.setXtalTrimToCr(u4XtalTrim);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "invalid input value", 0).show();
        }
    }

    private void onClickBtnTxGo() {
        try {
            long u4TxGainVal = Long.parseLong(this.mTxEdit.getText().toString(), 16);
            switch (this.mModeIndex) {
                case 0:
                    try {
                        int i;
                        long pktNum = Long.parseLong(this.mPktEdit.getText().toString());
                        long cntNum = Long.parseLong(this.mPktcntEdit.getText().toString());
                        this.mGoBtn.setEnabled(false);
                        EMWifi.setATParam(2, u4TxGainVal);
                        EMWifi.setATParam(3, (long) this.mRate.mRateIndex);
                        EMWifi.setATParam(4, 0);
                        EMWifi.setATParam(5, (long) this.u4Antenna);
                        EMWifi.setATParam(6, pktNum);
                        EMWifi.setATParam(7, cntNum);
                        EMWifi.setATParam(8, 0);
                        if (this.mALCCheck.isChecked()) {
                            i = 1;
                        } else {
                            i = 0;
                        }
                        EMWifi.setATParam(9, (long) i);
                        EMWifi.setATParam(10, 131072);
                        EMWifi.setATParam(12, -14548988);
                        EMWifi.setATParam(12, 860094470);
                        EMWifi.setATParam(12, 1432748040);
                        EMWifi.setATParam(12, 1431633945);
                        EMWifi.setATParam(12, -1431699429);
                        EMWifi.setATParam(12, -1145372643);
                        EMWifi.setATParam(13, 1);
                        EMWifi.setATParam(14, 2);
                        this.mHandler.sendEmptyMessage(1);
                        break;
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "invalid input value", 0).show();
                        return;
                    }
                case Light.MAIN_KEY_LIGHT /*1*/:
                    EMWifi.setOutputPower((long) this.mRate.getRateCfg(), u4TxGainVal, this.u4Antenna);
                    break;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    int i4ModeType;
                    int rateCfg = this.mRate.getRateCfg();
                    this.mRate.getClass();
                    if (rateCfg <= 0) {
                        i4ModeType = 0;
                    } else {
                        i4ModeType = 1;
                    }
                    EMWifi.setCarrierSuppression((long) i4ModeType, u4TxGainVal, (long) this.u4Antenna);
                    break;
                case Light.CHARGE_GREEN_LIGHT /*3*/:
                    EMWifi.setLocalFrequecy(u4TxGainVal, (long) this.u4Antenna);
                    break;
                case 4:
                    this.mGoBtn.setEnabled(false);
                    EMWifi.setNormalMode();
                    EMWifi.setOutputPin(20, 0);
                    EMWifi.setPnpPower(4);
                    break;
            }
        } catch (NumberFormatException e2) {
            Toast.makeText(this, "invalid input value", 0).show();
        }
    }

    private void onClickBtnTxStop() {
        switch (this.mModeIndex) {
            case 0:
                this.mHandler.sendEmptyMessage(2);
                return;
            case 4:
                EMWifi.setPnpPower(1);
                EMWifi.setTestMode();
                EMWifi.setChannel((long) this.mChannel.getChannelFreq());
                uiUpdateTxPower();
                this.mGoBtn.setEnabled(true);
                return;
            default:
                EMWifi.setStandBy();
                this.mGoBtn.setEnabled(true);
                return;
        }
    }
}
