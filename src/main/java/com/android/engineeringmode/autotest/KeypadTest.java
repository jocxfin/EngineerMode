package com.android.engineeringmode.autotest;

import android.content.Intent;
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
import com.oem.util.Feature;
import com.oneplus.TriKeyManager.TriKeyManager;
import com.oneplus.TriKeyManager.TriKeyManager.TriKeyMode;
import com.oneplus.TriKeyManager.TriKeyManager.TriKeySwitchChangedCallback;

import java.util.HashMap;
import java.util.Map.Entry;

public class KeypadTest extends KeepScreenOnActivity implements TriKeySwitchChangedCallback {
    private static boolean mIsOnePlus = false;
    private boolean isInModelTest = false;
    private int mCurrentTime = 0;
    private Button mExitButton;
    private Handler mHandler = null;
    private HashMap<Integer, String> mKeyValues;
    private TriKeyManager mTriKeyManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903131);
        setTitle(2131296692);
        mIsOnePlus = Feature.isThreeStageKeySupported(this);
        this.mExitButton = (Button) findViewById(2131493266);
        this.mExitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                KeypadTest.this.finish();
            }
        });
        this.mExitButton.setVisibility(8);
        if (mIsOnePlus) {
            this.mTriKeyManager = new TriKeyManager();
            this.mTriKeyManager.setTriKeySwitchChangedCallback(this);
        }
        initHashMap();
        initLayout();
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
        if (this.isInModelTest) {
            this.mHandler = new Handler();
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.mTriKeyManager != null && mIsOnePlus) {
            this.mTriKeyManager.start();
            this.mTriKeyManager.setTriKeySwitchChangedCallback(this);
        }
        Intent intent = new Intent("com.oem.intent.action.KEY_LOCK_MODE");
        intent.putExtra("KeyLockMode", 2);
        intent.putExtra("ProcessName", "com.android.engineeringmode");
        sendBroadcast(intent);
    }

    protected void onPause() {
        super.onPause();
        if (this.mTriKeyManager != null && mIsOnePlus) {
            this.mTriKeyManager.Stop();
        }
        Intent intent = new Intent("com.oem.intent.action.KEY_LOCK_MODE");
        intent.putExtra("KeyLockMode", 0);
        intent.putExtra("ProcessName", "com.android.engineeringmode");
        sendBroadcast(intent);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("KeypadTest", "onKeyDown KEY:" + keyCode);
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
                            KeypadTest.this.finish();
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
        Log.d("KeypadTest", "onKeyUp KEY:" + keyCode);
        return true;
    }

    private void initHashMap() {
        int[] keysToTest;
        String[] keyValues;
        if (isHasNavigationBar()) {
            keysToTest = new int[]{26, 25, 24, 1000, 1001, 1002, 3, 4, 187};
            keyValues = new String[]{"power", "vol_down", "vol_up", "normal", "do_not_disturb", "mute", "home", "back", "app_switch"};
        } else {
            keysToTest = new int[]{26, 25, 24, 1000, 1001, 1002, 3, 4, 187};
            keyValues = new String[]{"power", "vol_down", "vol_up", "normal", "do_not_disturb", "mute", "home", "back", "app_switch"};
        }
        if (mIsOnePlus) {
            keysToTest = new int[]{26, 25, 24, 1000, 1001, 1002, 3, 4, 187};
            keyValues = new String[]{"power", "vol_down", "vol_up", "normal", "do_not_disturb", "mute", "home", "back", "app_switch"};
        }
        this.mKeyValues = new HashMap();
        for (int i = 0; i < keysToTest.length; i++) {
            this.mKeyValues.put(Integer.valueOf(keysToTest[i]), keyValues[i]);
        }
    }

    public void onTriKeySwitchChanged(TriKeyMode type) {
        try {
            Log.d("KeypadTest", "onTriKeySwitchChanged:" + type);
            int TriKeyCode = 0;
            int i = this.mCurrentTime + 1;
            this.mCurrentTime = i;
            if (i > 2) {
                if (type == TriKeyMode.MODE_NORMAL) {
                    TriKeyCode = 1000;
                } else if (type == TriKeyMode.MODE_DO_NOT_DISTURB) {
                    TriKeyCode = 1001;
                } else if (type == TriKeyMode.MODE_MUTE) {
                    TriKeyCode = 1002;
                }
                View v = findViewById(TriKeyCode);
                if (v != null) {
                    v.setVisibility(4);
                    this.mKeyValues.remove(Integer.valueOf(TriKeyCode));
                }
                refreshTestResult();
            }
        } catch (Exception e) {
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
            Log.d("KeypadTest", "initKeyTestLayout:" + entry.getKey());
            tv.setPadding(0, 0, 5, 0);
            tv.setText((CharSequence) entry.getValue());
            Log.d("KeypadTest", "initKeyTestLayout:" + ((String) entry.getValue()));
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
