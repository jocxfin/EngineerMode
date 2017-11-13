package com.android.engineeringmode.util;

import android.content.Context;
import android.os.Handler;
import android.os.SystemProperties;
import android.text.TextUtils;

import com.android.engineeringmode.Log;
import com.qualcomm.qcnvitems.QcNvItems;

import java.io.IOException;

public class ExternFunction {
    private QcNvItems mQcNvItems = null;

    public ExternFunction(Context context) {
        this.mQcNvItems = new QcNvItems(context);
    }

    public byte[] getGpsData() {
        try {
            return this.mQcNvItems.getGpsSnr();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getAdjustStatus() {
        try {
            byte[] res = this.mQcNvItems.getCalibrateInformation();
            Log.d("ExternFunction", "engineeringmode--ExternFunction-------res" + res);
            return res;
        } catch (IOException e) {
            Log.d("ExternFunction", "engineeringmode--ExternFunction-------res IOException" + e);
            e.printStackTrace();
            return null;
        }
    }

    public String getEncryptIMei(int subId) {
        try {
            Log.d("ExternFunction", "engineeringmode--ExternFunction----get EncryptIMei");
            if (!SystemProperties.get("telephony.lteOnCdmaDevice", "").equals("1,1")) {
                return (String) this.mQcNvItems.getClass().getDeclaredMethod("getEncryptImei", new Class[0]).invoke(this.mQcNvItems, new Object[0]);
            }
            return (String) this.mQcNvItems.getClass().getDeclaredMethod("getEncryptImei", new Class[]{Byte.TYPE}).invoke(this.mQcNvItems, new Object[]{Byte.valueOf((byte) subId)});
        } catch (Exception e) {
            Log.d("ExternFunction", "engineeringmode--ExternFunction----not get EncryptIMei");
            return "";
        }
    }

    public String getPCBNumber() {
        String pcb;
        try {
            Log.d("ExternFunction", "engineeringmode--ExternFunction------- get PCBNumber");
            pcb = this.mQcNvItems.getPcbNumber();
            Log.d("ExternFunction", "engineeringmode--ExternFunction------- get PCBNumber2");
        } catch (IOException e) {
            Log.d("ExternFunction", "engineeringmode--ExternFunction----not get PCBNumber");
            pcb = "";
        }
        if (TextUtils.isEmpty(pcb) || -1 == pcb.indexOf(0)) {
            return pcb;
        }
        return pcb.substring(0, pcb.indexOf(0));
    }

    public String getMeidNumber() {
        try {
            Log.d("ExternFunction", "engineeringmode--ExternFunction------- get getMeidNumber");
            String meid = this.mQcNvItems.getMEID();
            Log.d("ExternFunction", "engineeringmode--ExternFunction------- get getMeidNumber");
            return meid;
        } catch (IOException e) {
            Log.d("ExternFunction", "engineeringmode--ExternFunction----not get getMeidNumber");
            return "";
        }
    }

    public byte[] getProductLineTestFlag() {
        try {
            Log.d("ExternFunction", "engineeringmode--ExternFunction----getProductLineTestFlag");
            return this.mQcNvItems.getProductLineTestFlag();
        } catch (IOException e) {
            Log.d("ExternFunction", "ExternFunction getProductLineTestFlag IOException" + e.getMessage());
            return null;
        }
    }

    public void setProductLineTestFlagExtraByte(int index, byte value) {
        byte[] flag = getProductLineTestFlag();
        if (flag == null || flag.length < index) {
            Log.d("ExternFunction", "index out of bound");
            return;
        }
        flag[index] = value;
        setProductLineTestFlag(flag);
    }

    public void setProductLineTestFlag(byte[] value) {
        try {
            Log.d("ExternFunction", "engineeringmode--ExternFunction----setProductLineTestFlag");
            this.mQcNvItems.setProductLineTestFlag(value);
        } catch (IOException e) {
            Log.d("ExternFunction", "ExternFunction setProductLineTestFlag IOException" + e.getMessage());
        }
    }

    public void registerOnServiceConnected(Handler h, int what, Object obj) {
        this.mQcNvItems.registerOnServiceConnected(h, what, obj);
    }

    public void unregisterOnServiceConnected(Handler h) {
        this.mQcNvItems.unregisterOnServiceConnected(h);
    }

    public void dispose() {
        Log.d("ExternFunction", "engineeringmode--ExternFunction-------dispose");
        this.mQcNvItems.dispose();
    }
}
