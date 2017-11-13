package com.android.engineeringmode.touchscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.engineeringmode.functions.Light;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class TouchScreen extends Activity implements OnItemClickListener {
    public void onCreate(Bundle savedInstanceState) {
        Exception e;
        String strProductName;
        ListView TP_listView;
        List<String> items;
        super.onCreate(savedInstanceState);
        setContentView(2130903214);
        int tempchar = -1;
        try {
            Reader reader = new InputStreamReader(new FileInputStream(new File("/proc/touchpanel/vendor_id")));
            try {
                tempchar = reader.read();
                Log.d("TouchScreen", "" + tempchar);
                reader.close();
                Reader reader2 = reader;
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                strProductName = getResources().getString(2131297045);
                tempchar -= 48;
                if (tempchar != -1) {
                    strProductName = strProductName + getResources().getString(2131297044);
                } else if (tempchar != 1) {
                    strProductName = strProductName + getResources().getString(2131297037);
                } else if (tempchar != 2) {
                    strProductName = strProductName + getResources().getString(2131297038);
                } else if (tempchar != 3) {
                    strProductName = strProductName + getResources().getString(2131297039);
                } else if (tempchar != 4) {
                    strProductName = strProductName + getResources().getString(2131297040);
                } else if (tempchar != 5) {
                    strProductName = strProductName + getResources().getString(2131297041);
                } else if (tempchar != 6) {
                    strProductName = strProductName + getResources().getString(2131297042);
                } else if (tempchar == 7) {
                    strProductName = strProductName + getResources().getString(2131297043);
                }
                TP_listView = (ListView) findViewById(2131493570);
                items = new ArrayList();
                items.add("HandWriting");
                items.add("Verification");
                items.add(getResources().getString(2131297029));
                items.add(strProductName);
                if (TouchScreen_ShellExe.execCommand(new String[]{"/system/bin/sh", "-c", "cat /sys/module/tpd_setting/parameters/tpd_type_cap"}) == 0) {
                    Log.i("MTK", TouchScreen_ShellExe.getOutput());
                    if (TouchScreen_ShellExe.getOutput().equalsIgnoreCase("1")) {
                        items.add("MultiTouch");
                    }
                }
                TP_listView.setAdapter(new ArrayAdapter(this, 2130903203, items));
                TP_listView.setOnItemClickListener(this);
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            strProductName = getResources().getString(2131297045);
            tempchar -= 48;
            if (tempchar != -1) {
                strProductName = strProductName + getResources().getString(2131297044);
            } else if (tempchar != 1) {
                strProductName = strProductName + getResources().getString(2131297037);
            } else if (tempchar != 2) {
                strProductName = strProductName + getResources().getString(2131297038);
            } else if (tempchar != 3) {
                strProductName = strProductName + getResources().getString(2131297039);
            } else if (tempchar != 4) {
                strProductName = strProductName + getResources().getString(2131297040);
            } else if (tempchar != 5) {
                strProductName = strProductName + getResources().getString(2131297041);
            } else if (tempchar != 6) {
                strProductName = strProductName + getResources().getString(2131297042);
            } else if (tempchar == 7) {
                strProductName = strProductName + getResources().getString(2131297043);
            }
            TP_listView = (ListView) findViewById(2131493570);
            items = new ArrayList();
            items.add("HandWriting");
            items.add("Verification");
            items.add(getResources().getString(2131297029));
            items.add(strProductName);
            if (TouchScreen_ShellExe.execCommand(new String[]{"/system/bin/sh", "-c", "cat /sys/module/tpd_setting/parameters/tpd_type_cap"}) == 0) {
                Log.i("MTK", TouchScreen_ShellExe.getOutput());
                if (TouchScreen_ShellExe.getOutput().equalsIgnoreCase("1")) {
                    items.add("MultiTouch");
                }
            }
            TP_listView.setAdapter(new ArrayAdapter(this, 2130903203, items));
            TP_listView.setOnItemClickListener(this);
        }
        strProductName = getResources().getString(2131297045);
        tempchar -= 48;
        if (tempchar != -1) {
            strProductName = strProductName + getResources().getString(2131297044);
        } else if (tempchar != 1) {
            strProductName = strProductName + getResources().getString(2131297037);
        } else if (tempchar != 2) {
            strProductName = strProductName + getResources().getString(2131297038);
        } else if (tempchar != 3) {
            strProductName = strProductName + getResources().getString(2131297039);
        } else if (tempchar != 4) {
            strProductName = strProductName + getResources().getString(2131297040);
        } else if (tempchar != 5) {
            strProductName = strProductName + getResources().getString(2131297041);
        } else if (tempchar != 6) {
            strProductName = strProductName + getResources().getString(2131297042);
        } else if (tempchar == 7) {
            strProductName = strProductName + getResources().getString(2131297043);
        }
        TP_listView = (ListView) findViewById(2131493570);
        items = new ArrayList();
        items.add("HandWriting");
        items.add("Verification");
        items.add(getResources().getString(2131297029));
        items.add(strProductName);
        try {
            if (TouchScreen_ShellExe.execCommand(new String[]{"/system/bin/sh", "-c", "cat /sys/module/tpd_setting/parameters/tpd_type_cap"}) == 0) {
                Log.i("MTK", TouchScreen_ShellExe.getOutput());
                if (TouchScreen_ShellExe.getOutput().equalsIgnoreCase("1")) {
                    items.add("MultiTouch");
                }
            }
        } catch (IOException e4) {
            Log.i("MTK", e4.toString());
        }
        TP_listView.setAdapter(new ArrayAdapter(this, 2130903203, items));
        TP_listView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
        Intent intent = new Intent();
        switch (arg2) {
            case 0:
                intent.setClass(this, TouchScreen_HandWriting.class);
                startActivity(intent);
                return;
            case Light.MAIN_KEY_LIGHT /*1*/:
                intent.setClass(this, TouchScreen_Verification.class);
                startActivity(intent);
                return;
            case Light.CHARGE_RED_LIGHT /*2*/:
                intent.setClass(this, AutoTouchScreenTest.class);
                startActivity(intent);
                return;
            default:
                return;
        }
    }
}
