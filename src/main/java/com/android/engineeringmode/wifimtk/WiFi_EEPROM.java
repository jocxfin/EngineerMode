package com.android.engineeringmode.wifimtk;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class WiFi_EEPROM extends Activity implements OnClickListener {
    private final String FILE_PATH = "/data/data/com.mediatek.engineermode/myData";
    private final String TAG = "EM_WiFi_EEPROM";
    private Button mBurnBtn;
    private Button mClearBtn;
    private Button mReadAllBtn;
    private Button mSaveBtn;
    private String mSaveFileName;
    private TextView mShowWindowText;
    private EditText mStringAddrEdit;
    private EditText mStringLengthEdit;
    private Button mStringReadBtn;
    private EditText mStringValueEdit;
    private Button mStringWriteBtn;
    private EditText mWordAddrEdit;
    private Button mWordReadBtn;
    private EditText mWordValueEdit;
    private Button mWordWriteBtn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903229);
        this.mWordAddrEdit = (EditText) findViewById(2131493614);
        this.mWordValueEdit = (EditText) findViewById(2131493616);
        this.mWordReadBtn = (Button) findViewById(2131493617);
        this.mWordWriteBtn = (Button) findViewById(2131493618);
        this.mStringAddrEdit = (EditText) findViewById(2131493621);
        this.mStringLengthEdit = (EditText) findViewById(2131493623);
        this.mStringValueEdit = (EditText) findViewById(2131493625);
        this.mStringReadBtn = (Button) findViewById(2131493626);
        this.mStringWriteBtn = (Button) findViewById(2131493627);
        this.mSaveBtn = (Button) findViewById(2131493628);
        this.mBurnBtn = (Button) findViewById(2131493629);
        this.mReadAllBtn = (Button) findViewById(2131493630);
        this.mClearBtn = (Button) findViewById(2131493631);
        this.mShowWindowText = (TextView) findViewById(2131493632);
        this.mWordReadBtn.setOnClickListener(this);
        this.mWordWriteBtn.setOnClickListener(this);
        this.mStringReadBtn.setOnClickListener(this);
        this.mStringWriteBtn.setOnClickListener(this);
        this.mSaveBtn.setOnClickListener(this);
        this.mBurnBtn.setOnClickListener(this);
        this.mReadAllBtn.setOnClickListener(this);
        this.mClearBtn.setOnClickListener(this);
        if (EMWifi.setEEPRomSize(512) != 0) {
            Toast.makeText(this, "initial setEEPRomSize to 512 failed", 1).show();
        }
        File myFile = new File("/data/data/com.mediatek.engineermode/myData");
        if (!myFile.exists()) {
            if (myFile.mkdirs()) {
                Log.d("EM_WiFi_EEPROM", "create dir succeed" + myFile.toString());
            } else {
                Log.d("EM_WiFi_EEPROM", "create dir failed" + myFile.toString());
            }
        }
    }

    protected void onResume() {
        super.onResume();
    }

    public void onClick(View arg0) {
        long u4Addr;
        long u4Length;
        switch (arg0.getId()) {
            case 2131493617:
                long[] u4Val = new long[1];
                try {
                    EMWifi.readEEPRom16(Long.parseLong(this.mWordAddrEdit.getText().toString(), 16), u4Val);
                    this.mWordValueEdit.setText(Long.toHexString(u4Val[0]));
                    break;
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "invalid input value", 0).show();
                    return;
                }
            case 2131493618:
                try {
                    EMWifi.writeEEPRom16(Long.parseLong(this.mWordAddrEdit.getText().toString(), 16), Long.parseLong(this.mWordValueEdit.getText().toString(), 16));
                    EMWifi.setEEPromCKSUpdated();
                    break;
                } catch (NumberFormatException e2) {
                    Toast.makeText(this, "invalid input value", 0).show();
                    return;
                }
            case 2131493626:
                byte[] acSzTmp = new byte[512];
                try {
                    u4Addr = Long.parseLong(this.mStringAddrEdit.getText().toString(), 16);
                    u4Length = Long.parseLong(this.mStringLengthEdit.getText().toString());
                    if (u4Length != 0) {
                        EMWifi.eepromReadByteStr(u4Addr, u4Length, acSzTmp);
                        this.mStringValueEdit.setText(new String(acSzTmp, 0, ((int) u4Length) * 2));
                        break;
                    }
                    return;
                } catch (NumberFormatException e3) {
                    Toast.makeText(this, "invalid input value", 0).show();
                    return;
                }
            case 2131493627:
                CharSequence inputVal = this.mStringAddrEdit.getText();
                if (TextUtils.isEmpty(inputVal)) {
                    Toast.makeText(this, "invalid input value", 0).show();
                    break;
                }
                try {
                    u4Addr = Long.parseLong(inputVal.toString(), 16);
                    u4Length = Long.parseLong(this.mStringLengthEdit.getText().toString());
                    String szTmp = this.mStringValueEdit.getText().toString();
                    int len = szTmp.length();
                    if (len != 0 && len % 2 != 1) {
                        EMWifi.eepromWriteByteStr(u4Addr, (long) (len / 2), szTmp);
                        EMWifi.setEEPromCKSUpdated();
                        break;
                    }
                    PrintText("Byte string length error:" + len + "bytes\n");
                    return;
                } catch (NumberFormatException e4) {
                    Toast.makeText(this, "invalid input value", 0).show();
                    return;
                }
                break;
            case 2131493628:
                onClickSaveBtnEepEe2File();
                break;
            case 2131493629:
                onClickBurnBtnEepFile2ee();
                break;
            case 2131493630:
                onClickReadAllBtn();
                break;
            case 2131493631:
                this.mShowWindowText.setText("");
                break;
        }
    }

    private boolean createFile(String filename) {
        try {
            new File("/data/data/com.mediatek.engineermode/myData/" + filename).createNewFile();
            Log.d("EM_WiFi_EEPROM", "create file succeed");
            return true;
        } catch (IOException e) {
            PrintText("Create file failed\n");
            e.printStackTrace();
            return false;
        }
    }

    private boolean SaveAsFile(String filename, String str) {
        try {
            FileOutputStream out = new FileOutputStream(new File("/data/data/com.mediatek.engineermode/myData", filename), true);
            try {
                out.write(str.getBytes());
                out.close();
                return true;
            } catch (IOException e) {
                PrintText("No file is specified");
                e.printStackTrace();
                return false;
            }
        } catch (FileNotFoundException E) {
            PrintText("Write fail with string" + str + "\n");
            E.printStackTrace();
            return false;
        }
    }

    private void onClickSaveBtnEepEe2File() {
        View layout = ((LayoutInflater) getSystemService("layout_inflater")).inflate(2130903165, null);
        final EditText text = (EditText) layout.findViewById(2131493445);
        Builder builder = new Builder(this);
        builder.setTitle("Please input a file name");
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                CharSequence name = text.getText();
                if (!TextUtils.isEmpty(name)) {
                    WiFi_EEPROM.this.mSaveFileName = name.toString();
                    WiFi_EEPROM.this.PrintText("SaveFileName =" + WiFi_EEPROM.this.mSaveFileName + "\n");
                    if (WiFi_EEPROM.this.createFile(WiFi_EEPROM.this.mSaveFileName)) {
                        long[] i4Tmp = new long[1];
                        long[] u4Tmp = new long[1];
                        if (EMWifi.getEEPRomSize(i4Tmp) != 0) {
                            i4Tmp[0] = 512;
                        }
                        Log.d("EM_WiFi_EEPROM", "i4Tmp = " + i4Tmp[0]);
                        int u2EepromSz = (int) i4Tmp[0];
                        Log.d("EM_WiFi_EEPROM", "u2EepromSz = " + u2EepromSz);
                        long u2Tmp = 0;
                        while (u2Tmp < ((long) (u2EepromSz / 2)) && EMWifi.readEEPRom16(u2Tmp, u4Tmp) == 0) {
                            int u2EepromValue = (int) u4Tmp[0];
                            Log.d("EM_WiFi_EEPROM", "i4Tmp = " + u4Tmp[0]);
                            if (WiFi_EEPROM.this.SaveAsFile(WiFi_EEPROM.this.mSaveFileName, String.format("%1$04x", new Object[]{Integer.valueOf(u2EepromValue)}) + "\r\n")) {
                                if (u2Tmp % 16 == 15) {
                                    if (!WiFi_EEPROM.this.SaveAsFile(WiFi_EEPROM.this.mSaveFileName, "\r\n")) {
                                        return;
                                    }
                                }
                                u2Tmp++;
                            } else {
                                return;
                            }
                        }
                    }
                }
            }
        });
        builder.create().show();
    }

    private void onClickBurnBtnEepFile2ee() {
        startActivityForResult(new Intent(this, FileList.class), 1);
    }

    private void onClickReadAllBtn() {
        long[] i4Tmp = new long[1];
        if (EMWifi.getEEPRomSize(i4Tmp) < 0) {
            PrintText("Get EEPROM size failed\n");
            return;
        }
        int u2EepromSzByte = (int) i4Tmp[0];
        if (u2EepromSzByte > 512) {
            PrintText("Get EEPROM size " + u2EepromSzByte + " too big\n");
            return;
        }
        int[] pau2EepromValue = new int[(u2EepromSzByte / 2)];
        if (pau2EepromValue == null) {
            PrintText("Memory allocate fail, size " + u2EepromSzByte + "\n");
        } else if (eepReadWordBySize(pau2EepromValue, u2EepromSzByte / 2)) {
            for (int u2Tmp = 0; u2Tmp < u2EepromSzByte / 2; u2Tmp += 4) {
                int u2Tmp2;
                if (u2Tmp + 4 < u2EepromSzByte / 2) {
                    u2Tmp2 = 4;
                } else {
                    u2Tmp2 = (u2EepromSzByte / 2) - u2Tmp;
                }
                String key;
                String value0;
                String value1;
                switch (u2Tmp2) {
                    case Light.MAIN_KEY_LIGHT /*1*/:
                        key = String.format("%1$04x", new Object[]{Integer.valueOf(u2Tmp)});
                        PrintText("\n" + key + ": " + String.format("%1$04x", new Object[]{Integer.valueOf(pau2EepromValue[u2Tmp])}) + "\t");
                        break;
                    case Light.CHARGE_RED_LIGHT /*2*/:
                        key = String.format("%1$04x", new Object[]{Integer.valueOf(u2Tmp)});
                        value0 = String.format("%1$04x", new Object[]{Integer.valueOf(pau2EepromValue[u2Tmp])});
                        PrintText("\n" + key + ": " + value0 + "\t" + String.format("%1$04x", new Object[]{Integer.valueOf(pau2EepromValue[u2Tmp + 1])}) + "\t");
                        break;
                    case Light.CHARGE_GREEN_LIGHT /*3*/:
                        key = String.format("%1$04x", new Object[]{Integer.valueOf(u2Tmp)});
                        value0 = String.format("%1$04x", new Object[]{Integer.valueOf(pau2EepromValue[u2Tmp])});
                        value1 = String.format("%1$04x", new Object[]{Integer.valueOf(pau2EepromValue[u2Tmp + 1])});
                        PrintText("\n" + key + ": " + value0 + "\t" + value1 + "\t" + String.format("%1$04x", new Object[]{Integer.valueOf(pau2EepromValue[u2Tmp + 2])}) + "\t");
                        break;
                    case 4:
                        key = String.format("%1$04x", new Object[]{Integer.valueOf(u2Tmp)});
                        value0 = String.format("%1$04x", new Object[]{Integer.valueOf(pau2EepromValue[u2Tmp])});
                        value1 = String.format("%1$04x", new Object[]{Integer.valueOf(pau2EepromValue[u2Tmp + 1])});
                        String value2 = String.format("%1$04x", new Object[]{Integer.valueOf(pau2EepromValue[u2Tmp + 2])});
                        PrintText("\n" + key + ": " + value0 + "\t" + value1 + "\t" + value2 + "\t" + String.format("%1$04x", new Object[]{Integer.valueOf(pau2EepromValue[u2Tmp + 3])}) + "\t");
                        break;
                    default:
                        break;
                }
            }
        } else {
            PrintText("Get EEPROM content failed\n");
        }
    }

    private boolean eepReadWordBySize(int[] pau2EepValue, int u2EepSizeW) {
        long[] u4Tmp = new long[1];
        for (int u2Tmp = 0; u2Tmp < u2EepSizeW; u2Tmp++) {
            if (EMWifi.readEEPRom16((long) u2Tmp, u4Tmp) != 0) {
                PrintText("EEPROM read fial at word offset " + String.format("%1$04x", new Object[]{Integer.valueOf(u2Tmp)}) + "\n");
                return false;
            }
            pau2EepValue[u2Tmp] = (int) u4Tmp[0];
        }
        return true;
    }

    private void PrintText(String text) {
        this.mShowWindowText.append(text);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case -1:
                String filename = data.getExtras().getString("FILENAME");
                PrintText("GetOpenFileNameEx = " + filename + "\n");
                if (EMWifi.setEEPRomSize(512) == 0) {
                    int i4RetVal = EMWifi.setEEPRomFromFile("/data/data/com.mediatek.engineermode/myData/" + filename);
                    if (i4RetVal == 0) {
                        Log.d("EM_WiFi_EEPROM", "write from file succeed");
                        break;
                    }
                    PrintText("setEEPRomFromFile failed, error = " + i4RetVal);
                    Log.d("EM_WiFi_EEPROM", "write from file failed");
                    break;
                }
                return;
            default:
                PrintText("GetOpenFileNameEx return null");
                break;
        }
    }
}
