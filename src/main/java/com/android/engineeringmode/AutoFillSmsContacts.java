package com.android.engineeringmode;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.engineeringmode.util.FillContactsUtil;
import com.android.engineeringmode.util.FillSmsUtil;

public class AutoFillSmsContacts extends KeepScreenOnActivity implements OnClickListener {
    private boolean exit = true;
    private Button mContactBtn;
    private FillContactsUtil mFillContacts;
    private FillSmsUtil mFillSms;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int num;
            if (msg.what == 1) {
                num = AutoFillSmsContacts.this.mFillSms.getInsertNum();
                AutoFillSmsContacts.this.mPDialog.setProgress(num);
                if (num >= FillSmsUtil.SMS_LENGTH || AutoFillSmsContacts.this.exit) {
                    AutoFillSmsContacts.this.mPDialog.dismiss();
                } else {
                    sendEmptyMessageDelayed(1, 1000);
                }
            } else if (msg.what == 2) {
                num = AutoFillSmsContacts.this.mFillContacts.getInsertNum();
                AutoFillSmsContacts.this.mPDialog.setProgress(num);
                if (num >= FillContactsUtil.CONTACTS_LENGTH || AutoFillSmsContacts.this.exit) {
                    AutoFillSmsContacts.this.mPDialog.dismiss();
                } else {
                    sendEmptyMessageDelayed(2, 1000);
                }
            }
        }
    };
    private ProgressDialog mPDialog;
    private Button mSmsBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903058);
        this.mSmsBtn = (Button) findViewById(2131492929);
        this.mContactBtn = (Button) findViewById(2131492930);
        this.mSmsBtn.setOnClickListener(this);
        this.mContactBtn.setOnClickListener(this);
        init();
    }

    private void init() {
        this.mFillSms = new FillSmsUtil(this);
        this.mFillContacts = new FillContactsUtil(this);
        this.mPDialog = new ProgressDialog(this);
        this.mPDialog.setTitle("Inserting");
        this.mPDialog.setProgressStyle(1);
        this.mPDialog.setButton(getString(17039360), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                AutoFillSmsContacts.this.stop();
            }
        });
        this.mPDialog.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                AutoFillSmsContacts.this.stop();
            }
        });
    }

    protected void onPause() {
        super.onPause();
        stop();
    }

    public void onClick(View v) {
        this.exit = false;
        v.setEnabled(false);
        if (v.getId() == 2131492929) {
            this.mPDialog.setMax(FillSmsUtil.SMS_LENGTH);
            this.mPDialog.show();
            this.mFillSms.beginFillSms(FillSmsUtil.SMS_LENGTH);
            this.mHandler.sendEmptyMessage(1);
        } else if (v.getId() == 2131492930) {
            this.mPDialog.setMax(FillContactsUtil.CONTACTS_LENGTH);
            this.mPDialog.show();
            this.mFillContacts.beginFillContacts(FillContactsUtil.CONTACTS_LENGTH);
            this.mHandler.sendEmptyMessage(2);
        }
    }

    private void stop() {
        if (!this.exit) {
            this.exit = true;
            this.mFillSms.stopFillSms();
            this.mFillContacts.stopFillContacts();
            Toast.makeText(this, "Stop inserting", 1).show();
        }
    }
}
