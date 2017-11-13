package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.android.engineeringmode.manualtest.CoverManager.CoverChangedCallback;
import com.android.engineeringmode.manualtest.CoverManager.Cover_Type;

public class CoverTest extends Activity implements CoverChangedCallback {
    private CoverManager mCoverManager;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean isInModelTest = CoverTest.this.getIntent().getBooleanExtra("model_test", false);
            int line = CoverTest.this.mCoverManager.getCoverTypeNum();
            if (line > 0) {
                CoverTest.this.tv_CoverType.setText("PASS\n" + CoverTest.this.getHintString(line));
                CoverTest.this.tv_CoverType.setTextColor(-16711936);
                if (isInModelTest) {
                    CoverTest.this.setResult(1);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            CoverTest.this.finish();
                        }
                    }, 2000);
                }
            } else if (CoverTest.this.testNum < 7) {
                CoverTest.this.mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                CoverTest.this.tv_CoverType.setText("FAIL\n" + CoverTest.this.getHintString(line));
                CoverTest.this.tv_CoverType.setTextColor(-65536);
                if (isInModelTest) {
                    CoverTest.this.setResult(3);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            CoverTest.this.finish();
                        }
                    }, 2000);
                }
            }
            CoverTest coverTest = CoverTest.this;
            coverTest.testNum = coverTest.testNum + 1;
        }
    };
    private int testNum = 0;
    private String text;
    TextView tv_CoverType;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903079);
        this.mCoverManager = new CoverManager();
        this.mCoverManager.setCoverChangedCallback(this);
        this.tv_CoverType = (TextView) findViewById(2131493017);
        this.text = this.tv_CoverType.getText().toString();
        this.tv_CoverType.setText("");
        this.mHandler.sendEmptyMessageDelayed(0, 3000);
    }

    public void onResume() {
        super.onResume();
        this.mCoverManager.start();
        Log.d("CoverTest", "getCoverType().ordinal()=" + this.mCoverManager.getCoverType().ordinal());
        this.mCoverManager.setCoverChangedCallback(this);
    }

    public void onPause() {
        super.onPause();
        this.mCoverManager.Stop();
    }

    public void onCoverTypeChanged(Cover_Type covertype) {
        try {
            Log.d("CoverTest", "onCoverTypeChanged()");
            Log.d("CoverTest", "getCoverType().ordinal()=" + this.mCoverManager.getCoverType().ordinal());
        } catch (Exception e) {
        }
    }

    private String getHintString(int line) {
        if (line == 0) {
            return getString(2131297469);
        }
        if (line == 1) {
            return getString(2131297470);
        }
        if (line == 2) {
            return getString(2131297471);
        }
        if (line == 3) {
            return getString(2131297472);
        }
        if (line == 4) {
            return getString(2131297473);
        }
        if (line == 5) {
            return getString(2131297474);
        }
        if (line == 6) {
            return getString(2131297475);
        }
        if (line == 7) {
            return getString(2131297476);
        }
        if (line == 8) {
            return getString(2131297477);
        }
        return getString(2131297469);
    }
}
