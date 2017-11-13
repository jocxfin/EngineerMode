package jxl.write.biff;

import jxl.CellType;
import jxl.LabelCell;
import jxl.biff.FormattingRecords;
import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.common.Logger;

public abstract class LabelRecord extends CellValue {
    private static Logger logger = Logger.getLogger(LabelRecord.class);
    private String contents;
    private int index;
    private SharedStrings sharedStrings;

    protected LabelRecord(int c, int r, String cont) {
        super(Type.LABELSST, c, r);
        this.contents = cont;
        if (this.contents == null) {
            this.contents = "";
        }
    }

    protected LabelRecord(LabelCell lc) {
        super(Type.LABELSST, lc);
        this.contents = lc.getString();
        if (this.contents == null) {
            this.contents = "";
        }
    }

    public CellType getType() {
        return CellType.LABEL;
    }

    public byte[] getData() {
        byte[] celldata = super.getData();
        byte[] data = new byte[(celldata.length + 4)];
        System.arraycopy(celldata, 0, data, 0, celldata.length);
        IntegerHelper.getFourBytes(this.index, data, celldata.length);
        return data;
    }

    public String getContents() {
        return this.contents;
    }

    public String getString() {
        return this.contents;
    }

    void setCellDetails(FormattingRecords fr, SharedStrings ss, WritableSheetImpl s) {
        super.setCellDetails(fr, ss, s);
        this.sharedStrings = ss;
        this.index = this.sharedStrings.getIndex(this.contents);
        this.contents = this.sharedStrings.get(this.index);
    }
}
