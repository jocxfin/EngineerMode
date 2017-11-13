package com.android.engineeringmode.wifitest.download;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.WebAddress;
import android.provider.Downloads.Impl;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import com.android.engineeringmode.Log;
import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.wifitest.WifiDownloadPreference;
import com.android.engineeringmode.wifitest.download.DownloadThread.OnDownloadCompeleteListener;
import com.android.engineeringmode.wifitest.download.DownloadThread.OnDownloadingListener;
import com.android.engineeringmode.wifitest.download.DownloadThread.OnErrorWhenDownloadingListener;

public class DownloadManager implements OnDownloadCompeleteListener, OnDownloadingListener, OnErrorWhenDownloadingListener {
    private static DownloadManager sInstance;
    private final Context mContext;
    private DownloadThread mDownloadThread = null;
    private String mFilePath = "download.mp3";
    private String mIpAddress = "192.168.1.2";
    private WifiDownloadPreference mWifiDownloadPreference;
    private boolean mbIsStart = false;

    private DownloadManager(Context context) {
        this.mContext = context;
        this.mIpAddress = getServerIpAdderssFromSharedPreference();
        if (TextUtils.isEmpty(this.mIpAddress)) {
            this.mIpAddress = "192.168.1.2";
        }
        this.mFilePath = getServerFilepathFromSharedPreference();
        if (TextUtils.isEmpty(this.mFilePath)) {
            this.mFilePath = "download.mp3";
        }
    }

    public static void init(Context context) {
        if (sInstance != null) {
            Log.d("MyDownloadManager", "init(), sInstance already initialize!");
        } else {
            sInstance = new DownloadManager(context);
        }
    }

    public static DownloadManager getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        throw new IllegalStateException("Uninitialized.");
    }

    public void startDownloading() {
        if (this.mDownloadThread == null) {
            this.mDownloadThread = new DownloadThread(this, this.mContext);
            this.mDownloadThread.setOnDownloadCompeleteListener(this);
            this.mDownloadThread.setOnDownloadingListener(this);
            this.mDownloadThread.setOnErrorListener(this);
            String url = composeUrl();
            Log.d("MyDownloadManager", "startDownloading(), url = " + url);
            this.mDownloadThread.setUrl(url);
            this.mDownloadThread.start();
            this.mbIsStart = true;
        }
    }

    public boolean isDownloading() {
        return this.mbIsStart;
    }

    public void onDownloadComplete(int totalBytes, long totalTime) {
        Log.d("MyDownloadManager", "onDownloadComplete(), totalBytes = " + totalBytes + ", totalTime = " + totalTime);
        Intent intnt = new Intent("oppo.intent.action.wifitest.downloadcomplete");
        intnt.putExtra("key_download_completed_total_bytes_engineeringmode", totalBytes);
        intnt.putExtra("key_download_completed_total_time_engineeringmode", totalTime);
        this.mContext.sendBroadcast(intnt);
        this.mbIsStart = false;
        this.mDownloadThread = null;
    }

    public void onError(int errorCode) {
        Log.e("MyDownloadManager", "onError(), errorCode = " + errorCode);
        Intent intnt = new Intent("oppo.intent.action.wifitest.downloaderror");
        intnt.putExtra("key_download_error_engineeringmode", errorCode);
        this.mContext.sendBroadcast(intnt);
        if (errorCode <= 0) {
            this.mDownloadThread = null;
            this.mbIsStart = false;
        }
    }

    public String getServerIpAdderssFromSharedPreference() {
        return "192.168.1.2";
    }

    public String getServerFilepathFromSharedPreference() {
        return "download.mp3";
    }

    public void setIpAddress(String ipAddress) {
        Log.d("MyDownloadManager", "setIpAddress(), ipAddress = " + ipAddress);
        this.mIpAddress = ipAddress.trim();
        Editor passwdfile = this.mContext.getSharedPreferences("key_wifi_server_ip_set", 0).edit();
        if (passwdfile != null) {
            passwdfile.putString("key_wifi_server_ip_set", this.mIpAddress);
            passwdfile.commit();
        }
    }

    public void setFilePath(String filePath) {
        Log.d("MyDownloadManager", "setFilePath(), filePath = " + filePath);
        this.mFilePath = filePath.trim();
        Editor passwdfile = this.mContext.getSharedPreferences("key_wifi_server_filepath_set", 0).edit();
        if (passwdfile != null) {
            passwdfile.putString("key_wifi_server_filepath_set", this.mFilePath);
            passwdfile.commit();
        }
    }

    protected String composeUrl() {
        StringBuffer urlStringBuf = new StringBuffer();
        String ipAddress = this.mIpAddress.trim();
        Log.d("MyDownloadManager", "composeUrl(), ipAddress = " + ipAddress);
        if (ipAddress.startsWith("http://")) {
            urlStringBuf.append(ipAddress);
        } else {
            urlStringBuf.append("http://");
            urlStringBuf.append(ipAddress);
        }
        String filePath = this.mFilePath.replace("\\", "/").trim();
        Log.d("MyDownloadManager", "composeUrl(), filePath = " + filePath);
        if (this.mFilePath.startsWith("/")) {
            urlStringBuf.append(filePath);
        } else {
            urlStringBuf.append("/");
            urlStringBuf.append(filePath);
        }
        return urlStringBuf.toString();
    }

    public static boolean isIpAddress(String value) {
        boolean z = false;
        int start = 0;
        int end = value.indexOf(46);
        int numBlocks = 0;
        while (start < value.length()) {
            if (end == -1) {
                end = value.length();
            }
            try {
                int block = Integer.parseInt(value.substring(start, end));
                if (block > Light.MAIN_KEY_MAX || block < 0) {
                    return false;
                }
                numBlocks++;
                start = end + 1;
                end = value.indexOf(46, start);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        if (numBlocks == 4) {
            z = true;
        }
        return z;
    }

    public void downLoadByBrowser() {
        String url = composeUrl();
        String newMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        Log.e("MyDownloadManager", "newMimeType:" + newMimeType);
        try {
            WebAddress webAddress = new WebAddress(url);
            webAddress.setPath(encodePath(webAddress.getPath()));
            String filename = URLUtil.guessFileName(url, null, newMimeType);
            Log.e("MyDownloadManager", "filename:" + filename);
            ContentValues values = new ContentValues();
            values.put("uri", webAddress.toString());
            values.put("notificationpackage", this.mContext.getPackageName());
            values.put("visibility", Integer.valueOf(0));
            values.put("mimetype", newMimeType);
            values.put("hint", filename);
            values.put("description", webAddress.getHost());
            values.put("destination", Integer.valueOf(0));
            Uri contentUri = this.mContext.getContentResolver().insert(Impl.CONTENT_URI, values);
            if (contentUri != null) {
                Log.e("MyDownloadManager", contentUri.toString());
                this.mWifiDownloadPreference.registerChangeObserver(contentUri);
            } else {
                Log.e("MyDownloadManager", "contentUri is null");
            }
        } catch (Exception e) {
            Log.e("MyDownloadManager", "Exception trying to parse url:" + url);
        }
    }

    private static String encodePath(String path) {
        int length;
        char c;
        int i = 0;
        char[] chars = path.toCharArray();
        boolean needed = false;
        for (char c2 : chars) {
            if (c2 == '[' || c2 == ']') {
                needed = true;
                break;
            }
        }
        if (!needed) {
            return path;
        }
        StringBuilder sb = new StringBuilder("");
        length = chars.length;
        while (i < length) {
            c2 = chars[i];
            if (c2 == '[' || c2 == ']') {
                sb.append('%');
                sb.append(Integer.toHexString(c2));
            } else {
                sb.append(c2);
            }
            i++;
        }
        return sb.toString();
    }

    public void setDownLoadPreference(WifiDownloadPreference p) {
        this.mWifiDownloadPreference = p;
    }
}
