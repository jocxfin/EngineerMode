package jxl.read.biff;

import jxl.Cell;
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
import jxl.biff.formula.FormulaParser;
import jxl.common.Assert;
import jxl.common.Logger;

public class SharedStringFormulaRecord extends BaseSharedFormulaRecord implements LabelCell, FormulaData, StringFormulaCell {
    protected static final EmptyString EMPTY_STRING = new EmptyString();
    private static Logger logger = Logger.getLogger(SharedStringFormulaRecord.class);
    private String value;

    private static final class EmptyString {
        private EmptyString() {
        }
    }

    public SharedStringFormulaRecord(Record t, File excelFile, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si, WorkbookSettings ws) {
        boolean z;
        int startpos;
        boolean unicode;
        super(t, fr, es, nt, si, excelFile.getPos());
        int pos = excelFile.getPos();
        int filepos = excelFile.getPos();
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
        int chars = IntegerHelper.getInt(stringData[0], stringData[1]);
        if (stringData.length == chars + 2) {
            startpos = 2;
            unicode = false;
        } else if (stringData[2] != (byte) 1) {
            startpos = 3;
            unicode = false;
        } else {
            startpos = 3;
            unicode = true;
        }
        if (unicode) {
            this.value = StringHelper.getUnicodeString(stringData, chars, startpos);
        } else {
            this.value = StringHelper.getString(stringData, chars, startpos, ws);
        }
        excelFile.setPos(filepos);
    }

    public SharedStringFormulaRecord(Record t, File excelFile, FormattingRecords fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si, EmptyString dummy) {
        super(t, fr, es, nt, si, excelFile.getPos());
        this.value = "";
    }

    public String getString() {
        return this.value;
    }

    public String getContents() {
        return this.value;
    }

    public CellType getType() {
        return CellType.STRING_FORMULA;
    }

    public byte[] getFormulaData() throws FormulaException {
        if (getSheet().getWorkbookBof().isBiff8()) {
            FormulaParser fp = new FormulaParser(getTokens(), (Cell) this, getExternalSheet(), getNameTable(), getSheet().getWorkbook().getSettings());
            fp.parse();
            byte[] rpnTokens = fp.getBytes();
            byte[] data = new byte[(rpnTokens.length + 22)];
            IntegerHelper.getTwoBytes(getRow(), data, 0);
            IntegerHelper.getTwoBytes(getColumn(), data, 2);
            IntegerHelper.getTwoBytes(getXFIndex(), data, 4);
            data[6] = (byte) 0;
            data[12] = (byte) -1;
            data[13] = (byte) -1;
            System.arraycopy(rpnTokens, 0, data, 22, rpnTokens.length);
            IntegerHelper.getTwoBytes(rpnTokens.length, data, 20);
            byte[] d = new byte[(data.length - 6)];
            System.arraycopy(data, 6, d, 0, data.length - 6);
            return d;
        }
        throw new FormulaException(FormulaException.BIFF8_SUPPORTED);
    }
}
