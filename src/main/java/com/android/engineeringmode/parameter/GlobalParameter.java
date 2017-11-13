package com.android.engineeringmode.parameter;

import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.DateFormat;

import java.io.File;

public class GlobalParameter {
    private static final String RECORD_STORAGE_PATH;

    public static class FileDescriptor {
        private final String mType;
        private final Uri mUri;

        public FileDescriptor(Uri uri, String mimeType) {
            this.mUri = uri;
            this.mType = mimeType;
        }

        public Uri getUri() {
            return this.mUri;
        }

        public String getMimeType() {
            return this.mType;
        }
    }

    private GlobalParameter() {
    }

    public static String getVideoPath() {
        return "file:///sdcard/test.mp4";
    }

    public static FileDescriptor getVideoDescriptor() {
        return new FileDescriptor(Uri.parse("file:///sdcard/test.mp4"), "video/mp4");
    }

    public static int getExternalRequestCode() {
        return 34952;
    }

    static {
        Object stringBuilder = new StringBuilder();
        File dataDirectory = Environment.getDataDirectory();
        String str = File.separator;
        str = "backup/";
        RECORD_STORAGE_PATH = stringBuilder;
    }

    public static String getRecordStorage() {
        return RECORD_STORAGE_PATH;
    }

    public static long getRecordMinSize() {
        return 2097152;
    }

    public static boolean hasRecordStorage() {
        StatFs stat = new StatFs(Environment.getDataDirectory().toString());
        return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize()) >= getRecordMinSize();
    }

    public static String getDateTimeFormat() {
        return "yyyy-MM-dd_kk.mm.ss";
    }

    public static String getCurrentDateTime() {
        return DateFormat.format(getDateTimeFormat(), System.currentTimeMillis()).toString();
    }
}
