package com.android.engineeringmode.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.widget.Toast;

public class MessageCenter {
    private MessageCenter() {
    }

    public static void showShortMessage(Context context, int resId) {
        showShortMessage(context, context.getResources().getString(resId));
    }

    public static void showShortMessage(Context context, String msg) {
        Toast.makeText(context, msg, 0).show();
    }

    public static void showLongMessage(Context context, int resId) {
        showLongMessage(context, context.getResources().getString(resId));
    }

    public static void showLongMessage(Context context, String msg) {
        Toast.makeText(context, msg, 1).show();
    }

    public static ProgressDialog showProgress(Context context, OnCancelListener cancelListener, int title, int msg) {
        return showProgress(context, cancelListener, context.getResources().getString(title), context.getResources().getString(msg));
    }

    public static ProgressDialog showProgress(Context context, OnCancelListener cancelListener, String title, String msg) {
        return ProgressDialog.show(context, title, msg, false, true, cancelListener);
    }
}
