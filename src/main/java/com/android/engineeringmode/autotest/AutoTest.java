package com.android.engineeringmode.autotest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.engineeringmode.Log;

import java.util.List;

public class AutoTest extends AutoBaseActivity implements OnClickListener {
    private TextView mAutotestShow = null;
    private Button mBtnReport = null;
    private Button mBtnStartTest;
    private final FailRecordManager mFailRecordManager = FailRecordManager.getInstance();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903059);
        setTitle(2131296689);
        this.mBtnStartTest = (Button) findViewById(2131492932);
        this.mBtnStartTest.setOnClickListener(this);
        this.mBtnReport = (Button) findViewById(2131492933);
        this.mBtnReport.setOnClickListener(this);
        this.mAutotestShow = (TextView) findViewById(2131492931);
        setRequestedOrientation(1);
    }

    public void onClick(View v) {
        if (v == this.mBtnStartTest) {
            Log.d("AutoTest", "onClick(), begin test");
            if (this.mFailRecordManager != null) {
                this.mFailRecordManager.getFailList().clear();
            }
            this.mAutotestShow.setText("");
            beginTest();
        } else if (v == this.mBtnReport) {
            startActivity(new Intent(this, AutotestRcdShow.class));
        }
    }

    public void onAllTestEnd() {
        Log.d("AutoTest", "onAllTestEnd(), ================");
        if (this.mFailRecordManager != null) {
            addFailedResultToList(this.mFailRecordManager.getFailList());
        }
    }

    public void onOneTestEnd(Intent data) {
        Log.d("AutoTest", "onOneTestEnd(), +++++++++++++++++");
        startPassUnpassActivity(new Intent("com.android.engineeringmode.autotest.PassUnpassActivity"));
    }

    public AutoTestBaseManager newTestManager() {
        return new AutoTestManager(this);
    }

    public void onTestFailed(String title) {
        String failedRcd = getPrintableFragment(title);
        if (this.mFailRecordManager != null && this.mFailRecordManager.getFailList() != null) {
            this.mFailRecordManager.getFailList().add(failedRcd);
        }
    }

    public String getPrintableFragment(String testItemTitle) {
        return String.format(getString(2131296701), new Object[]{testItemTitle});
    }

    private void addFailedResultToList(List<String> failedInfoList) {
        if (failedInfoList == null || failedInfoList.isEmpty()) {
            FailRecordManager.deleteFile(this);
            this.mAutotestShow.setVisibility(0);
            this.mAutotestShow.setText(2131296270);
            startActivity(new Intent(this, AutotestRcdShow.class));
            return;
        }
        FailRecordManager.savetoFile(this);
        this.mAutotestShow.setVisibility(0);
        this.mAutotestShow.setText(2131296271);
        startActivity(new Intent(this, AutotestRcdShow.class));
    }
}
