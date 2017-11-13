package com.android.engineeringmode.autotest;

import android.content.Intent;

public class BaseItem {
    private long mDuration;
    private boolean mEnabled = true;
    private final Intent mIntent;
    private final String mTitle;
    private String mToggleOffText;
    private String mToggleOnText;
    private boolean mToggled = false;

    public BaseItem(Intent intent, String title) {
        this.mIntent = intent;
        this.mTitle = title;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public boolean isToggleEnabled() {
        return (this.mToggleOffText == null || this.mToggleOnText == null) ? false : true;
    }

    public void setToggleText(String toggleOnText, String toggleOffText) {
        this.mToggleOnText = toggleOnText;
        if (this.mToggleOnText != null && this.mToggleOnText.equals("")) {
            this.mToggleOnText = null;
        }
        this.mToggleOffText = toggleOffText;
        if (this.mToggleOffText != null && this.mToggleOffText.equals("")) {
            this.mToggleOffText = null;
        }
    }

    public String getToggledText(boolean on) {
        return on ? this.mToggleOnText : this.mToggleOffText;
    }

    public void setToggled(boolean toggled) {
        this.mToggled = toggled;
    }

    public boolean isToggled() {
        return this.mToggled;
    }
}
