package com.android.engineeringmode;

import android.app.ListActivity;
import android.os.Bundle;

public class KeepScreenOnListActivity extends ListActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
    }
}
