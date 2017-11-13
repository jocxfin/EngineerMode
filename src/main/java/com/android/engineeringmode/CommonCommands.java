package com.android.engineeringmode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CommonCommands extends Activity {
    private static final String[] mActions = new String[]{"com.android.engineeringmode.qualcomm.LogSwitch", "com.android.engineeringmode.qualcomm.DiagEnabled", "com.android.engineeringmode.gps.GpsActivity", "com.android.engineeringmode.wifitest.WifiSettings", "com.android.engineeringmode.NetworkSearch", "com.android.engineeringmode.TDSNetworkSearch", "com.android.engineeringmode.WCDMANetworkSearch", "com.android.engineeringmode.LTENetworkSearch", "com.android.engineeringmode.GSMNetworkSearch", "com.android.engineeringmode.bluetoothtest.BluetoothTest", "com.android.engineeringmode.autoaging.AutoAgingMainListActivity", "com.android.engineeringmode.autotest.AutoTest", "com.android.engineeringmode.manualtest.ManualTest", "com.android.engineeringmode.echotest.EchoTest", "com.android.engineeringmode.qualcomm.OtaSwitch", "com.android.engineeringmode.manualtest.MasterClear", "com.android.engineeringmode.KeepSrceenOn", "com.android.engineeringmode.CheckSoftwareInfo", "com.android.engineeringmode.IMeiAndPcbCheck", "com.android.engineeringmode.PcbShow", "com.android.engineeringmode.BackCameraAdjusting", "com.android.engineeringmode.qualcomm.QualCommActivity", ""};
    private static final String[] mCommandsName = new String[]{"*#800#", "*#801#", "*#802#", "*#803#", "*#804#", "*#814# TDSCDMA", "*#824# WCDMA", "*#834# LTE", "*#844# GSM", "*#805#", "*#806#", "*#807#", "*#808#", "*#809#", "*#811#", "*#8778#", "*#99#", "*#6776#", "*#66#", "*#888#", "*#900", "*#268#", "*#1234#"};
    private ListView mListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903076);
        this.mListView = (ListView) findViewById(2131493011);
        this.mListView.setAdapter(new ArrayAdapter(this, 17367046, getData()));
        this.mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                if (CommonCommands.mCommandsName[arg2].equals("*#1234#")) {
                    CommonCommands.this.showOppoVersion(CommonCommands.this);
                    return;
                }
                Intent intent = new Intent(CommonCommands.mActions[arg2]);
                intent.addFlags(268435456);
                CommonCommands.this.startActivity(intent);
            }
        });
    }

    private List<String> getData() {
        if (mActions.length != mCommandsName.length) {
            throw new RuntimeException("please make mActions and mCommandsName corresponding");
        }
        List<String> data = new ArrayList();
        for (Object add : mCommandsName) {
            data.add(add);
        }
        return data;
    }

    private void showOppoVersion(Context context) {
        AlertDialog alert = new Builder(context).setTitle("OppoVersion").setMessage(SystemProperties.get("ro.build.display.id")).setPositiveButton(17039370, null).setCancelable(false).show();
    }
}
