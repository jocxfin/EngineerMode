package com.android.engineeringmode.gps;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.engineeringmode.util.ExternFunction;

import java.util.ArrayList;

public class GpsViewRecords extends ListActivity {
    private ExternFunction mExternFun;
    private byte[] mGpsData;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.d("GpsViewRecords", "handleMessage");
                GpsViewRecords.this.mGpsData = GpsViewRecords.this.mExternFun.getGpsData();
                if (GpsViewRecords.this.mGpsData == null) {
                    GpsViewRecords.this.mGpsData = new byte[4];
                }
                int length = (GpsViewRecords.this.mGpsData.length / 4) * 4;
                ArrayList<String> mDataList = new ArrayList();
                Log.e("GpsViewRecords", "length:" + length);
                int k = 1;
                for (int i = 0; i < length; i += 4) {
                    int prn1 = GpsViewRecords.this.mGpsData[i];
                    int snr1 = GpsViewRecords.this.mGpsData[i + 1];
                    int prn2 = GpsViewRecords.this.mGpsData[i + 2];
                    int snr2 = GpsViewRecords.this.mGpsData[i + 3];
                    StringBuilder sb = new StringBuilder();
                    int k2 = k + 1;
                    sb.append("(").append(k).append(") ");
                    sb.append(prn1).append(" ").append(snr1);
                    sb.append("    ");
                    k = k2 + 1;
                    sb.append("(").append(k2).append(") ");
                    sb.append(prn2).append(" ").append(snr2);
                    mDataList.add(sb.toString());
                }
                GpsViewRecords.this.mListView.setAdapter(new ArrayAdapter(GpsViewRecords.this, 2130903203, 2131493145, mDataList));
            }
        }
    };
    private ListView mListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mListView = getListView();
        this.mExternFun = new ExternFunction(this);
        this.mExternFun.registerOnServiceConnected(this.mHandler, 1, null);
        this.mListView.setAdapter(new ArrayAdapter(this, 2130903203, 2131493145, new String[]{" please wait:", "reading"}));
    }

    protected void onDestroy() {
        this.mExternFun.unregisterOnServiceConnected(this.mHandler);
        this.mExternFun.dispose();
        super.onDestroy();
    }
}
