package jxl.read.biff;

import com.android.engineeringmode.functions.Light;

import java.util.ArrayList;

import jxl.WorkbookSettings;
import jxl.biff.BuiltInName;
import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.biff.StringHelper;
import jxl.common.Assert;
import jxl.common.Logger;

public class NameRecord extends RecordData {
    public static Biff7 biff7 = new Biff7();
    private static Logger logger = Logger.getLogger(NameRecord.class);
    private BuiltInName builtInName;
    private int index;
    private boolean isbiff8;
    private String name;
    private ArrayList ranges;
    private int sheetRef = 0;

    private static class Biff7 {
        private Biff7() {
        }
    }

    public class NameRange {
        private int columnFirst;
        private int columnLast;
        private int externalSheet;
        private int rowFirst;
        private int rowLast;

        NameRange(int s1, int c1, int r1, int c2, int r2) {
            this.columnFirst = c1;
            this.rowFirst = r1;
            this.columnLast = c2;
            this.rowLast = r2;
            this.externalSheet = s1;
        }

        public int getFirstColumn() {
            return this.columnFirst;
        }

        public int getFirstRow() {
            return this.rowFirst;
        }

        public int getLastColumn() {
            return this.columnLast;
        }

        public int getLastRow() {
            return this.rowLast;
        }

        public int getExternalSheet() {
            return this.externalSheet;
        }
    }

    NameRecord(Record t, WorkbookSettings ws, int ind) {
        super(t);
        this.index = ind;
        this.isbiff8 = true;
        try {
            this.ranges = new ArrayList();
            byte[] data = getRecord().getData();
            int option = IntegerHelper.getInt(data[0], data[1]);
            int length = data[3];
            this.sheetRef = IntegerHelper.getInt(data[8], data[9]);
            if ((option & 32) == 0) {
                this.name = StringHelper.getString(data, length, 15, ws);
            } else {
                this.builtInName = BuiltInName.getBuiltInName(data[15]);
            }
            if ((option & 12) == 0) {
                int pos = length + 15;
                int columnMask;
                boolean z;
                if (data[pos] == (byte) 58) {
                    int sheet = IntegerHelper.getInt(data[pos + 1], data[pos + 2]);
                    int row = IntegerHelper.getInt(data[pos + 3], data[pos + 4]);
                    columnMask = IntegerHelper.getInt(data[pos + 5], data[pos + 6]);
                    int column = columnMask & Light.MAIN_KEY_MAX;
                    if ((786432 & columnMask) != 0) {
                        z = false;
                    } else {
                        z = true;
                    }
                    Assert.verify(z);
                    this.ranges.add(new NameRange(sheet, column, row, column, row));
                } else if (data[pos] == (byte) 59) {
                    while (pos < data.length) {
                        sheet1 = IntegerHelper.getInt(data[pos + 1], data[pos + 2]);
                        r1 = IntegerHelper.getInt(data[pos + 3], data[pos + 4]);
                        r2 = IntegerHelper.getInt(data[pos + 5], data[pos + 6]);
                        columnMask = IntegerHelper.getInt(data[pos + 7], data[pos + 8]);
                        c1 = columnMask & Light.MAIN_KEY_MAX;
                        if ((786432 & columnMask) != 0) {
                            z = false;
                        } else {
                            z = true;
                        }
                        Assert.verify(z);
                        columnMask = IntegerHelper.getInt(data[pos + 9], data[pos + 10]);
                        c2 = columnMask & Light.MAIN_KEY_MAX;
                        if ((786432 & columnMask) != 0) {
                            z = false;
                        } else {
                            z = true;
                        }
                        Assert.verify(z);
                        this.ranges.add(new NameRange(sheet1, c1, r1, c2, r2));
                        pos += 11;
                    }
                } else if (data[pos] != (byte) 41) {
                    logger.warn("Cannot read name ranges for " + (this.name == null ? this.builtInName.getName() : this.name) + " - setting to empty");
                    this.ranges.add(new NameRange(0, 0, 0, 0, 0));
                } else {
                    if (!(pos >= data.length || data[pos] == (byte) 58 || data[pos] == (byte) 59)) {
                        if (data[pos] == (byte) 41) {
                            pos += 3;
                        } else if (data[pos] == (byte) 16) {
                            pos++;
                        }
                    }
                    while (pos < data.length) {
                        sheet1 = IntegerHelper.getInt(data[pos + 1], data[pos + 2]);
                        r1 = IntegerHelper.getInt(data[pos + 3], data[pos + 4]);
                        r2 = IntegerHelper.getInt(data[pos + 5], data[pos + 6]);
                        columnMask = IntegerHelper.getInt(data[pos + 7], data[pos + 8]);
                        c1 = columnMask & Light.MAIN_KEY_MAX;
                        if ((786432 & columnMask) != 0) {
                            z = false;
                        } else {
                            z = true;
                        }
                        Assert.verify(z);
                        columnMask = IntegerHelper.getInt(data[pos + 9], data[pos + 10]);
                        c2 = columnMask & Light.MAIN_KEY_MAX;
                        if ((786432 & columnMask) != 0) {
                            z = false;
                        } else {
                            z = true;
                        }
                        Assert.verify(z);
                        this.ranges.add(new NameRange(sheet1, c1, r1, c2, r2));
                        pos += 11;
                        if (!(pos >= data.length || data[pos] == (byte) 58 || data[pos] == (byte) 59)) {
                            if (data[pos] == (byte) 41) {
                                pos += 3;
                            } else if (data[pos] == (byte) 16) {
                                pos++;
                            }
                        }
                    }
                }
            }
        } catch (Throwable th) {
            logger.warn("Cannot read name");
            this.name = "ERROR";
        }
    }

    NameRecord(Record t, WorkbookSettings ws, int ind, Biff7 dummy) {
        super(t);
        this.index = ind;
        this.isbiff8 = false;
        try {
            this.ranges = new ArrayList();
            byte[] data = getRecord().getData();
            int length = data[3];
            this.sheetRef = IntegerHelper.getInt(data[8], data[9]);
            this.name = StringHelper.getString(data, length, 14, ws);
            int pos = length + 14;
            if (pos < data.length) {
                if (data[pos] == (byte) 58) {
                    int sheet = IntegerHelper.getInt(data[pos + 11], data[pos + 12]);
                    int row = IntegerHelper.getInt(data[pos + 15], data[pos + 16]);
                    int column = data[pos + 17];
                    this.ranges.add(new NameRange(sheet, column, row, column, row));
                } else if (data[pos] == (byte) 59) {
                    while (pos < data.length) {
                        this.ranges.add(new NameRange(IntegerHelper.getInt(data[pos + 11], data[pos + 12]), data[pos + 19], IntegerHelper.getInt(data[pos + 15], data[pos + 16]), data[pos + 20], IntegerHelper.getInt(data[pos + 17], data[pos + 18])));
                        pos += 21;
                    }
                } else if (data[pos] == (byte) 41) {
                    if (!(pos >= data.length || data[pos] == (byte) 58 || data[pos] == (byte) 59)) {
                        if (data[pos] == (byte) 41) {
                            pos += 3;
                        } else if (data[pos] == (byte) 16) {
                            pos++;
                        }
                    }
                    while (pos < data.length) {
                        this.ranges.add(new NameRange(IntegerHelper.getInt(data[pos + 11], data[pos + 12]), data[pos + 19], IntegerHelper.getInt(data[pos + 15], data[pos + 16]), data[pos + 20], IntegerHelper.getInt(data[pos + 17], data[pos + 18])));
                        pos += 21;
                        if (!(pos >= data.length || data[pos] == (byte) 58 || data[pos] == (byte) 59)) {
                            if (data[pos] == (byte) 41) {
                                pos += 3;
                            } else if (data[pos] == (byte) 16) {
                                pos++;
                            }
                        }
                    }
                }
            }
        } catch (Throwable th) {
            logger.warn("Cannot read name.");
            this.name = "ERROR";
        }
    }

    public String getName() {
        return this.name;
    }

    public BuiltInName getBuiltInName() {
        return this.builtInName;
    }

    public NameRange[] getRanges() {
        return (NameRange[]) this.ranges.toArray(new NameRange[this.ranges.size()]);
    }

    int getIndex() {
        return this.index;
    }

    public int getSheetRef() {
        return this.sheetRef;
    }

    public byte[] getData() {
        return getRecord().getData();
    }

    public boolean isBiff8() {
        return this.isbiff8;
    }

    public boolean isGlobal() {
        return this.sheetRef == 0;
    }
}
