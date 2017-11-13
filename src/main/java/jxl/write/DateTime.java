package jxl.write;

import jxl.DateCell;
import jxl.write.biff.DateRecord;

public class DateTime extends DateRecord implements WritableCell, DateCell {
    public static final GMTDate GMT = new GMTDate();

    public DateTime(DateCell dc) {
        super(dc);
    }
}
