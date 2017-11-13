package com.android.engineeringmode.wifitest.download;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;

import com.android.engineeringmode.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadThread extends Thread {
    private final Context mContext;
    private int mCurrentBytesReceived = 0;
    private OnDownloadCompeleteListener mDownloadCompeleteListener = null;
    private final DownloadManager mDownloadManager;
    private OnDownloadingListener mDownloadingListener = null;
    private OnErrorWhenDownloadingListener mErrorWhenDownloadingListener = null;
    private int mFileLength = 0;
    private String mFileName = "download.mp3";
    private int mRetryCount;
    private int mTotalBytesReceived = 0;
    private String mUrl = null;

    public interface OnDownloadCompeleteListener {
        void onDownloadComplete(int i, long j);
    }

    public interface OnDownloadingListener {
    }

    public interface OnErrorWhenDownloadingListener {
        void onError(int i);
    }

    public DownloadThread(DownloadManager downManager, Context context) {
        this.mContext = context;
        this.mDownloadManager = downManager;
        this.mRetryCount = 3;
    }

    public void run() {
        this.mFileName = getFileNameFormUrl(this.mUrl);
        Log.d("DownloadThread", "run(), mFileName = " + this.mFileName);
        StringBuffer saveFileName = new StringBuffer();
        String EXTERNAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d("DownloadThread", "run(), EXTERNAL_PATH = " + EXTERNAL_PATH);
        saveFileName.append(EXTERNAL_PATH).append("/").append(this.mFileName.trim());
        do {
            int i = this.mRetryCount;
            this.mRetryCount = i - 1;
            if (i > 0) {
                this.mContext.sendBroadcast(new Intent("oppo.intent.action.wifitest.startdownload"));
            } else {
                return;
            }
        } while (!downloadFile(this.mUrl, saveFileName.toString()));
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public void setOnDownloadCompeleteListener(OnDownloadCompeleteListener downloadCompeleteListener) {
        this.mDownloadCompeleteListener = downloadCompeleteListener;
    }

    public void setOnDownloadingListener(OnDownloadingListener onListener) {
        this.mDownloadingListener = onListener;
    }

    public void setOnErrorListener(OnErrorWhenDownloadingListener onListener) {
        this.mErrorWhenDownloadingListener = onListener;
    }

    public boolean downloadFile(String url, String filePath) {
        Exception e;
        Log.d("DownloadThread", "downloadFile(), url = " + url + ", filePath = " + filePath);
        if (TextUtils.isEmpty(getFileNameFormUrl(url))) {
            Log.w("DownloadThread", "downloadFile(), use the default filename");
            String fileName = "test";
        }
        try {
            URL url2 = new URL(url);
            try {
                HttpURLConnection con = (HttpURLConnection) url2.openConnection();
                con.setConnectTimeout(7000);
                InputStream in = con.getInputStream();
                FileOutputStream out = new FileOutputStream(new File(filePath));
                byte[] bytes = new byte[10240];
                this.mTotalBytesReceived = 0;
                this.mCurrentBytesReceived = 0;
                this.mFileLength = con.getContentLength();
                Log.d("DownloadThread", "file length = " + this.mFileLength);
                long beginTime = SystemClock.elapsedRealtime();
                long startTime = beginTime;
                long endTime = beginTime;
                while (!false) {
                    int byteReceived = in.read(bytes);
                    if (byteReceived == -1) {
                        break;
                    }
                    this.mCurrentBytesReceived += byteReceived;
                    this.mTotalBytesReceived += byteReceived;
                    out.write(bytes, 0, byteReceived);
                    endTime = SystemClock.elapsedRealtime();
                    if (endTime - startTime >= 1000) {
                        float speed = (((float) this.mCurrentBytesReceived) * 1.0f) / (((float) (endTime - startTime)) * 1.0f);
                        Intent intnt = new Intent("oppo.intent.action.wifitest.speed");
                        if (this.mFileLength > 0) {
                            intnt.putExtra("key_download_complete_precent_engineeringmode", (this.mTotalBytesReceived * 100) / this.mFileLength);
                        }
                        intnt.putExtra("key_download_speed_engineeringmode", speed);
                        this.mContext.sendBroadcast(intnt);
                        this.mCurrentBytesReceived = 0;
                        startTime = endTime;
                    }
                }
                if (this.mDownloadCompeleteListener != null) {
                    this.mDownloadCompeleteListener.onDownloadComplete(this.mTotalBytesReceived, SystemClock.elapsedRealtime() - beginTime);
                }
                con.disconnect();
                out.flush();
                in.close();
                out.close();
                this.mTotalBytesReceived = 0;
                this.mCurrentBytesReceived = 0;
                Log.e("DownloadThread", "Thread exit()");
                return true;
            } catch (Exception e2) {
                e = e2;
                URL urlServer = url2;
                Log.e("DownloadThread", "Error happen in downloadFile(), url = " + url);
                e.printStackTrace();
                if (this.mErrorWhenDownloadingListener != null) {
                    this.mErrorWhenDownloadingListener.onError(this.mRetryCount);
                }
                SystemClock.sleep(1000);
                return false;
            }
        } catch (Exception e3) {
            e = e3;
            Log.e("DownloadThread", "Error happen in downloadFile(), url = " + url);
            e.printStackTrace();
            if (this.mErrorWhenDownloadingListener != null) {
                this.mErrorWhenDownloadingListener.onError(this.mRetryCount);
            }
            SystemClock.sleep(1000);
            return false;
        }
    }

    public String getFileNameFormUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
