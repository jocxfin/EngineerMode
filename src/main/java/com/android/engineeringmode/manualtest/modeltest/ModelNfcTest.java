package com.android.engineeringmode.manualtest.modeltest;

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
import android.widget.Button;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.util.ExternFunction;

public class ModelNfcTest extends ModelTest3ItemActivity implements OnClickListener {
    private final boolean AUTO_READ_LABEL_TEST = true;
    private boolean is_read_label_test = true;
    private ExternFunction mExternFun = null;
    private boolean mNFCAdapterHasBind = false;
    private boolean mNeedDisableNfc = false;
    private NfcAdapter mNfcAdapter;
    private Handler mNfcHandler = new Handler() {
        public void handleMessage(Message msg) {
            boolean z = true;
            Log.i("ModelNfcTestActivity", "handleMessage " + msg.what);
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    ModelNfcTest.this.mPcbTextView.setText(ModelNfcTest.this.getPCBNumber());
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    Log.i("ModelNfcTestActivity", "nfc read card time out ");
                    ModelNfcTest.this.tv.setText("FAIL");
                    ModelNfcTest.this.tv.setTextColor(-65536);
                    ModelNfcTest.this.resetView();
                    ModelNfcTest.this.setResultTextView(false);
                    ModelNfcTest.this.unbindNFCAdapter();
                    return;
                case Light.CHARGE_GREEN_LIGHT /*3*/:
                    Log.i("ModelNfcTestActivity", "nfc read card success ");
                    ModelNfcTest.this.mNfcHandler.removeMessages(2);
                    ModelNfcTest.this.tv.setText("PASS");
                    ModelNfcTest.this.tv.setTextColor(-16711936);
                    ModelNfcTest.this.unbindNFCAdapter();
                    ModelNfcTest.this.resetView();
                    ModelNfcTest.this.setResultTextView(true);
                    return;
                case 4096:
                case 4097:
                    ModelNfcTest modelNfcTest = ModelNfcTest.this;
                    if (msg.arg1 <= 0) {
                        z = false;
                    }
                    modelNfcTest.setResultTextView(z);
                    return;
                default:
                    return;
            }
        }
    };
    private TextView mPcbTextView;
    private Button mReadLabel;
    private TextView mRusultTextView;
    private Button pass;
    private TextView tv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903154);
        Log.i("ModelNfcTestActivity", "onCreate");
        findViewById(2131493363).setVisibility(0);
        findViewById(2131493364).setVisibility(8);
        this.tv = (TextView) findViewById(2131493363);
        if (this.mNfcAdapter == null) {
            this.tv.setText("NFC device not avalible");
        } else {
            this.tv.setText(2131297221);
        }
        initResources();
    }

    private void resetView() {
        Log.i("ModelNfcTestActivity", "resetView ");
        findViewById(2131493363).setVisibility(8);
        findViewById(2131493364).setVisibility(0);
        this.mPcbTextView = (TextView) findViewById(2131493365);
        this.mReadLabel = (Button) findViewById(2131493372);
        this.mExternFun = new ExternFunction(this);
        this.mExternFun.registerOnServiceConnected(this.mNfcHandler, 1, null);
        this.pass.setEnabled(true);
        this.mRusultTextView = (TextView) findViewById(2131493374);
        this.mReadLabel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ModelNfcTest.this.setResult(2);
                ModelNfcTest.this.finish();
            }
        });
        ((Button) findViewById(2131493373)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SystemProperties.set("persist.sys.nfc_card_emulation", "1");
                ModelNfcTest.this.mExternFun.setProductLineTestFlagExtraByte(54, (byte) 1);
            }
        });
    }

    private void startReadLableTest() {
        Log.i("ModelNfcTestActivity", "startReadLableTest ");
        this.mNfcHandler.removeMessages(2);
        this.mNfcHandler.sendEmptyMessageDelayed(2, 15000);
        bindNfcAdapterForeground();
    }

    public void onBackPressed() {
        Log.i("ModelNfcTestActivity", "onBackPressed");
        super.onBackPressed();
    }

    private void initResources() {
        Log.i("ModelNfcTestActivity", "initResources");
        this.pass = (Button) findViewById(2131493015);
        this.pass.setEnabled(false);
        this.pass.setOnClickListener(this);
        ((Button) findViewById(2131493236)).setOnClickListener(this);
        ((Button) findViewById(2131493237)).setOnClickListener(this);
    }

    public void onClick(View v) {
        Log.i("ModelNfcTestActivity", "onClick");
        switch (v.getId()) {
            case 2131493015:
                setResult(1);
                finish();
                return;
            case 2131493236:
                setResult(2);
                finish();
                return;
            case 2131493237:
                setResult(3);
                finish();
                return;
            default:
                return;
        }
    }

    protected void onResume() {
        Log.i("ModelNfcTestActivity", "onResume");
        super.onResume();
        this.mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (!(this.mNfcAdapter == null || this.mNfcAdapter.isEnabled())) {
            this.mNfcAdapter.enable();
            this.mNeedDisableNfc = true;
            Log.e("ModelNfcTestActivity", "onResume nfc enable by ModelNfcTest  mNeedDisableNfc= " + this.mNeedDisableNfc);
        }
        if (this.is_read_label_test) {
            this.pass.setEnabled(false);
            startReadLableTest();
            return;
        }
        this.pass.setEnabled(true);
    }

    protected void onPause() {
        Log.i("ModelNfcTestActivity", "onPause");
        super.onPause();
        this.mNfcHandler.removeMessages(2);
        unbindNFCAdapter();
        if (this.mNfcAdapter != null && this.mNeedDisableNfc) {
            this.mNfcAdapter.disable();
            Log.e("ModelNfcTestActivity", "onDestroy nfc disable by ModelNfcTest  mNeedDisableNfc= " + this.mNeedDisableNfc);
            this.mNfcAdapter = null;
        }
    }

    public String getPCBNumber() {
        Log.i("ModelNfcTestActivity", "getPCBNumber");
        String pcbNumber = getString(2131296352) + ":";
        if (this.mExternFun != null) {
            return pcbNumber + this.mExternFun.getPCBNumber();
        }
        return pcbNumber;
    }

    protected void onDestroy() {
        Log.i("ModelNfcTestActivity", "onDestroy");
        super.onDestroy();
        if (this.mExternFun != null) {
            this.mExternFun.unregisterOnServiceConnected(this.mNfcHandler);
            this.mExternFun.dispose();
            this.mExternFun = null;
        }
    }

    private void bindNfcAdapterForeground() {
        Log.i("ModelNfcTestActivity", "bindNfcAdapterForeground");
        IntentFilter[] localIntentFilters = new IntentFilter[]{new IntentFilter("android.nfc.action.TAG_DISCOVERED")};
        Intent intent = new Intent(this, ModelNfcTest.class);
        intent.setFlags(536870912);
        this.mNfcAdapter.enableForegroundDispatch(this, PendingIntent.getActivity(this, 0, intent, 0), localIntentFilters, null);
        this.mNFCAdapterHasBind = true;
    }

    private void unbindNFCAdapter() {
        Log.i("ModelNfcTestActivity", "unbindNFCAdapter");
        if (this.mNFCAdapterHasBind) {
            this.mNfcAdapter.disableForegroundDispatch(this);
            this.mNFCAdapterHasBind = false;
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("ModelNfcTestActivity", "onNewIntent action is : " + intent.getAction());
        if ("android.nfc.action.TAG_DISCOVERED".equals(intent.getAction())) {
            this.is_read_label_test = false;
            SystemProperties.set("persist.sys.nfc_read_label", "1");
            this.mNfcHandler.sendEmptyMessage(3);
        }
    }

    private void setResultTextView(boolean success) {
        Log.i("ModelNfcTestActivity", "setResultTextView");
        this.mRusultTextView.setVisibility(0);
        if (success) {
            this.mRusultTextView.setText("Pass");
            this.mRusultTextView.setTextColor(-16711936);
            return;
        }
        this.mRusultTextView.setText("Fail");
        this.mRusultTextView.setTextColor(-65536);
    }
}
