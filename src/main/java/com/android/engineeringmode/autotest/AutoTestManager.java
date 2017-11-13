package com.android.engineeringmode.autotest;

import android.content.Context;

public class AutoTestManager extends AutoTestBaseManager {
    public AutoTestManager(Context context) {
        super(context);
    }

    public void beginTest() {
        restart();
    }

    public void gotoNext() {
        gotoNext(false);
    }

    public boolean hasNext() {
        return hasNext(false);
    }

    protected BaseParser obtainParser(Context context) {
        return (AutoTestParser) ParserFactory.getInstance().getAutoParser(context);
    }
}
