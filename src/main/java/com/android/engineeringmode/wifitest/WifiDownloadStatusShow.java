package com.android.engineeringmode.wifitest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.Preference;
import android.text.format.Formatter;

import com.android.engineeringmode.Log;

public class WifiDownloadStatusShow {
    private final Context mContext;
    private BroadcastReceiver mDownloadReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("WifiDownloadShow", "onReceive(), action = " + action);
            if ("oppo.intent.action.wifitest.downloadcomplete".equals(action)) {
                int totalBytes = intent.getIntExtra("key_download_completed_total_bytes_engineeringmode", 0);
                long totalTime = intent.getLongExtra("key_download_completed_total_time_engineeringmode", 1);
                WifiDownloadStatusShow.this.mPrefDownloadStatus.setSummary(WifiDownloadStatusShow.this.getPrintableCompleteInfo(totalBytes, totalTime));
                if (WifiDownloadStatusShow.this.mPrefDownloadStatus instanceof WifiDownloadPreference) {
                    ((WifiDownloadPreference) WifiDownloadStatusShow.this.mPrefDownloadStatus).setButtonDownloadEnable(true);
                }
                if (totalTime > 0) {
                    int speed;
                    try {
                        speed = (totalBytes / 1024) / ((int) (totalTime / 1000));
                    } catch (ArithmeticException e) {
                        Log.e("WifiDownloadShow", "divide by zero");
                        speed = 0;
                    }
                    Log.i("WifiDownloadShow", "speed:" + speed);
                }
            } else if ("oppo.intent.action.wifitest.downloaderror".equals(action)) {
                WifiDownloadStatusShow.this.mPrefDownloadStatus.setSummary(WifiDownloadStatusShow.this.mContext.getString(2131296599));
                if (WifiDownloadStatusShow.this.mPrefDownloadStatus instanceof WifiDownloadPreference) {
                    ((WifiDownloadPreference) WifiDownloadStatusShow.this.mPrefDownloadStatus).setButtonDownloadEnable(true);
                }
            } else if ("oppo.intent.action.wifitest.speed".equals(action)) {
                float speed2 = intent.getFloatExtra("key_download_speed_engineeringmode", 0.0f);
                int precent = intent.getIntExtra("key_download_complete_precent_engineeringmode", 0);
                Log.d("WifiDownloadShow", "onReceive(), speed = " + speed2);
                WifiDownloadStatusShow.this.mPrefDownloadStatus.setSummary(WifiDownloadStatusShow.this.getPrintableDownloadingInfo(speed2, precent));
                if (WifiDownloadStatusShow.this.mPrefDownloadStatus instanceof WifiDownloadPreference) {
                    ((WifiDownloadPreference) WifiDownloadStatusShow.this.mPrefDownloadStatus).setButtonDownloadEnable(false);
                }
            } else if ("oppo.intent.action.wifitest.startdownload".equals(action)) {
                WifiDownloadStatusShow.this.mPrefDownloadStatus.setSummary(2131296441);
            }
        }
    };
    private final Preference mPrefDownloadStatus;
    private StringBuffer mTempStringBuffer;

    public WifiDownloadStatusShow(Preference preference, Context context) {
        this.mPrefDownloadStatus = preference;
        this.mContext = context;
        this.mTempStringBuffer = new StringBuffer();
    }

    public void onResume() {
        IntentFilter intntFilter = new IntentFilter();
        intntFilter.addAction("oppo.intent.action.wifitest.downloadcomplete");
        intntFilter.addAction("oppo.intent.action.wifitest.downloaderror");
        intntFilter.addAction("oppo.intent.action.wifitest.speed");
        intntFilter.addAction("oppo.intent.action.wifitest.startdownload");
        this.mContext.registerReceiver(this.mDownloadReceiver, intntFilter);
        Log.d("WifiDownloadShow", "onResume()");
    }

    public void onPause() {
        this.mContext.unregisterReceiver(this.mDownloadReceiver);
        Log.d("WifiDownloadShow", "onPause()");
    }

    public String getPrintableSpeed(float speed) {
        String fragment = this.mContext.getString(2131296598);
        this.mTempStringBuffer.setLength(0);
        return String.format(fragment, new Object[]{Integer.valueOf((int) speed)});
    }

    public String getFormatPrecent(int precent) {
        this.mTempStringBuffer.setLength(0);
        this.mTempStringBuffer.append(this.mContext.getString(2131296604)).append(": ").append(precent).append("%");
        return this.mTempStringBuffer.toString();
    }

    public String getPrintableCompleteInfo(int totalBytes, long totalTime) {
        StringBuffer tempSrting = new StringBuffer();
        tempSrting.append(this.mContext.getString(2131296600)).append(" ").append(this.mContext.getString(2131296601)).append(Formatter.formatFileSize(this.mContext, (long) totalBytes)).append(" ").append(this.mContext.getString(2131296602)).append(getFormatTime(totalTime)).append(" ").append(this.mContext.getString(2131296603)).append(" ").append(getFormatAverageSpeed(totalBytes, totalTime));
        return tempSrting.toString();
    }

    public String getPrintableDownloadingInfo(float speed, int precent) {
        StringBuffer tempSrting = new StringBuffer();
        tempSrting.append(getPrintableSpeed(speed)).append(" ").append(getFormatPrecent(precent));
        return tempSrting.toString();
    }

    public String getFormatAverageSpeed(int totalBytes, long timeUsed) {
        if (timeUsed <= 0) {
            return "";
        }
        this.mTempStringBuffer.setLength(0);
        float averageSpeed = ((float) totalBytes) / (((float) timeUsed) / 1000.0f);
        Log.d("WifiDownloadShow", "getFormatAverageSpeed(), 1.averageSpeed = " + averageSpeed);
        int suffix = 2131297244;
        if (averageSpeed > 1024.0f) {
            suffix = 2131297245;
            averageSpeed /= 1024.0f;
            Log.d("WifiDownloadShow", "getFormatAverageSpeed(), 2.averageSpeed = " + averageSpeed);
        }
        if (averageSpeed > 1024.0f) {
            suffix = 2131297246;
            averageSpeed /= 1024.0f;
            Log.d("WifiDownloadShow", "getFormatAverageSpeed(), 3.averageSpeed = " + averageSpeed);
        }
        Log.d("WifiDownloadShow", "getFormatAverageSpeed(), 3.iAverageSpeed = " + ((int) averageSpeed));
        this.mTempStringBuffer.append(averageSpeed).append(" ").append(this.mContext.getString(suffix)).append("/s");
        return this.mTempStringBuffer.toString();
    }

    public String getFormatTime(long timeUsed) {
        this.mTempStringBuffer.setLength(0);
        this.mTempStringBuffer.append(timeUsed / 1000);
        this.mTempStringBuffer.append("s");
        return this.mTempStringBuffer.toString();
    }
}
