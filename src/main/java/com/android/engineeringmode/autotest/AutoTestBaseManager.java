package com.android.engineeringmode.autotest;

import android.content.Context;
import android.content.Intent;

import java.util.List;

public abstract class AutoTestBaseManager {
    private BaseParser mAutoTestParser;
    private int mCurrentIndex = 0;

    public abstract void beginTest();

    public abstract void gotoNext();

    public abstract boolean hasNext();

    protected abstract BaseParser obtainParser(Context context);

    public AutoTestBaseManager(Context context) {
        this.mAutoTestParser = obtainParser(context);
    }

    public BaseParser getParser() {
        return this.mAutoTestParser;
    }

    public final int getCurIndex() {
        return this.mCurrentIndex;
    }

    public final Intent getCurIntent() {
        return getIntent(this.mCurrentIndex);
    }

    private Intent getIntent(int index) {
        Intent intent = null;
        List<BaseItem> items = this.mAutoTestParser.getItems();
        if (items == null || index < 0 || index >= items.size()) {
            return null;
        }
        BaseItem item = (BaseItem) items.get(index);
        if (item != null && item.isEnabled()) {
            intent = item.getIntent();
        }
        return intent;
    }

    public final String getCurTitle() {
        return getTitle(this.mCurrentIndex);
    }

    private String getTitle(int index) {
        String str = null;
        List<BaseItem> items = this.mAutoTestParser.getItems();
        if (items == null || index < 0 || index >= items.size()) {
            return null;
        }
        BaseItem item = (BaseItem) items.get(index);
        if (item != null && item.isEnabled()) {
            str = item.getTitle();
        }
        return str;
    }

    public void gotoNext(boolean loop) {
        List<BaseItem> items = this.mAutoTestParser.getItems();
        if (items == null) {
            this.mCurrentIndex = 0;
            return;
        }
        int index = this.mCurrentIndex + 1;
        if (index < 0) {
            index = 0;
        } else if (index > items.size()) {
            index = items.size();
        }
        while (index < items.size()) {
            BaseItem item = (BaseItem) items.get(index);
            if (item != null && item.isEnabled()) {
                break;
            }
            index++;
        }
        if (loop) {
            if (index == items.size()) {
                index = 0;
                while (index <= this.mCurrentIndex && index < items.size()) {
                    item = (BaseItem) items.get(index);
                    if (item != null && item.isEnabled()) {
                        break;
                    }
                    index++;
                }
            }
            this.mCurrentIndex = index;
            return;
        }
        this.mCurrentIndex = index;
    }

    public boolean hasNext(boolean loop) {
        List<BaseItem> items = this.mAutoTestParser.getItems();
        if (items == null) {
            return false;
        }
        int startIndex = loop ? 0 : this.mCurrentIndex + 1;
        if (!loop && startIndex >= items.size()) {
            return false;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int index = startIndex; index < items.size(); index++) {
            BaseItem item = (BaseItem) items.get(index);
            if (item != null && item.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    public void restart() {
        this.mCurrentIndex = -1;
    }
}
