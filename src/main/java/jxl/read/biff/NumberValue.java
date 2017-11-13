package jxl.read.biff;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import jxl.CellFeatures;
import jxl.CellType;
import jxl.NumberCell;
import jxl.biff.FormattingRecords;
import jxl.format.CellFormat;

class NumberValue implements NumberCell, CellFeaturesAccessor {
    private static DecimalFormat defaultFormat = new DecimalFormat("#.###");
    private CellFormat cellFormat;
    private int column;
    private CellFeatures features;
    private NumberFormat format = defaultFormat;
    private FormattingRecords formattingRecords;
    private boolean initialized;
    private int row;
    private SheetImpl sheet;
    private double value;
    private int xfIndex;

    public NumberValue(int r, int c, double val, int xfi, FormattingRecords fr, SheetImpl si) {
        this.row = r;
        this.column = c;
        this.value = val;
        this.xfIndex = xfi;
        this.formattingRecords = fr;
        this.sheet = si;
        this.initialized = false;
    }

    final void setNumberFormat(NumberFormat f) {
        if (f != null) {
            this.format = f;
        }
    }

    public final int getRow() {
        return this.row;
    }

    public final int getColumn() {
        return this.column;
    }

    public double getValue() {
        return this.value;
    }

    public String getContents() {
        return this.format.format(this.value);
    }

    public CellType getType() {
        return CellType.NUMBER;
    }

    public CellFormat getCellFormat() {
        if (!this.initialized) {
            this.cellFormat = this.formattingRecords.getXFRecord(this.xfIndex);
            this.initialized = true;
        }
        return this.cellFormat;
    }

    public CellFeatures getCellFeatures() {
        return this.features;
    }

    public void setCellFeatures(CellFeatures cf) {
        this.features = cf;
    }
}
