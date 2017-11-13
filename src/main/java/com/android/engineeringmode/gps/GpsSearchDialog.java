package com.android.engineeringmode.gps;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.TextView;

import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController.AlertParams;

public class GpsSearchDialog extends AlertActivity implements OnClickListener {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertParams params = this.mAlertParams;
        params.mTitle = getString(2131297270);
        params.mPositiveButtonText = getString(17039370);
        params.mPositiveButtonListener = this;
        String str = getString(2131297271);
        TextView view = new TextView(this);
        view.setGravity(17);
        view.setTextSize(40.0f);
        view.setText(str);
        view.setTextColor(-65536);
        params.mView = view;
        setupAlert();
    }

    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case -1:
                finish();
                return;
            default:
                return;
        }
    }
}
