package com.android.engineeringmode.util;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;

import java.util.ArrayList;

public class FillContactsUtil {
    public static int CONTACTS_LENGTH = 2000;
    private int insertNum;
    private boolean isRun;
    private ContentResolver mContentResolver;
    private Context mContext;
    private Thread mThread;
    private volatile boolean start;

    public FillContactsUtil(Context context) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
    }

    public void beginFillContacts(int length) {
        if (length > CONTACTS_LENGTH) {
            length = CONTACTS_LENGTH;
        }
        final int len = length;
        this.insertNum = 0;
        this.isRun = true;
        this.mThread = new Thread(null, new Runnable() {
            public void run() {
                String[] emails = new String[]{"emails@oppo.com"};
                String[] webs = new String[]{"www.oppo.com"};
                for (int i = 0; i < len && FillContactsUtil.this.start; i++) {
                    String[] phones = new String[]{String.valueOf(((long) FillContactsUtil.this.insertNum) + 13800000000L)};
                    String names = String.valueOf(FillContactsUtil.this.insertNum + 10000);
                    FillContactsUtil fillContactsUtil = FillContactsUtil.this;
                    fillContactsUtil.insertNum = fillContactsUtil.insertNum + 1;
                    FillContactsUtil.saveContactsToDB(FillContactsUtil.this.mContext, names, phones, emails, webs);
                }
            }
        });
        this.start = true;
        this.mThread.start();
    }

    public void stopFillContacts() {
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

    public static boolean saveContactsToDB(Context context, String name, String[] phones, String[] emails, String[] webs) {
        try {
            context.getContentResolver().applyBatch("com.android.contacts", buildOperationForRecord(name, phones, emails, webs));
            return true;
        } catch (RemoteException e) {
            return false;
        } catch (OperationApplicationException e2) {
            return false;
        }
    }

    private static ArrayList<ContentProviderOperation> buildOperationForRecord(String name, String[] phones, String[] emails, String[] webs) {
        ArrayList<ContentProviderOperation> operationList = new ArrayList();
        Builder builder = ContentProviderOperation.newInsert(RawContacts.CONTENT_URI);
        builder.withValue("aggregation_needed", Integer.valueOf(0));
        builder.withValue("aggregation_mode", Integer.valueOf(3));
        operationList.add(builder.build());
        ArrayList<ContentProviderOperation> nameOpList = buildNamesInsert(name);
        ArrayList<ContentProviderOperation> phoneOpList = buildPhonesInsert(phones);
        ArrayList<ContentProviderOperation> emailOpList = buildEmailsInsert(emails);
        ArrayList<ContentProviderOperation> webOpList = buildWebsInsert(webs);
        if (nameOpList != null && nameOpList.size() > 0) {
            operationList.addAll(nameOpList);
        }
        if (phoneOpList != null && phoneOpList.size() > 0) {
            operationList.addAll(phoneOpList);
        }
        if (emailOpList != null && emailOpList.size() > 0) {
            operationList.addAll(emailOpList);
        }
        if (webOpList != null && webOpList.size() > 0) {
            operationList.addAll(webOpList);
        }
        return operationList;
    }

    private static ArrayList<ContentProviderOperation> buildNamesInsert(String name) {
        ArrayList<ContentProviderOperation> operationList = new ArrayList();
        Builder builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
        builder.withValueBackReference("raw_contact_id", 0);
        builder.withValue("mimetype", "vnd.android.cursor.item/name");
        builder.withValue("data1", name);
        operationList.add(builder.build());
        return operationList;
    }

    private static ArrayList<ContentProviderOperation> buildPhonesInsert(String[] phones) {
        if (phones == null) {
            return null;
        }
        boolean hasSetPrimary = false;
        ArrayList<ContentProviderOperation> operationList = new ArrayList();
        for (int i = 0; i != phones.length; i++) {
            Builder builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
            builder.withValueBackReference("raw_contact_id", 0);
            builder.withValue("mimetype", "vnd.android.cursor.item/phone_v2");
            builder.withValue("data2", Integer.valueOf(2));
            builder.withValue("data1", phones[i]);
            if (!hasSetPrimary) {
                builder.withValue("is_primary", Integer.valueOf(1));
                hasSetPrimary = true;
            }
            operationList.add(builder.build());
        }
        return operationList;
    }

    private static ArrayList<ContentProviderOperation> buildWebsInsert(String[] webs) {
        if (webs == null) {
            return null;
        }
        boolean hasSetPrimary = false;
        ArrayList<ContentProviderOperation> operationList = new ArrayList();
        for (int i = 0; i != webs.length; i++) {
            Builder builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
            builder.withValueBackReference("raw_contact_id", 0);
            builder.withValue("mimetype", "vnd.android.cursor.item/website");
            builder.withValue("data2", Integer.valueOf(4));
            builder.withValue("data1", webs[i]);
            operationList.add(builder.build());
            if (!hasSetPrimary) {
                builder.withValue("is_primary", Integer.valueOf(1));
                hasSetPrimary = true;
            }
            operationList.add(builder.build());
        }
        return operationList;
    }

    private static ArrayList<ContentProviderOperation> buildEmailsInsert(String[] emails) {
        if (emails == null) {
            return null;
        }
        boolean hasSetPrimary = false;
        ArrayList<ContentProviderOperation> operationList = new ArrayList();
        for (int i = 0; i != emails.length; i++) {
            Builder builder = ContentProviderOperation.newInsert(Data.CONTENT_URI);
            builder.withValueBackReference("raw_contact_id", 0);
            builder.withValue("mimetype", "vnd.android.cursor.item/email_v2");
            builder.withValue("data2", Integer.valueOf(1));
            builder.withValue("data4", emails[i]);
            operationList.add(builder.build());
            if (!hasSetPrimary) {
                builder.withValue("is_primary", Integer.valueOf(1));
                hasSetPrimary = true;
            }
            operationList.add(builder.build());
        }
        return operationList;
    }
}
