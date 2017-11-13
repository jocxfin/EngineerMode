package com.android.engineeringmode.network;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.engineeringmode.KeepScreenOnActivity;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

public class BandMode2 extends KeepScreenOnActivity {
    private static final String[] BAND_NAMES = new String[]{"Automatic", "EURO Band", "USA Band", "JAPAN Band", "AUS Band", "AUS2 Band", "CELLULAR Band", "PCS Band", "JTACS Band", "KOREAN PCS Band", "450MHZ Band", "2GHZ Band", "UPPER700MHZ Band", "1800MHZ Band", "900MHZ Band", "SEC800MHZ Band", "SEC_EUOROPEAN_PAMR Band", "SEC_AWS Band", "SEC_US_2_5_GHZ Band", "", "", "", "", "", "", "", "", "", "", "", "", "GSM 850", "PGSM 900", "GSM 1800", "GSM 1900", "WCDMA 850", "WCDMA 1900", "WCDMA 2100", "GSM 850/1800", "PGSM 900/1800", "GSM 850/1900", "WCDMA 850/1900", "Hardware Default(China)", "Hardware Default(Foreign)"};
    private ListView mBandList;
    private ArrayAdapter mBandListAdapter;
    private OnItemClickListener mBandSelectionHandler = new OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            BandMode2.this.getWindow().setFeatureInt(5, -1);
            BandMode2.this.mTargetBand = (BandListItem) parent.getAdapter().getItem(position);
            BandMode2.this.log("Select band : " + BandMode2.this.mTargetBand.toString());
            BandMode2.this.mPhone.setBandMode(BandMode2.this.mTargetBand.getBand(), BandMode2.this.mHandler.obtainMessage(200));
        }
    };
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    BandMode2.this.bandListLoaded(msg.obj);
                    return;
                case 200:
                    AsyncResult ar = (AsyncResult) msg.obj;
                    BandMode2.this.getWindow().setFeatureInt(5, -2);
                    BandMode2.this.displayBandSelectionResult(ar.exception);
                    return;
                case 300:
                    int[] band = ((AsyncResult) msg.obj).result;
                    if (band == null || band[0] < 0 || band[0] >= BandMode2.BAND_NAMES.length) {
                        BandMode2.this.setTitle("Unknow");
                        return;
                    } else {
                        BandMode2.this.setTitle(BandMode2.BAND_NAMES[band[0]]);
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private Phone mPhone = null;
    private DialogInterface mProgressPanel;
    private BandListItem mTargetBand = null;

    private static class BandListItem {
        private int mBandMode = 0;

        public BandListItem(int bm) {
            this.mBandMode = bm;
        }

        public int getBand() {
            return this.mBandMode;
        }

        public String toString() {
            if (this.mBandMode >= BandMode2.BAND_NAMES.length) {
                return "Unknow Band";
            }
            return BandMode2.BAND_NAMES[this.mBandMode];
        }
    }

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(2130903061);
        setTitle(getString(2131296661));
        if (TelephonyManager.getDefault().isMultiSimEnabled()) {
            this.mPhone = PhoneFactory.getPhone(getIntent().getIntExtra("sim_num", 0));
        } else {
            this.mPhone = PhoneFactory.getDefaultPhone();
        }
        this.mBandList = (ListView) findViewById(2131492938);
        this.mBandListAdapter = new ArrayAdapter(this, 2130903203);
        this.mBandList.setAdapter(this.mBandListAdapter);
        this.mBandList.setOnItemClickListener(this.mBandSelectionHandler);
        Log.e("BandMode2", "onCreate");
        getBandMode();
        loadBandList();
    }

    private void loadBandList() {
        String str = getString(2131296662);
        log(str);
        this.mProgressPanel = new Builder(this).setMessage(str).show();
        this.mPhone.queryAvailableBandMode(this.mHandler.obtainMessage(100));
    }

    private void getBandMode() {
        this.mPhone.getBandMode(this.mHandler.obtainMessage(300));
    }

    private void bandListLoaded(AsyncResult result) {
        int i;
        log("network list loaded");
        if (this.mProgressPanel != null) {
            this.mProgressPanel.dismiss();
        }
        clearList();
        boolean addBandSuccess = false;
        int[] bands = result.result;
        if (result.result != null && bands.length > 0) {
            int size = bands[0];
            Log.e("BandMode2", "size:" + size);
            if (size > 0) {
                for (i = 1; i <= size; i++) {
                    BandListItem item = new BandListItem(bands[i]);
                    this.mBandListAdapter.add(item);
                    log("Add " + item.toString());
                }
                addBandSuccess = true;
            }
        }
        if (!addBandSuccess) {
            log("Error in query, add default list");
            for (i = 0; i < 19; i++) {
                item = new BandListItem(i);
                this.mBandListAdapter.add(item);
                log("Add default " + item.toString());
            }
        }
        this.mBandList.requestFocus();
    }

    private void displayBandSelectionResult(Throwable ex) {
        String status = getString(2131296663) + " [" + this.mTargetBand.toString() + "] ";
        if (ex != null) {
            status = status + getString(2131296664);
        } else {
            status = status + getString(2131296665);
            getBandMode();
        }
        this.mProgressPanel = new Builder(this).setMessage(status).setPositiveButton(17039370, null).show();
    }

    private void clearList() {
        while (this.mBandListAdapter.getCount() > 0) {
            this.mBandListAdapter.remove(this.mBandListAdapter.getItem(0));
        }
    }

    private void log(String msg) {
        Log.d("phone", "[BandsList] " + msg);
    }
}
