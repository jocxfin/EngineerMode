package com.android.engineeringmode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.engineeringmode.util.BarCodeHelper;
import com.android.engineeringmode.util.ExternFunction;

public class PcbShow extends Activity {
    private int QR_HEIGHT = 600;
    private int QR_WIDTH = 600;
    private ExternFunction mExternFun;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.d("PcbShow", "handleMessage");
                PcbShow.this.mPcb = PcbShow.this.getPCBNumber();
                Log.d("PcbShow", "handleMessage after " + PcbShow.this.mPcb);
                PcbShow.this.mListView.setAdapter(new ArrayAdapter(PcbShow.this, 2130903203, 2131493145, new String[]{PcbShow.this.title + " : ", PcbShow.this.mPcb}));
                Bitmap code_image = BarCodeHelper.createQRImage(PcbShow.this.mPcb, PcbShow.this.QR_WIDTH, PcbShow.this.QR_HEIGHT);
                if (code_image != null) {
                    PcbShow.this.mImageView.setImageBitmap(code_image);
                }
            }
        }
    };
    private ImageView mImageView;
    private ListView mListView;
    private String mPcb = "";
    private String title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PcbShow", "PcbShow--onCreate");
        setContentView(2130903178);
        this.mListView = (ListView) findViewById(2131493483);
        this.mImageView = (ImageView) findViewById(2131493484);
        this.mExternFun = new ExternFunction(this);
        Log.d("PcbShow", "PcbShow--new ExternFunction(this)");
        this.title = getString(2131296352);
        this.mExternFun.registerOnServiceConnected(this.mHandler, 1, null);
    }

    public String getPCBNumber() {
        return this.mExternFun.getPCBNumber();
    }

    protected void onDestroy() {
        this.mExternFun.unregisterOnServiceConnected(this.mHandler);
        this.mExternFun.dispose();
        super.onDestroy();
    }
}
