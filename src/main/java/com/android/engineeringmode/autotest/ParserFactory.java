package com.android.engineeringmode.autotest;

import android.content.Context;

import com.android.engineeringmode.Log;
import com.android.engineeringmode.autoaging.AutoAgingParser;

public class ParserFactory {
    private static ParserFactory sPraseFactory = null;
    private AutoTestParser mAutoTestPraser = null;
    private AutoAgingParser mAutoagingParser = null;

    ParserFactory() {
    }

    public BaseParser getAutoParser(Context context) {
        if (this.mAutoTestPraser == null) {
            this.mAutoTestPraser = new AutoTestParser(context);
        }
        return this.mAutoTestPraser;
    }

    public BaseParser getAutoagingParser(Context context) {
        if (this.mAutoagingParser == null) {
            this.mAutoagingParser = new AutoAgingParser(context);
        }
        return this.mAutoagingParser;
    }

    public static ParserFactory getInstance() {
        Log.i("PraserFactory", "getAutoFillPraser");
        if (sPraseFactory == null) {
            sPraseFactory = new ParserFactory();
        }
        return sPraseFactory;
    }
}
