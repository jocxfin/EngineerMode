package com.android.engineeringmode.wifitest;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.Preference;
import android.provider.Downloads.Impl;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WifiDownloadPreference extends Preference {
    private ContentObserver mContentObserver;
    private Context mContext;
    private NotificationManager mNotiManager;
    private OnClickListener mOnClickListener = null;
    private long mStartTime;
    private WifiDownloadStatusShow mWifiDownloadStatusShow;
    private Button mbtnStartDownload = null;

    private class ChangeObserver extends ContentObserver {
        private boolean mIsOppoQuickContactBadge;
        private final Uri mTrack;

        public ChangeObserver(WifiDownloadPreference this$0, Uri track) {
            this(track, false);
        }

        public ChangeObserver(Uri track, boolean isOppoQuickContactBadge) {
            super(new Handler());
            this.mTrack = track;
            this.mIsOppoQuickContactBadge = isOppoQuickContactBadge;
        }

        public boolean deliverSelfNotifications() {
            return false;
        }

        public void onChange(boolean selfChange) {
            Cursor cursor = null;
            try {
                cursor = WifiDownloadPreference.this.mContext.getContentResolver().query(this.mTrack, new String[]{"status", "total_bytes", "current_bytes", "_id"}, null, null, null);
                if (cursor.moveToFirst()) {
                    long totalBytes = cursor.getLong(1);
                    if (Impl.isStatusCompleted(cursor.getInt(0))) {
                        if (WifiDownloadPreference.this.mContentObserver != null) {
                            WifiDownloadPreference.this.mContext.getContentResolver().unregisterContentObserver(WifiDownloadPreference.this.mContentObserver);
                            WifiDownloadPreference.this.mContentObserver = null;
                        }
                        long totalTime = SystemClock.elapsedRealtime() - WifiDownloadPreference.this.mStartTime;
                        WifiDownloadPreference.this.setSummary(WifiDownloadPreference.this.mWifiDownloadStatusShow.getPrintableCompleteInfo((int) totalBytes, totalTime));
                        if (totalTime <= 0) {
                            if (cursor != null) {
                                cursor.close();
                            }
                            return;
                        }
                        int second = (int) (totalTime / 1000);
                        int totalKB = ((int) totalBytes) / 1024;
                        if (second <= 0) {
                            second = 1;
                        }
                        Log.i("WifiDownloadPreference", "speed:" + (totalKB / second));
                    } else {
                        long currentBytes = cursor.getLong(2);
                        double speed = (((double) currentBytes) / 1024.0d) / (((double) (SystemClock.elapsedRealtime() - WifiDownloadPreference.this.mStartTime)) / 1000.0d);
                        if (totalBytes > 0) {
                            WifiDownloadPreference.this.setSummary(WifiDownloadPreference.this.mWifiDownloadStatusShow.getPrintableDownloadingInfo((float) speed, (int) ((100 * currentBytes) / totalBytes)));
                        } else {
                            WifiDownloadPreference.this.setSummary(WifiDownloadPreference.this.mWifiDownloadStatusShow.getPrintableSpeed((float) speed));
                        }
                    }
                } else {
                    WifiDownloadPreference.this.setSummary(WifiDownloadPreference.this.mContext.getString(2131296599));
                    if (cursor.getCount() > 3) {
                        WifiDownloadPreference.this.mNotiManager.cancel(cursor.getInt(3));
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (IllegalStateException e) {
                Log.e("WifiTestDownload", "onChange", e);
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    public WifiDownloadPreference(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        this.mContext = context;
        this.mWifiDownloadStatusShow = new WifiDownloadStatusShow(this, context);
        this.mNotiManager = getNotificationManager(context);
        cancelAllDownload();
    }

    public void setButtonDownloadEnable(boolean bEnable) {
        if (this.mbtnStartDownload == null) {
            Log.w("WifiDownloadPreference", "setButtonDownloadEnable(), bEnable = " + bEnable);
        }
    }

    public void setBtnDownloadClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void registerChangeObserver(Uri uri) {
        if (this.mContentObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
            this.mContentObserver = null;
        }
        this.mContentObserver = new ChangeObserver(this, uri);
        this.mContext.getContentResolver().registerContentObserver(uri, false, this.mContentObserver);
        setSummary(2131296441);
        this.mStartTime = SystemClock.elapsedRealtime();
    }

    public void unregisterChangeObserver() {
        if (this.mContentObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
            this.mContentObserver = null;
        }
        cancelAllDownload();
    }

    private NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService("notification");
    }

    private void cancelAllDownload() {
        ContentValues values2 = new ContentValues();
        values2.put("control", Integer.valueOf(1));
        values2.put("visibility", Integer.valueOf(2));
        this.mContext.getContentResolver().update(Impl.CONTENT_URI, values2, null, null);
        this.mContext.getContentResolver().delete(Impl.CONTENT_URI, null, null);
    }
}
