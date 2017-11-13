package com.android.engineeringmode.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.Telephony.Threads;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FillSmsUtil {
    public static int SMS_LENGTH = 1000;
    private String[] dests;
    private int insertNum;
    private boolean isRun;
    private ContentResolver mContentResolver = this.mContext.getContentResolver();
    private Context mContext;
    private Thread mThread;
    private HashSet<String> recipients;
    private volatile boolean start;
    private ContentValues values;

    public FillSmsUtil(Context context) {
        this.mContext = context;
    }

    public void beginFillSms(int length) {
        if (length > 1000) {
            length = 1000;
        }
        final int len = length;
        this.insertNum = 0;
        this.isRun = true;
        this.mThread = new Thread(null, new Runnable() {
            public void run() {
                long addressLong = 13800138000L;
                for (int i = 0; i < len && FillSmsUtil.this.start; i++) {
                    FillSmsUtil.this.prepareSmsData(String.valueOf(addressLong));
                    addressLong++;
                    FillSmsUtil fillSmsUtil = FillSmsUtil.this;
                    fillSmsUtil.insertNum = fillSmsUtil.insertNum + 1;
                }
            }
        });
        this.start = true;
        this.mThread.start();
    }

    public void stopFillSms() {
        if (this.mThread != null) {
            try {
                this.mThread.interrupt();
                this.start = false;
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    public int getInsertNum() {
        return this.insertNum;
    }

    private void prepareSmsData(String address) {
        this.dests = formatePhoneName(address);
        this.recipients = new HashSet();
        for (Object add : this.dests) {
            this.recipients.add(add);
        }
        writeSmsDataBase(this.mContext, Threads.getOrCreateThreadId(this.mContext, this.recipients), System.currentTimeMillis(), address);
    }

    private void writeSmsDataBase(Context context, long threadId, long date, String address) {
        this.values = new ContentValues(7);
        this.values.put("address", address);
        this.values.put("date", Long.valueOf(1000 + date));
        this.values.put("read", Integer.valueOf(1));
        this.values.put("body", "oppo_sms_test");
        this.values.put("thread_id", Long.valueOf(threadId));
        this.values.put("status", Integer.valueOf(-1));
        this.values.put("type", Integer.valueOf(2));
        this.values.put("seen", Integer.valueOf(1));
        this.mContentResolver.insert(Uri.parse("content://sms"), this.values);
    }

    public static String[] formatePhoneName(String address) {
        List<String> numbers = new ArrayList();
        String semiSepNumbers = address;
        for (String number : address.split(";")) {
            if (!TextUtils.isEmpty(number)) {
                numbers.add(number);
            }
        }
        return (String[]) numbers.toArray(new String[numbers.size()]);
    }
}
