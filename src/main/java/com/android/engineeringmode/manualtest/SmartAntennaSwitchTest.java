package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.engineeringmode.nvbackup.OemHookManager;
import com.qualcomm.qcrilhook.QcRilHook.ApCmd2ModemType;

public class SmartAntennaSwitchTest extends Activity implements OnClickListener {
    private Button mDefaultButton;
    private Button mSwitchButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903204);
        this.mDefaultButton = (Button) findViewById(2131493541);
        this.mSwitchButton = (Button) findViewById(2131493542);
        this.mDefaultButton.setOnClickListener(this);
        this.mSwitchButton.setOnClickListener(this);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131493541:
                OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_SET_ANTENNA, new byte[]{(byte) 0});
                return;
            case 2131493542:
                OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_SET_ANTENNA, new byte[]{(byte) 1});
                return;
            default:
                return;
        }
    }

    protected void onPause() {
        super.onPause();
        OemHookManager.getInstance().sendOEMcmd(ApCmd2ModemType.AP_2_MODEM_SET_ANTENNA, new byte[]{(byte) 0});
    }
}
