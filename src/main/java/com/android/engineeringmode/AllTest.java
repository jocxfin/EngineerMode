package com.android.engineeringmode;

import android.content.Context;
import android.widget.Toast;

public class AllTest {
    public static void showShortMessage(Context context, int nResId) {
        Toast.makeText(context, context.getResources().getString(nResId), 0).show();
    }
}
