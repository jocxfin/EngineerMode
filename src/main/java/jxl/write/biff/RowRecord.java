package jxl.write.biff;

import com.android.engineeringmode.functions.Light;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jxl.CellType;
import jxl.biff.CellReferenceHelper;
import jxl.biff.IndexMapping;
import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;
import jxl.biff.XFRecord;
import jxl.common.Logger;
import jxl.write.Number;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableSheet;

class RowRecord extends WritableRecordData {
    private static int defaultHeightIndicator = Light.MAIN_KEY_MAX;
    private static final Logger logger = Logger.getLogger(RowRecord.class);
    private static int maxColumns = 256;
    private CellValue[] cells = new CellValue[0];
    private boolean collapsed = false;
    private boolean defaultFormat;
    private boolean groupStart;
    private boolean matchesDefFontHeight = true;
    private int numColumns = 0;
    private int outlineLevel;
    private int rowHeight = defaultHeightIndicator;
    private int rowNumber;
    private WritableSheet sheet;
    private XFRecord style;
    private int xfIndex;

    public RowRecord(int rn, WritableSheet ws) {
        super(Type.ROW);
        this.rowNumber = rn;
        this.sheet = ws;
    }

    void setRowDetails(int height, boolean mdfh, boolean col, int ol, boolean gs, XFRecord xfr) {
        this.rowHeight = height;
        this.collapsed = col;
        this.matchesDefFontHeight = mdfh;
        this.outlineLevel = ol;
        this.groupStart = gs;
        if (xfr != null) {
            this.defaultFormat = true;
            this.style = xfr;
            this.xfIndex = this.style.getXFIndex();
        }
    }

    public void addCell(CellValue cv) {
        int col = cv.getColumn();
        if (col < maxColumns) {
            if (col >= this.cells.length) {
                CellValue[] oldCells = this.cells;
                this.cells = new CellValue[Math.max(oldCells.length + 10, col + 1)];
                System.arraycopy(oldCells, 0, this.cells, 0, oldCells.length);
            }
            if (this.cells[col] != null) {
                WritableCellFeatures wcf = this.cells[col].getWritableCellFeatures();
                if (wcf != null) {
                    wcf.removeComment();
                    if (!(wcf.getDVParser() == null || wcf.getDVParser().extendedCellsValidation())) {
                        wcf.removeDataValidation();
                    }
                }
            }
            this.cells[col] = cv;
            this.numColumns = Math.max(col + 1, this.numColumns);
            return;
        }
        logger.warn("Could not add cell at " + CellReferenceHelper.getCellReference(cv.getRow(), cv.getColumn()) + " because it exceeds the maximum column limit");
    }

    public void write(File outputFile) throws IOException {
        outputFile.write(this);
    }

    public void writeCells(File outputFile) throws IOException {
        ArrayList integerValues = new ArrayList();
        for (int i = 0; i < this.numColumns; i++) {
            boolean integerValue = false;
            if (this.cells[i] == null) {
                writeIntegerValues(integerValues, outputFile);
            } else {
                if (this.cells[i].getType() == CellType.NUMBER) {
                    Number nc = this.cells[i];
                    if (nc.getValue() == ((double) ((int) nc.getValue())) && nc.getValue() < 5.36870911E8d && nc.getValue() > -5.36870912E8d && nc.getCellFeatures() == null) {
                        integerValue = true;
                    }
                }
                if (integerValue) {
                    integerValues.add(this.cells[i]);
                } else {
                    writeIntegerValues(integerValues, outputFile);
                    outputFile.write(this.cells[i]);
                    if (this.cells[i].getType() == CellType.STRING_FORMULA) {
                        outputFile.write(new StringRecord(this.cells[i].getContents()));
                    }
                }
            }
        }
        writeIntegerValues(integerValues, outputFile);
    }

    private void writeIntegerValues(ArrayList integerValues, File outputFile) throws IOException {
        if (integerValues.size() != 0) {
            if (integerValues.size() < 3) {
                Iterator i = integerValues.iterator();
                while (i.hasNext()) {
                    outputFile.write((CellValue) i.next());
                }
            } else {
                outputFile.write(new MulRKRecord(integerValues));
            }
            integerValues.clear();
        }
    }

    public byte[] getData() {
        byte[] data = new byte[16];
        int rh = this.rowHeight;
        if (this.sheet.getSettings().getDefaultRowHeight() != Light.MAIN_KEY_MAX && rh == defaultHeightIndicator) {
            rh = this.sheet.getSettings().getDefaultRowHeight();
        }
        IntegerHelper.getTwoBytes(this.rowNumber, data, 0);
        IntegerHelper.getTwoBytes(this.numColumns, data, 4);
        IntegerHelper.getTwoBytes(rh, data, 6);
        int options = this.outlineLevel + 256;
        if (this.groupStart) {
            options |= 16;
        }
        if (this.collapsed) {
            options |= 32;
        }
        if (!this.matchesDefFontHeight) {
            options |= 64;
        }
        if (this.defaultFormat) {
            options = (options | 128) | (this.xfIndex << 16);
        }
        IntegerHelper.getFourBytes(options, data, 12);
        return data;
    }

    public int getMaxColumn() {
        return this.numColumns;
    }

    public CellValue getCell(int col) {
        return (col >= 0 && col < this.numColumns) ? this.cells[col] : null;
    }

    void rationalize(IndexMapping xfmapping) {
        if (this.defaultFormat) {
            this.xfIndex = xfmapping.getNewIndex(this.xfIndex);
        }
    }
}
