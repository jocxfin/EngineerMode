package com.android.engineeringmode.touchscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.engineeringmode.functions.Light;

import java.util.ArrayList;
import java.util.List;

public class TouchScreen_Verification extends Activity implements OnItemClickListener {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903215);
        ListView TP_Verification_listView = (ListView) findViewById(2131493571);
        List<String> items = new ArrayList();
        items.add("PointVerification");
        items.add("LineVerification");
        items.add("ShakingVerification");
        TP_Verification_listView.setAdapter(new ArrayAdapter(this, 2130903203, items));
        TP_Verification_listView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
        Intent intent = new Intent();
        switch (arg2) {
            case 0:
                intent.setClass(this, TouchScreen_VerificationPoint.class);
                break;
            case Light.MAIN_KEY_LIGHT /*1*/:
                intent.setClass(this, TouchScreen_VerificationLine.class);
                break;
            case Light.CHARGE_RED_LIGHT /*2*/:
                intent.setClass(this, TouchScreen_VerificationShaking.class);
                break;
        }
        startActivity(intent);
    }
}
