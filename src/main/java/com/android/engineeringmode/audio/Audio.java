package com.android.engineeringmode.audio;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;

import java.util.ArrayList;
import java.util.List;

public class Audio extends Activity implements OnItemClickListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903042);
        ListView Audio_listView = (ListView) findViewById(2131492878);
        List<String> items = new ArrayList();
        items.add("Set Mode");
        items.add("Normal Mode");
        items.add("Headset Mode");
        items.add("Handfree Mode");
        items.add("Dual Microphone Setting");
        items.add("Choose Ring");
        Audio_listView.setAdapter(new ArrayAdapter(this, 2130903203, items));
        Audio_listView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
        Intent intent = new Intent();
        switch (arg2) {
            case 0:
                showDialog(1);
                return;
            case Light.MAIN_KEY_LIGHT /*1*/:
                intent.setClass(this, Audio_ModeSetting.class);
                intent.putExtra("current_mode", 1);
                startActivity(intent);
                return;
            case Light.CHARGE_RED_LIGHT /*2*/:
                intent.setClass(this, Audio_ModeSetting.class);
                intent.putExtra("current_mode", 2);
                startActivity(intent);
                return;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                intent.setClass(this, Audio_ModeSetting.class);
                intent.putExtra("current_mode", 3);
                startActivity(intent);
                return;
            case 4:
                intent.setClass(this, Audio_DualMic.class);
                startActivity(intent);
                return;
            case 5:
                try {
                    startActivity(new Intent("oppo.intent.action.ACTION_MUSIC_SELECT"));
                    return;
                } catch (ActivityNotFoundException e) {
                    Log.e("Audio", "ActivityNotFoundException:" + e.getMessage());
                    Toast.makeText(this, "Can not deal action:oppo.intent.action.ACTION_MUSIC_SELECT, ActivityNotFoundException", 1).show();
                    return;
                }
            default:
                return;
        }
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                String[] setRouteItems = new String[]{"Normal mode", "Headset mode", "Handfree mode"};
                Builder builder = new Builder(this);
                builder.setTitle("Set Mode");
                builder.setSingleChoiceItems(setRouteItems, 0, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                builder.setPositiveButton("OK", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }
}
