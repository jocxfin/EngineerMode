package com.android.engineeringmode.qualcomm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.engineeringmode.functions.Light;
import com.oem.util.Feature;

import java.util.Calendar;

public class DiagEnabled extends Activity implements OnClickListener, OnCheckedChangeListener {
    private static String ALLDIAG_USB_CONFIG = "diag,serial_smd,serial_tty,rmnet_bam,mass_storage,adb";
    private static String USB_CONFIG_PROPERTY;
    private int mAdbEnable = 0;
    private ToggleButton mAllDiag;
    private Boolean mAllOpened = Boolean.valueOf(false);
    private ToggleButton mDiag;
    private Boolean mDiagOpened = Boolean.valueOf(false);
    private EditText mKeyField;
    private Button mPrivilege;
    private ToggleButton mRndisAndDiag;
    private Boolean mRndisAndDiagOpened = Boolean.valueOf(false);
    private ToggleButton mSerial;
    private Boolean mSerialOpened = Boolean.valueOf(false);
    private UsbManager mUsbManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diag_enabled);
        this.mSerial = (ToggleButton) findViewById(2131493042);
        this.mSerial.setOnCheckedChangeListener(this);
        this.mDiag = (ToggleButton) findViewById(2131493041);
        this.mDiag.setOnCheckedChangeListener(this);
        this.mAllDiag = (ToggleButton) findViewById(2131493043);
        this.mAllDiag.setOnCheckedChangeListener(this);
        this.mRndisAndDiag = (ToggleButton) findViewById(2131493044);
        this.mRndisAndDiag.setOnCheckedChangeListener(this);
        this.mPrivilege = (Button) findViewById(2131493046);
        this.mPrivilege.setOnClickListener(this);
        this.mPrivilege.setVisibility(4);
        ((TextView) findViewById(2131493045)).setVisibility(4);
        this.mUsbManager = (UsbManager) getSystemService("usb");
        if (getIntent() != null) {
            escalatedUp(true, getIntent().getStringExtra("code"));
        }
        if (Feature.isSerialCdevSupported(this)) {
            ALLDIAG_USB_CONFIG = "diag,serial_cdev,serial_tty,rmnet_ipa,mass_storage,adb";
        }
    }

    protected void onResume() {
        super.onResume();
        this.mDiag.setChecked(SystemProperties.getBoolean("persist.sys.dial.enable", false));
        USB_CONFIG_PROPERTY = SystemProperties.get("sys.usb.config");
        boolean isSelected = false;
        if (USB_CONFIG_PROPERTY.equals(ALLDIAG_USB_CONFIG)) {
            isSelected = true;
            SystemProperties.set("persist.sys.diag.enable", "true");
        }
        Log.d("DiagEnabled", "USB_CONFIG_PROPERTY: " + USB_CONFIG_PROPERTY + "  sys.usb.config  isSeleceted: " + isSelected);
        this.mAllDiag.setChecked(isSelected);
        updatePrivilegeButton();
        this.mAdbEnable = Global.getInt(getContentResolver(), "adb_enabled", 0);
    }

    private boolean checkPrivilege() {
        return !getSharedPreferences("privilege", 0).getBoolean("escalated", false) ? Privilege.isEscalated() : true;
    }

    private void updatePrivilegeButton() {
        int i;
        boolean privilege = checkPrivilege();
        Button button = this.mPrivilege;
        if (privilege) {
            i = 2131297173;
        } else {
            i = 2131297172;
        }
        button.setText(i);
        this.mPrivilege.setTag(Boolean.valueOf(privilege));
    }

    private boolean escalatedUp(boolean enable, String password) {
        boolean ret = true;
        if (enable) {
            if (password != null) {
                enable = Privilege.escalate(password);
                if (enable) {
                    SystemProperties.set("persist.sys.adbroot", "1");
                    SystemProperties.set("oem.selinux.reload_policy", "1");
                }
                Log.d("DiagEnabled", "privilege escalate " + (enable ? "success" : "failed"));
            } else {
                enable = false;
            }
            ret = enable;
        } else {
            SystemProperties.set("persist.sys.adbroot", "0");
            Privilege.recover();
        }
        Editor e = getSharedPreferences("privilege", 0).edit();
        e.putBoolean("escalated", enable);
        e.commit();
        updatePrivilegeButton();
        if (ret) {
            if ("0".equals(SystemProperties.get("persist.sys.adbroot", "1"))) {
                new Thread(new Runnable() {
                    public void run() {
                        Log.i("DiagEnabled", "reboot device...");
                        ((PowerManager) DiagEnabled.this.getSystemService("power")).reboot(null);
                    }
                }).start();
            } else {
                SystemProperties.set("ctl.restart", "adbd");
            }
        }
        return ret;
    }

    public Dialog onCreateDialog(int id) {
        Dialog dialog = new Dialog(this);
        if (id == 2435) {
            dialog.setContentView(R.layout.password_picker);
            dialog.setTitle(2131297174);
            dialog.findViewById(2131493481).setOnClickListener(this);
            dialog.findViewById(2131493482).setOnClickListener(this);
        }
        return dialog;
    }

    public void onPrepareDialog(int id, Dialog dialog) {
        if (id == 2435) {
            EditText et = (EditText) dialog.findViewById(R.id.password);
            this.mKeyField = et;
            et.setText(null);
            et.setOnEditorActionListener(new OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == 6) {
                        String key = DiagEnabled.this.mKeyField.getText().toString();
                        DiagEnabled.this.dismissDialog(2435);
                        if (DiagEnabled.this.escalatedUp(true, key)) {
                            Toast.makeText(DiagEnabled.this, 2131297175, 0).show();
                        }
                    }
                    return true;
                }
            });
        }
        super.onPrepareDialog(id, dialog);
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case 2131493041:
                SystemProperties.set("persist.sys.allcommode", Boolean.toString(isChecked));
                if (isChecked) {
                    this.mAdbEnable = Global.getInt(getContentResolver(), "adb_enabled", 0);
                    if (this.mAdbEnable != 1) {
                        Global.putInt(getContentResolver(), "adb_enabled", 1);
                    }
                    SystemProperties.set("persist.sys.adb.engineermode", "0");
                    SystemClock.sleep(200);
                    SystemProperties.set("persist.sys.usb.config", "diag,adb");
                    SystemProperties.set("sys.usb.config", "diag,adb");
                    sendBroadcast(new Intent("android.intent.action.LOCKSREEN_ENGINEERINGMODE"));
                    SystemProperties.set("persist.service.adb.enable", "0");
                    SystemClock.sleep(100);
                    SystemProperties.set("persist.service.adb.enable", "1");
                    this.mDiagOpened = Boolean.valueOf(true);
                    this.mAllDiag.setEnabled(false);
                    this.mRndisAndDiag.setEnabled(false);
                    this.mUsbManager.setUsbDataUnlocked(false);
                } else {
                    if (this.mAdbEnable != 1) {
                        Global.putInt(getContentResolver(), "adb_enabled", 0);
                    }
                    SystemProperties.set("persist.sys.adb.engineermode", "1");
                    this.mUsbManager.setCurrentFunction("mtp");
                    this.mUsbManager.setUsbDataUnlocked(true);
                    this.mAllDiag.setEnabled(true);
                    this.mRndisAndDiag.setEnabled(true);
                }
                SystemProperties.set("persist.sys.dial.enable", Boolean.toString(isChecked));
                break;
            case 2131493042:
                if (!isChecked) {
                    Light.closeSerialPort();
                    SystemProperties.set("ctl.stop", "console");
                    this.mSerialOpened = Boolean.valueOf(false);
                    break;
                }
                Light.openSerialPort();
                SystemProperties.set("ctl.start", "console");
                this.mSerialOpened = Boolean.valueOf(true);
                break;
            case 2131493043:
                SystemProperties.set("persist.sys.allcommode", Boolean.toString(isChecked));
                if (!isChecked) {
                    this.mUsbManager.setCurrentFunction("mtp");
                    this.mUsbManager.setUsbDataUnlocked(true);
                    this.mAllOpened = Boolean.valueOf(false);
                    SystemProperties.set("persist.sys.diag.enable", "false");
                    this.mDiag.setEnabled(true);
                    this.mRndisAndDiag.setEnabled(true);
                    break;
                }
                SystemProperties.set("persist.sys.usb.config", ALLDIAG_USB_CONFIG);
                SystemProperties.set("sys.usb.config", ALLDIAG_USB_CONFIG);
                sendBroadcast(new Intent("android.intent.action.LOCKSREEN_ENGINEERINGMODE"));
                SystemProperties.set("persist.service.adb.enable", "0");
                SystemClock.sleep(100);
                SystemProperties.set("persist.service.adb.enable", "1");
                Log.d("DiagEnabled", SystemProperties.get("sys.usb.config"));
                this.mAllOpened = Boolean.valueOf(true);
                SystemProperties.set("persist.sys.diag.enable", "true");
                this.mDiag.setEnabled(false);
                this.mRndisAndDiag.setEnabled(false);
                break;
            case 2131493044:
                SystemProperties.set("sys.usb.engspecialconfig", Boolean.toString(isChecked));
                if (!isChecked) {
                    if (this.mRndisAndDiagOpened.booleanValue()) {
                        setUsbTethering(false);
                    }
                    this.mRndisAndDiagOpened = Boolean.valueOf(false);
                    this.mAllDiag.setEnabled(true);
                    this.mDiag.setEnabled(true);
                    break;
                }
                this.mDiag.setEnabled(false);
                this.mAllDiag.setEnabled(false);
                for (int i = 0; i < 20; i++) {
                    if (SystemProperties.getBoolean("sys.usb.engspecialconfig", false)) {
                        setUsbTethering(true);
                        this.mRndisAndDiagOpened = Boolean.valueOf(true);
                        return;
                    }
                    SystemClock.sleep(50);
                }
                Toast.makeText(this, "set usb config rndis,diag failed", 1).show();
                break;
        }
    }

    private void setUsbTethering(boolean enabled) {
        if (((ConnectivityManager) getSystemService("connectivity")).setUsbTethering(enabled) != 0) {
            Toast.makeText(this, "set usb config rndis,diag failed", 1).show();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131493046:
                if (checkPrivilege()) {
                    escalatedUp(false, null);
                    Toast.makeText(this, 2131297176, 0).show();
                    return;
                }
                showDialog(2435);
                return;
            case 2131493481:
                String key = this.mKeyField.getText().toString();
                dismissDialog(2435);
                if (escalatedUp(true, key)) {
                    Toast.makeText(this, 2131297175, 0).show();
                    return;
                }
                return;
            case 2131493482:
                dismissDialog(2435);
                return;
            default:
                return;
        }
    }

    protected void onStop() {
        Calendar now = Calendar.getInstance();
        String time = now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13);
        if (this.mDiagOpened.booleanValue()) {
            String str = time + "--DiagEnabled--" + "open diag_mdm,adb";
        }
        if (this.mSerialOpened.booleanValue()) {
            str = time + "--DiagEnabled--" + "open SerialPort";
        }
        if (this.mAllOpened.booleanValue()) {
            str = time + "--DiagEnabled--" + " open " + ALLDIAG_USB_CONFIG;
        }
        super.onStop();
    }
}
