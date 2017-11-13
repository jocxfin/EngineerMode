package jxl.write.biff;

import jxl.WorkbookSettings;
import jxl.biff.EncodedURLHelper;
import jxl.biff.IntegerHelper;
import jxl.biff.StringHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;
import jxl.common.Assert;
import jxl.common.Logger;

class SupbookRecord extends WritableRecordData {
    public static final SupbookType ADDIN = new SupbookType();
    public static final SupbookType EXTERNAL = new SupbookType();
    public static final SupbookType INTERNAL = new SupbookType();
    public static final SupbookType LINK = new SupbookType();
    public static final SupbookType UNKNOWN = new SupbookType();
    private static Logger logger = Logger.getLogger(SupbookRecord.class);
    private byte[] data;
    private String fileName;
    private int numSheets;
    private String[] sheetNames;
    private SupbookType type;
    private WorkbookSettings workbookSettings;

    private static class SupbookType {
        private SupbookType() {
        }
    }

    public SupbookRecord() {
        super(Type.SUPBOOK);
        this.type = ADDIN;
    }

    public SupbookRecord(int sheets, WorkbookSettings ws) {
        super(Type.SUPBOOK);
        this.numSheets = sheets;
        this.type = INTERNAL;
        this.workbookSettings = ws;
    }

    public SupbookRecord(String fn, WorkbookSettings ws) {
        super(Type.SUPBOOK);
        this.fileName = fn;
        this.numSheets = 1;
        this.sheetNames = new String[0];
        this.workbookSettings = ws;
        this.type = EXTERNAL;
    }

    public SupbookRecord(jxl.read.biff.SupbookRecord sr, WorkbookSettings ws) {
        super(Type.SUPBOOK);
        this.workbookSettings = ws;
        if (sr.getType() == jxl.read.biff.SupbookRecord.INTERNAL) {
            this.type = INTERNAL;
            this.numSheets = sr.getNumberOfSheets();
        } else if (sr.getType() == jxl.read.biff.SupbookRecord.EXTERNAL) {
            this.type = EXTERNAL;
            this.numSheets = sr.getNumberOfSheets();
            this.fileName = sr.getFileName();
            this.sheetNames = new String[this.numSheets];
            for (int i = 0; i < this.numSheets; i++) {
                this.sheetNames[i] = sr.getSheetName(i);
            }
        }
        if (sr.getType() == jxl.read.biff.SupbookRecord.ADDIN) {
            logger.warn("Supbook type is addin");
        }
    }

    private void initInternal() {
        this.data = new byte[4];
        IntegerHelper.getTwoBytes(this.numSheets, this.data, 0);
        this.data[2] = (byte) 1;
        this.data[3] = (byte) 4;
        this.type = INTERNAL;
    }

    void adjustInternal(int sheets) {
        boolean z;
        if (this.type != INTERNAL) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
        this.numSheets = sheets;
        initInternal();
    }

    private void initExternal() {
        int i;
        int totalSheetNameLength = 0;
        for (i = 0; i < this.numSheets; i++) {
            totalSheetNameLength += this.sheetNames[i].length();
        }
        byte[] fileNameData = EncodedURLHelper.getEncodedURL(this.fileName, this.workbookSettings);
        this.data = new byte[(((fileNameData.length + 6) + (this.numSheets * 3)) + (totalSheetNameLength * 2))];
        IntegerHelper.getTwoBytes(this.numSheets, this.data, 0);
        IntegerHelper.getTwoBytes(fileNameData.length + 1, this.data, 2);
        this.data[4] = (byte) 0;
        this.data[5] = (byte) 1;
        System.arraycopy(fileNameData, 0, this.data, 6, fileNameData.length);
        int pos = (fileNameData.length + 4) + 2;
        for (i = 0; i < this.sheetNames.length; i++) {
            IntegerHelper.getTwoBytes(this.sheetNames[i].length(), this.data, pos);
            this.data[pos + 2] = (byte) 1;
            StringHelper.getUnicodeBytes(this.sheetNames[i], this.data, pos + 3);
            pos += (this.sheetNames[i].length() * 2) + 3;
        }
    }

    private void initAddin() {
        this.data = new byte[]{(byte) 1, (byte) 0, (byte) 1, (byte) 58};
    }

    public byte[] getData() {
        if (this.type == INTERNAL) {
            initInternal();
        } else if (this.type == EXTERNAL) {
            initExternal();
        } else if (this.type != ADDIN) {
            logger.warn("unsupported supbook type - defaulting to internal");
            initInternal();
        } else {
            initAddin();
        }
        return this.data;
    }

    public SupbookType getType() {
        return this.type;
    }

    public int getNumberOfSheets() {
        return this.numSheets;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getSheetIndex(String s) {
        boolean found = false;
        int sheetIndex = 0;
        for (int i = 0; i < this.sheetNames.length && !found; i++) {
            if (this.sheetNames[i].equals(s)) {
                found = true;
                sheetIndex = 0;
            }
        }
        if (found) {
            return sheetIndex;
        }
        String[] names = new String[(this.sheetNames.length + 1)];
        System.arraycopy(this.sheetNames, 0, names, 0, this.sheetNames.length);
        names[this.sheetNames.length] = s;
        this.sheetNames = names;
        return this.sheetNames.length - 1;
    }

    public String getSheetName(int s) {
        return this.sheetNames[s];
    }
}
