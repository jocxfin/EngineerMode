package jxl.read.biff;

import jxl.CellType;
import jxl.LabelCell;
import jxl.StringFormulaCell;
import jxl.WorkbookSettings;
import jxl.biff.FormattingRecords;
import jxl.biff.FormulaData;
import jxl.biff.IntegerHelper;
import jxl.biff.StringHelper;
import jxl.biff.Type;
import jxl.biff.WorkbookMethods;
import jxl.biff.formula.ExternalSheet;
import jxl.biff.formula.FormulaException;
import jxl.common.Assert;
import jxl.common.Logger;

class StringFormulaRecord extends CellValue implements LabelCell, FormulaData, StringFormulaCell {
    private static Logger logger = Logger.getLogger(StringFormulaRecord.class);
    private byte[] data;
    private ExternalSheet externalSheet;
    private WorkbookMethods nameTable;
    private String value;

    public StringFormulaRecord(Record t, File excelFile, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si, WorkbookSettings ws) {
        boolean z;
        super(t, fr, si);
        this.externalSheet = es;
        this.nameTable = nt;
        this.data = getRecord().getData();
        int pos = excelFile.getPos();
        Record nextRecord = excelFile.next();
        int count = 0;
        while (nextRecord.getType() != Type.STRING && count < 4) {
            nextRecord = excelFile.next();
            count++;
        }
        if (count >= 4) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z, " @ " + pos);
        byte[] stringData = nextRecord.getData();
        nextRecord = excelFile.peek();
        while (nextRecord.getType() == Type.CONTINUE) {
            nextRecord = excelFile.next();
            byte[] d = new byte[((stringData.length + nextRecord.getLength()) - 1)];
            System.arraycopy(stringData, 0, d, 0, stringData.length);
            System.arraycopy(nextRecord.getData(), 1, d, stringData.length, nextRecord.getLength() - 1);
            stringData = d;
            nextRecord = excelFile.peek();
        }
        readString(stringData, ws);
    }

    public StringFormulaRecord(Record t, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si) {
        super(t, fr, si);
        this.externalSheet = es;
        this.nameTable = nt;
        this.data = getRecord().getData();
        this.value = "";
    }

    private void readString(byte[] d, WorkbookSettings ws) {
        int chars = IntegerHelper.getInt(d[0], d[1]);
        if (chars != 0) {
            boolean extendedString;
            boolean richString;
            boolean asciiEncoding;
            int optionFlags = d[2];
            int pos = 2 + 1;
            if ((optionFlags & 15) != optionFlags) {
                chars = IntegerHelper.getInt(d[0], (byte) 0);
                optionFlags = d[1];
                pos = 2;
            }
            if ((optionFlags & 4) == 0) {
                extendedString = false;
            } else {
                extendedString = true;
            }
            if ((optionFlags & 8) == 0) {
                richString = false;
            } else {
                richString = true;
            }
            if (richString) {
                pos += 2;
            }
            if (extendedString) {
                pos += 4;
            }
            if ((optionFlags & 1) != 0) {
                asciiEncoding = false;
            } else {
                asciiEncoding = true;
            }
            if (asciiEncoding) {
                this.value = StringHelper.getString(d, chars, pos, ws);
            } else {
                this.value = StringHelper.getUnicodeString(d, chars, pos);
            }
            return;
        }
        this.value = "";
    }

    public String getContents() {
        return this.value;
    }

    public String getString() {
        return this.value;
    }

    public CellType getType() {
        return CellType.STRING_FORMULA;
    }

    public byte[] getFormulaData() throws FormulaException {
        if (getSheet().getWorkbook().getWorkbookBof().isBiff8()) {
            byte[] d = new byte[(this.data.length - 6)];
            System.arraycopy(this.data, 6, d, 0, this.data.length - 6);
            return d;
        }
        throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
    }
}
