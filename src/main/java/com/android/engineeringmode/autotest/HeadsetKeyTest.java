package com.android.engineeringmode.autotest;

import android.os.Bundle;
import android.os.Handler;
import android.os.ServiceManager;
import android.util.Log;
import android.view.IWindowManager.Stub;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.KeepScreenOnActivity;

import java.util.HashMap;
import java.util.Map.Entry;

public class HeadsetKeyTest extends KeepScreenOnActivity {
    private boolean isInModelTest = false;
    private int mCurrentTime = 0;
    private Button mExitButton;
    private Handler mHandler = null;
    private HashMap<Integer, String> mKeyValues;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903131);
        setTitle("耳机按键");
        this.mExitButton = (Button) findViewById(2131493266);
        this.mExitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                HeadsetKeyTest.this.finish();
            }
        });
        this.mExitButton.setVisibility(8);
        initHashMap();
        initLayout();
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
        if (this.isInModelTest) {
            this.mHandler = new Handler();
        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("HeadsetKeyTest", "onKeyDown KEY:" + keyCode);
        View v = findViewById(keyCode);
        if (v != null) {
            v.setVisibility(4);
            this.mKeyValues.remove(Integer.valueOf(keyCode));
        }
        refreshTestResult();
        return true;
    }

    private void refreshTestResult() {
        if (this.mKeyValues.size() <= 0) {
            Toast.makeText(this, "all keys have been pressed", 0).show();
            if (this.isInModelTest) {
                setResult(1);
                if (this.mHandler != null) {
                    this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            HeadsetKeyTest.this.finish();
                        }
                    }, 2000);
                    return;
                } else {
                    finish();
                    return;
                }
            }
            finish();
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("HeadsetKeyTest", "onKeyUp KEY:" + keyCode);
        return true;
    }

    private void initHashMap() {
        int[] keysToTest;
        String[] keyValues;
        if (isHasNavigationBar()) {
            keysToTest = new int[]{79, 25, 24};
            keyValues = new String[]{"挂断键", "音量减", "音量加"};
        } else {
            keysToTest = new int[]{79, 25, 24};
            keyValues = new String[]{"挂断键", "音量减", "音量加"};
        }
        this.mKeyValues = new HashMap();
        for (int i = 0; i < keysToTest.length; i++) {
            this.mKeyValues.put(Integer.valueOf(keysToTest[i]), keyValues[i]);
        }
    }

    private void initLayout() {
        int[] layouts = new int[]{2131493257, 2131493258, 2131493259, 2131493260, 2131493261, 2131493262, 2131493263, 2131493264, 2131493265};
        LayoutInflater inflater = LayoutInflater.from(this);
        int i = 0;
        for (Entry<Integer, String> entry : this.mKeyValues.entrySet()) {
            LinearLayout layout = (LinearLayout) findViewById(layouts[i / 1]);
            TextView tv = (TextView) inflater.inflate(2130903091, null);
            tv.setId(((Integer) entry.getKey()).intValue());
            Log.d("HeadsetKeyTest", "initKeyTestLayout:" + entry.getKey());
            tv.setPadding(0, 0, 5, 0);
            tv.setText((CharSequence) entry.getValue());
            Log.d("HeadsetKeyTest", "initKeyTestLayout:" + ((String) entry.getValue()));
            layout.addView(tv);
            i++;
        }
    }

    public boolean isHasNavigationBar() {
        boolean mHasNavigationBar = false;
        try {
            mHasNavigationBar = Stub.asInterface(ServiceManager.getService("window")).hasNavigationBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mHasNavigationBar;
    }
}
