package com.android.engineeringmode.network;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.engineeringmode.AllTest;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

public class BandMode extends Activity {
    private static final String[] BAND_NAMES = new String[]{"Auto", "GSM_850", "GSM_900", "GSM_1800", "GSM_1900", "WCDMA_850", "WCDMA_1900", "WCDMA_2100", "GSM_850/1800", "GSM_900/1800", "GSM_850/1900", "WCDMA_850/1900"};
    private static final int[] BAND_VALUES = new int[]{42, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41};
    private ListView mBandList;
    private ArrayAdapter mBandListAdapter;
    private OnItemClickListener mBandSelectionHandler = new OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            BandMode.this.mPhone.setBandMode(BandMode.BAND_VALUES[position], BandMode.this.mHandler.obtainMessage(200));
            BandMode.this.mbCanExit = false;
        }
    };
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    if (BandMode.this.displayBandSelectionResult(msg.obj.exception)) {
                        sendEmptyMessageDelayed(300, 5000);
                        AllTest.showShortMessage(BandMode.this, 2131296666);
                    }
                    BandMode.this.mbCanExit = true;
                    return;
                default:
                    return;
            }
        }
    };
    private Phone mPhone = null;
    private PowerManager mPowerManager;
    private DialogInterface mProgressPanel;
    private boolean mbCanExit = true;

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(2130903061);
        setTitle(getString(2131296661));
        this.mPhone = PhoneFactory.getDefaultPhone();
        this.mPowerManager = (PowerManager) getSystemService("power");
        this.mBandList = (ListView) findViewById(2131492938);
        this.mBandListAdapter = new ArrayAdapter(this, 2130903203, 2131493145, BAND_NAMES);
        this.mBandList.setAdapter(this.mBandListAdapter);
        this.mBandList.setOnItemClickListener(this.mBandSelectionHandler);
    }

    protected void onDestroy() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(100);
            this.mHandler.removeMessages(200);
            this.mHandler.removeMessages(300);
            this.mHandler = null;
        }
        super.onDestroy();
    }

    private boolean displayBandSelectionResult(Throwable ex) {
        boolean reboot;
        String status = getString(2131296663);
        if (ex != null) {
            status = status + getString(2131296664);
            reboot = false;
        } else {
            status = status + getString(2131296665);
            reboot = true;
        }
        this.mProgressPanel = new Builder(this).setMessage(status).setPositiveButton(17039370, null).show();
        return reboot;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (!this.mbCanExit) {
                    AllTest.showShortMessage(this, 2131296667);
                    break;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
