package com.android.engineeringmode.audio;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Audio_ModeSetting extends ListActivity {
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
    private int mMode;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mListView = getListView();
        this.mAdapter = new ArrayAdapter(this, 2130903203, 2131493145, new String[]{"Key Tone", "Speech", "Melody", "Microphone"});
        this.mListView.setAdapter(this.mAdapter);
        this.mMode = getIntent().getIntExtra("current_mode", -1);
    }

    protected void onListItemClick(ListView l, View v, int pos, long id) {
        Intent intent = new Intent(this, Audio_ParamSetting.class);
        intent.putExtra("current_mode", this.mMode);
        intent.putExtra("current_type", pos + 1);
        startActivity(intent);
    }
}
