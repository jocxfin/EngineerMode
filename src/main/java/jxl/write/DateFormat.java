package jxl.write;

import java.text.SimpleDateFormat;

import jxl.biff.DisplayFormat;
import jxl.write.biff.DateFormatRecord;

public class DateFormat extends DateFormatRecord implements DisplayFormat {
    public DateFormat(String format) {
        super(format);
        SimpleDateFormat df = new SimpleDateFormat(format);
    }
}
