package com.android.engineeringmode.autotest;

import android.content.Context;

import com.android.engineeringmode.Log;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class FailRecordManager {
    private static FailRecordManager sFailRcdMgr = null;
    private final ArrayList<String> mFialedList = new ArrayList();

    public void addFailRecord(String strFailRcd) {
        this.mFialedList.add(strFailRcd);
    }

    public final ArrayList<String> getFailList() {
        Log.i("FailRecordManager", "in getFailList, mFialedList = " + (this.mFialedList == null ? "null" : "not null"));
        return this.mFialedList;
    }

    public final void clearFailList() {
        Log.i("FailRecordManager", "clearFailList");
        if (this.mFialedList != null) {
            this.mFialedList.clear();
        }
    }

    public static void deleteFile(Context context) {
        try {
            context.deleteFile("engineermode_failed.data");
        } catch (Exception e) {
        }
    }

    public static boolean savetoFile(Context context) {
        Log.i("FailRecordManager", "savetoFile");
        try {
            DataOutputStream dos = new DataOutputStream(context.openFileOutput("engineermode_failed.data", 0));
            for (int i = getInstance().getFailList().size() - 1; i >= 0; i--) {
                dos.writeUTF((String) getInstance().getFailList().get(i));
            }
            dos.close();
        } catch (FileNotFoundException e) {
            Log.e("FailRecordManager", "in savetoFile(), an FileNotFoundException happen");
            e.printStackTrace();
        } catch (IOException ioe) {
            Log.e("FailRecordManager", "in savetoFile(), an IOException happen");
            ioe.printStackTrace();
        }
        return true;
    }

    public static FailRecordManager getInstance() {
        if (sFailRcdMgr == null) {
            sFailRcdMgr = new FailRecordManager();
        }
        return sFailRcdMgr;
    }
}
