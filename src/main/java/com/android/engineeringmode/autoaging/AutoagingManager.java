package com.android.engineeringmode.autoaging;

import android.content.Context;

import com.android.engineeringmode.autotest.AutoTestBaseManager;
import com.android.engineeringmode.autotest.BaseParser;
import com.android.engineeringmode.autotest.ParserFactory;

public class AutoagingManager extends AutoTestBaseManager {
    public AutoagingManager(Context context) {
        super(context);
    }

    public void beginTest() {
        restart();
    }

    public void gotoNext() {
        gotoNext(true);
    }

    public boolean hasNext() {
        return hasNext(true);
    }

    protected BaseParser obtainParser(Context context) {
        return ParserFactory.getInstance().getAutoagingParser(context);
    }
}
