package com.android.engineeringmode.qualcomm;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.engineeringmode.util.ExternFunction;

public class CalibrateStatus extends ListActivity {
    private String[] mCalibrateInfo;
    private ExternFunction mExFunction;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.d("CalibrateStatus", "handleMessage");
                byte[] res = CalibrateStatus.this.mExFunction.getAdjustStatus();
                CalibrateStatus.this.mCalibrateInfo = new String[]{"XoCalFlag", "ThermCalFlag", "WcdmaCalFlag", "GsmCalFlag", "GsmDelayCalFlag", "CdmaCalFlag", "TdScdmaCalFlag", "TdLteCalFlag", "FddLteCalFlag", "NvBackupFlag", "CalToolVer", " SwVer", "CalTime"};
                CalibrateStatus.this.mStatus = new String[CalibrateStatus.this.mCalibrateInfo.length];
                int i;
                if (res == null) {
                    for (i = 0; i < CalibrateStatus.this.mCalibrateInfo.length; i++) {
                        CalibrateStatus.this.mStatus[i] = new String("-1");
                    }
                    Log.e("CalibrateStatus", "res == null");
                } else {
                    for (i = 0; i <= 8; i++) {
                        CalibrateStatus.this.mStatus[i] = Byte.toString(res[i]);
                    }
                    CalibrateStatus.this.mStatus[9] = Byte.toString(res[31]);
                    CalibrateStatus.this.mStatus[10] = new String(res, 32, 32);
                    CalibrateStatus.this.mStatus[11] = new String(res, 64, 32);
                    CalibrateStatus.this.mStatus[12] = new String(res, 96, 16);
                    Log.e("CalibrateStatus", "res.length:" + res.length);
                }
                CalibrateStatus.this.mListView.setAdapter(new CalAdapter(CalibrateStatus.this));
            }
        }
    };
    private ListView mListView;
    private String[] mStatus;

    private class CalAdapter extends BaseAdapter {
        private Context mContext;

        public CalAdapter(Context context) {
            this.mContext = context;
        }

        public int getCount() {
            return CalibrateStatus.this.mCalibrateInfo.length;
        }

        public Object getItem(int position) {
            return CalibrateStatus.this.mCalibrateInfo[position];
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(this.mContext).inflate(17367044, null);
            }
            TextView text1 = (TextView) convertView.findViewById(16908308);
            TextView text2 = (TextView) convertView.findViewById(16908309);
            text1.setTextColor(CalibrateStatus.this.getResources().getColor(17170432));
            text2.setTextColor(CalibrateStatus.this.getResources().getColor(17170432));
            text1.setText(CalibrateStatus.this.mCalibrateInfo[position]);
            text2.setText(CalibrateStatus.this.mStatus[position]);
            return convertView;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mListView = getListView();
        this.mExFunction = new ExternFunction(this);
        this.mExFunction.registerOnServiceConnected(this.mHandler, 1, null);
        this.mListView.setAdapter(new ArrayAdapter(this, 2130903203, 2131493145, new String[]{" please wait : ", "reading"}));
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        this.mExFunction.unregisterOnServiceConnected(this.mHandler);
        this.mExFunction.dispose();
        super.onDestroy();
    }
}
