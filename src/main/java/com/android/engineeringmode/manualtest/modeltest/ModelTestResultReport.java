package com.android.engineeringmode.manualtest.modeltest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ModelTestResultReport extends Activity {
    private final String TAG = "ModelTestResultReport";
    private ArrayAdapter<String> adapter;
    private List<String> failedRecords;
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903157);
        this.failedRecords = getIntent().getStringArrayListExtra("FailedList");
        initResources();
    }

    private void initResources() {
        this.listView = (ListView) findViewById(2131493380);
        this.adapter = new ArrayAdapter(this, 17367043, this.failedRecords);
        this.listView.setAdapter(this.adapter);
        this.listView.setEmptyView((TextView) findViewById(2131493381));
        if (this.failedRecords.size() > 0) {
            setTitle(2131296688);
        } else {
            setTitle(2131296270);
        }
        ((Button) findViewById(2131493382)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d("ModelTestResultReport", "quit button clicked");
                ModelTestResultReport.this.startActivity(new Intent("com.android.engineeringmode.manualtest.ManualTest"));
                ModelTestResultReport.this.finish();
            }
        });
    }
}
