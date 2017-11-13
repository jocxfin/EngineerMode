package jxl.write.biff;

import java.util.Calendar;
import java.util.Date;

import jxl.CellType;
import jxl.DateCell;
import jxl.biff.DoubleHelper;
import jxl.biff.Type;
import jxl.common.Logger;
import jxl.write.DateFormats;
import jxl.write.WritableCellFormat;

public abstract class DateRecord extends CellValue {
    static final WritableCellFormat defaultDateFormat = new WritableCellFormat(DateFormats.DEFAULT);
    private static Logger logger = Logger.getLogger(DateRecord.class);
    private Date date;
    private boolean time;
    private double value;

    protected static final class GMTDate {
    }

    protected DateRecord(DateCell dc) {
        super(Type.NUMBER, dc);
        this.date = dc.getDate();
        this.time = dc.isTime();
        calculateValue(false);
    }

    private void calculateValue(boolean adjust) {
        long zoneOffset = 0;
        long dstOffset = 0;
        if (adjust) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(this.date);
            zoneOffset = (long) cal.get(15);
            dstOffset = (long) cal.get(16);
        }
        this.value = 25569.0d + (((double) ((this.date.getTime() + zoneOffset) + dstOffset)) / 8.64E7d);
        if (!this.time && this.value < 61.0d) {
            this.value -= 1.0d;
        }
        if (this.time) {
            this.value -= (double) ((int) this.value);
        }
    }

    public CellType getType() {
        return CellType.DATE;
    }

    public byte[] getData() {
        byte[] celldata = super.getData();
        byte[] data = new byte[(celldata.length + 8)];
        System.arraycopy(celldata, 0, data, 0, celldata.length);
        DoubleHelper.getIEEEBytes(this.value, data, celldata.length);
        return data;
    }

    public String getContents() {
        return this.date.toString();
    }

    public Date getDate() {
        return this.date;
    }

    public boolean isTime() {
        return this.time;
    }
}
