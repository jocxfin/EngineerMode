package jxl.write.biff;

import jxl.biff.FormatRecord;

public class DateFormatRecord extends FormatRecord {
    protected DateFormatRecord(String fmt) {
        setFormatString(replace(replace(fmt, "a", "AM/PM"), "S", "0"));
    }
}
