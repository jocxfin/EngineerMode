package com.android.engineeringmode.wifimtk;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.engineeringmode.functions.Light;

import java.util.ArrayList;
import java.util.List;

public class WiFi extends Activity implements OnItemClickListener {
    private final String TAG = "WiFi";
    private boolean isInitialized = false;
    private boolean isWifiOpened = false;
    private WifiManager mWifiManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903223);
        ListView WiFi_listView = (ListView) findViewById(2131493595);
        List<String> items = new ArrayList();
        items.add("Tx");
        items.add("Rx");
        items.add("EEPROM");
        items.add("Temperature Sensor");
        items.add("MCR");
        WiFi_listView.setAdapter(new ArrayAdapter(this, 2130903203, items));
        WiFi_listView.setOnItemClickListener(this);
        this.mWifiManager = (WifiManager) getSystemService("wifi");
        if (!this.isInitialized) {
            try {
                if (this.mWifiManager.isWifiEnabled()) {
                    this.isWifiOpened = true;
                    this.mWifiManager.setWifiEnabled(false);
                    while (this.mWifiManager.getWifiState() == 0) {
                        SystemClock.sleep(2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onResume() {
        super.onResume();
        if (!this.isInitialized) {
            if (EMWifi.initial() != 0) {
                new Builder(this).setTitle("Error").setMessage("Please check your wifi state").setPositiveButton("OK", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        WiFi.this.finish();
                    }
                }).show();
                return;
            }
            Log.d("WiFi", "Initialize succeed");
            EMWifi.setTestMode();
            this.isInitialized = true;
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
        Intent intent = new Intent();
        switch (arg2) {
            case 0:
                intent.setClass(this, WiFi_Tx.class);
                break;
            case Light.MAIN_KEY_LIGHT /*1*/:
                intent.setClass(this, WiFi_Rx.class);
                break;
            case Light.CHARGE_RED_LIGHT /*2*/:
                intent.setClass(this, WiFi_EEPROM.class);
                break;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                intent.setClass(this, WiFi_TemperatureSensor.class);
                break;
            case 4:
                intent.setClass(this, WiFi_MCR.class);
                break;
        }
        startActivity(intent);
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        EMWifi.setNormalMode();
        if (this.isInitialized) {
            EMWifi.UnInitial();
            this.isInitialized = false;
        }
        if (this.isWifiOpened) {
            this.mWifiManager.setWifiEnabled(true);
            while (this.mWifiManager.getWifiState() == 2) {
                SystemClock.sleep(2);
            }
            this.isWifiOpened = false;
        }
    }
}
