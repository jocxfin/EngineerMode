package com.android.engineeringmode.qualcomm;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class QualCommNvShow extends ListActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getListView().setAdapter(new ArrayAdapter(this, 2130903203, getIntent().getStringArrayListExtra("NV_BACKUP")));
    }
}
