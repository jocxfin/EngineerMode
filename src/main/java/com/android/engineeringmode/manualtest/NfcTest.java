package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.util.ExternFunction;

import java.util.Calendar;

public class NfcTest extends Activity {
    private int escTime = 0;
    private boolean isAvalible = false;
    private boolean isInModelTest = false;
    OnClickListener judgeClickLisenter = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case 2131493015:
                    NfcTest.this.setResult(1);
                    NfcTest.this.finish();
                    return;
                case 2131493236:
                    NfcTest.this.setResult(2);
                    NfcTest.this.finish();
                    return;
                case 2131493237:
                    NfcTest.this.setResult(3);
                    NfcTest.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private ExternFunction mExternFun = null;
    private boolean mIsNfcDisable = false;
    private boolean mIsOppoN1 = false;
    private boolean mNeedDisableNfc = false;
    private NfcAdapter mNfcAdapter;
    private Handler mNfcHandler = new Handler() {
        public void handleMessage(Message msg) {
            boolean z = false;
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    NfcTest.this.mPcbTextView.setText(NfcTest.this.getPCBNumber());
                    return;
                case 4096:
                case 4097:
                    NfcTest nfcTest = NfcTest.this;
                    if (msg.arg1 > 0) {
                        z = true;
                    }
                    nfcTest.setResultTextView(z);
                    return;
                default:
                    return;
            }
        }
    };
    private TextView mPcbTextView;
    private boolean mReadCard = true;
    private Button mReadLabel;
    private TextView mRusultTextView;
    private boolean passFlag = false;
    private Runnable timer = new Runnable() {
        public void run() {
            NfcTest nfcTest = NfcTest.this;
            nfcTest.escTime = nfcTest.escTime + 1000;
            if (!NfcTest.this.passFlag) {
                if (NfcTest.this.escTime <= 15000) {
                    NfcTest.this.timerHandler.postDelayed(NfcTest.this.timer, 1000);
                } else {
                    Log.e("NfcTestActivity", "test fail time out");
                    SystemProperties.set("oem.nfc.test", "false");
                    NfcTest.this.tv.setText("FAIL");
                    NfcTest.this.tv.setTextColor(-65536);
                }
            }
        }
    };
    private Handler timerHandler = new Handler();
    private TextView tv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903172);
        this.mIsOppoN1 = true;
        Intent srcIntent = getIntent();
        if (!(srcIntent == null || srcIntent.getExtras() == null || srcIntent.getExtras().getInt("isreadlabelmode") != 1)) {
            this.mIsOppoN1 = false;
        }
        this.mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Log.e("NfcTestActivity", "onCreate " + this.mIsOppoN1);
        if (this.mIsOppoN1) {
            if (!(this.mNfcAdapter == null || this.mNfcAdapter.isEnabled())) {
                this.mNfcAdapter.enable();
                this.mNeedDisableNfc = true;
                Log.e("NfcTestActivity", "onCreate nfc enable by NfcTest  mNeedDisableNfc= " + this.mNeedDisableNfc);
            }
            findViewById(2131493363).setVisibility(8);
            findViewById(2131493364).setVisibility(0);
            this.mPcbTextView = (TextView) findViewById(2131493365);
            this.mExternFun = new ExternFunction(this);
            this.mExternFun.registerOnServiceConnected(this.mNfcHandler, 1, null);
            this.mReadLabel = (Button) findViewById(2131493372);
            this.mRusultTextView = (TextView) findViewById(2131493374);
            this.mReadLabel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    NfcTest.this.startReadTag();
                }
            });
            ((Button) findViewById(2131493373)).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    SystemProperties.set("persist.sys.nfc_card_emulation", "1");
                }
            });
        } else {
            this.tv = (TextView) findViewById(2131493363);
            if (this.mNfcAdapter == null) {
                this.tv.setText("NFC device not avalible");
            } else {
                this.tv.setText(2131297221);
            }
        }
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
        setLisentenersForJudgeButtons();
    }

    protected void onResume() {
        Log.e("NfcTestActivity", "onResume " + this.mIsOppoN1);
        if (this.mIsOppoN1) {
            setResultTextView(SystemProperties.getBoolean("oem.nfc.test", false));
        }
        super.onResume();
        if (!this.mIsOppoN1) {
            this.passFlag = false;
            this.escTime = 0;
            this.timerHandler.post(this.timer);
            bindNfcAdapterForeground();
        } else if (this.mReadCard) {
            this.mReadCard = false;
            startReadTag();
        }
    }

    public String getPCBNumber() {
        String pcbNumber = getString(2131296352) + ":";
        if (this.mExternFun != null) {
            return pcbNumber + this.mExternFun.getPCBNumber();
        }
        return pcbNumber;
    }

    protected void onDestroy() {
        Log.e("NfcTestActivity", "onDestroy " + this.mIsOppoN1);
        if (this.mIsOppoN1 && this.mNfcAdapter != null && this.mNeedDisableNfc) {
            this.mNfcAdapter.disable();
            Log.e("NfcTestActivity", "onDestroy nfc disable by NfcTest  mNeedDisableNfc= " + this.mNeedDisableNfc);
            this.mNfcAdapter = null;
        }
        super.onDestroy();
        if (this.mExternFun != null) {
            this.mExternFun.unregisterOnServiceConnected(this.mNfcHandler);
            this.mExternFun.dispose();
            this.mExternFun = null;
        }
    }

    protected void onPause() {
        Log.e("NfcTestActivity", "onPause " + this.mIsOppoN1);
        if (!this.mIsOppoN1) {
            unbindNFCAdapter();
            this.timerHandler.removeCallbacks(this.timer);
        }
        super.onPause();
    }

    private void bindNfcAdapterForeground() {
        if (this.mNfcAdapter != null) {
            IntentFilter[] localIntentFilters = new IntentFilter[]{new IntentFilter("android.nfc.action.TAG_DISCOVERED")};
            Intent intent = new Intent(this, NfcTest.class);
            intent.setFlags(536870912);
            this.mNfcAdapter.enableForegroundDispatch(this, PendingIntent.getActivity(this, 0, intent, 0), localIntentFilters, null);
        }
    }

    private void unbindNFCAdapter() {
        if (this.mNfcAdapter != null) {
            this.mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("NfcTestActivity", "onNewIntent Action=" + intent.getAction());
        if ("android.nfc.action.TAG_DISCOVERED".equals(intent.getAction())) {
            Calendar now = Calendar.getInstance();
            Log.e("NfcTestActivity", "nfc pass content: " + ((now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--NfcTest--" + "PASS"));
            this.tv.setText("PASS");
            SystemProperties.set("persist.sys.nfc_read_label", "1");
            this.tv.setTextColor(-16711936);
            this.passFlag = true;
            if (this.isInModelTest) {
                setResult(1);
            }
            SystemProperties.set("oem.nfc.test", "true");
            finish();
        }
    }

    private void setResultTextView(boolean success) {
        this.mRusultTextView.setVisibility(0);
        if (success) {
            this.mRusultTextView.setText("Pass");
            this.mRusultTextView.setTextColor(-16711936);
            if (this.isInModelTest) {
                setResult(1);
                finish();
                return;
            }
            return;
        }
        this.mRusultTextView.setText("Fail");
        this.mRusultTextView.setTextColor(-65536);
    }

    private void startReadTag() {
        Log.e("NfcTestActivity", "startReadTag");
        Intent intent = new Intent();
        intent.putExtra("isreadlabelmode", 1);
        intent.setClass(this, NfcTest.class);
        intent.setFlags(268435456);
        startActivity(intent);
    }

    private void setLisentenersForJudgeButtons() {
        if (this.isInModelTest) {
            ((ViewStub) findViewById(2131493190)).setVisibility(0);
            ((Button) findViewById(2131493015)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493236)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493237)).setOnClickListener(this.judgeClickLisenter);
        }
    }
}
