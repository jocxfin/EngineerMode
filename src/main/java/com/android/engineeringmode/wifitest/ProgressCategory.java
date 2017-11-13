package com.android.engineeringmode.wifitest;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;

public class ProgressCategory extends PreferenceCategory {
    private boolean mProgress = false;

    public ProgressCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(2130903185);
    }

    public void onBindView(View view) {
        super.onBindView(view);
        View textView = view.findViewById(2131493491);
        View progressBar = view.findViewById(2131493490);
        int visibility = this.mProgress ? 0 : 4;
        textView.setVisibility(visibility);
        progressBar.setVisibility(visibility);
    }

    public void setProgress(boolean progressOn) {
        this.mProgress = progressOn;
        notifyChanged();
    }
}
