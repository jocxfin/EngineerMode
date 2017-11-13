package com.android.engineeringmode.wifimtk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WiFi_MCR extends Activity implements OnClickListener {
    private final String TAG = "EM_WiFi_MCR";
    private EditText mAddrEdit;
    private Button mReadBtn;
    private EditText mValueEdit;
    private Button mWriteBtn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903230);
        this.mAddrEdit = (EditText) findViewById(2131493635);
        this.mValueEdit = (EditText) findViewById(2131493637);
        this.mReadBtn = (Button) findViewById(2131493638);
        this.mWriteBtn = (Button) findViewById(2131493639);
        this.mReadBtn.setOnClickListener(this);
        this.mWriteBtn.setOnClickListener(this);
    }

    public void onClick(View arg0) {
        if (arg0.getId() == this.mReadBtn.getId()) {
            try {
                EMWifi.readMCR32(Long.parseLong(this.mAddrEdit.getText().toString(), 16), new long[1]);
                this.mValueEdit.setText(String.format("%1$08x", new Object[]{Long.valueOf(u4Value[0])}));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "invalid input value", 0).show();
                return;
            }
        }
        if (arg0.getId() == this.mWriteBtn.getId()) {
            try {
                EMWifi.writeMCR32(Long.parseLong(this.mAddrEdit.getText().toString(), 16), Long.parseLong(this.mValueEdit.getText().toString(), 16));
            } catch (NumberFormatException e2) {
                Toast.makeText(this, "invalid input value", 0).show();
            }
        }
    }
}
