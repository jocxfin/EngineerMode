package jxl.read.biff;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import jxl.CellReferenceHelper;
import jxl.Hyperlink;
import jxl.Sheet;
import jxl.WorkbookSettings;
import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.biff.SheetRangeImpl;
import jxl.biff.StringHelper;
import jxl.common.Logger;

public class HyperlinkRecord extends RecordData implements Hyperlink {
    private static final LinkType fileLink = new LinkType();
    private static Logger logger = Logger.getLogger(HyperlinkRecord.class);
    private static final LinkType unknown = new LinkType();
    private static final LinkType urlLink = new LinkType();
    private static final LinkType workbookLink = new LinkType();
    private File file;
    private int firstColumn;
    private int firstRow;
    private int lastColumn;
    private int lastRow;
    private LinkType linkType = unknown;
    private String location;
    private SheetRangeImpl range;
    private URL url;

    private static class LinkType {
        private LinkType() {
        }
    }

    HyperlinkRecord(Record t, Sheet s, WorkbookSettings ws) {
        boolean description;
        boolean targetFrame;
        super(t);
        byte[] data = getRecord().getData();
        this.firstRow = IntegerHelper.getInt(data[0], data[1]);
        this.lastRow = IntegerHelper.getInt(data[2], data[3]);
        this.firstColumn = IntegerHelper.getInt(data[4], data[5]);
        this.lastColumn = IntegerHelper.getInt(data[6], data[7]);
        this.range = new SheetRangeImpl(s, this.firstColumn, this.firstRow, this.lastColumn, this.lastRow);
        int options = IntegerHelper.getInt(data[28], data[29], data[30], data[31]);
        if ((options & 20) == 0) {
            description = false;
        } else {
            description = true;
        }
        int descbytes = 0;
        if (description) {
            descbytes = (IntegerHelper.getInt(data[32], data[33], data[34], data[35]) * 2) + 4;
        }
        int startpos = descbytes + 32;
        if ((options & 128) == 0) {
            targetFrame = false;
        } else {
            targetFrame = true;
        }
        int targetbytes = 0;
        if (targetFrame) {
            targetbytes = (IntegerHelper.getInt(data[startpos], data[startpos + 1], data[startpos + 2], data[startpos + 3]) * 2) + 4;
        }
        startpos += targetbytes;
        if ((options & 3) == 3) {
            this.linkType = urlLink;
            if (data[startpos] == (byte) 3) {
                this.linkType = fileLink;
            }
        } else if ((options & 1) != 0) {
            this.linkType = fileLink;
            if (data[startpos] == (byte) -32) {
                this.linkType = urlLink;
            }
        } else if ((options & 8) != 0) {
            this.linkType = workbookLink;
        }
        if (this.linkType == urlLink) {
            String str = null;
            startpos += 16;
            try {
                str = StringHelper.getUnicodeString(data, (IntegerHelper.getInt(data[startpos], data[startpos + 1], data[startpos + 2], data[startpos + 3]) / 2) - 1, startpos + 4);
                this.url = new URL(str);
            } catch (MalformedURLException e) {
                logger.warn("URL " + str + " is malformed.  Trying a file");
                try {
                    this.linkType = fileLink;
                    this.file = new File(str);
                } catch (Exception e2) {
                    logger.warn("Cannot set to file.  Setting a default URL");
                    try {
                        this.linkType = urlLink;
                        this.url = new URL("http://www.andykhan.com/jexcelapi/index.html");
                    } catch (MalformedURLException e3) {
                    }
                }
            } catch (Throwable e4) {
                StringBuffer sb1 = new StringBuffer();
                StringBuffer sb2 = new StringBuffer();
                CellReferenceHelper.getCellReference(this.firstColumn, this.firstRow, sb1);
                CellReferenceHelper.getCellReference(this.lastColumn, this.lastRow, sb2);
                sb1.insert(0, "Exception when parsing URL ");
                sb1.append('\"').append(sb2.toString()).append("\".  Using default.");
                logger.warn(sb1, e4);
                try {
                    this.url = new URL("http://www.andykhan.com/jexcelapi/index.html");
                } catch (MalformedURLException e5) {
                }
            }
        } else if (this.linkType == fileLink) {
            startpos += 16;
            try {
                int upLevelCount = IntegerHelper.getInt(data[startpos], data[startpos + 1]);
                String fileName = StringHelper.getString(data, IntegerHelper.getInt(data[startpos + 2], data[startpos + 3], data[startpos + 4], data[startpos + 5]) - 1, startpos + 6, ws);
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < upLevelCount; i++) {
                    sb.append("..\\");
                }
                sb.append(fileName);
                this.file = new File(sb.toString());
            } catch (Throwable e42) {
                logger.warn("Exception when parsing file " + e42.getClass().getName() + ".");
                this.file = new File(".");
            }
        } else if (this.linkType != workbookLink) {
            logger.warn("Cannot determine link type");
        } else {
            this.location = StringHelper.getUnicodeString(data, IntegerHelper.getInt(data[32], data[33], data[34], data[35]) - 1, 36);
        }
    }

    public boolean isFile() {
        return this.linkType == fileLink;
    }

    public boolean isURL() {
        return this.linkType == urlLink;
    }

    public boolean isLocation() {
        return this.linkType == workbookLink;
    }

    public int getRow() {
        return this.firstRow;
    }

    public int getColumn() {
        return this.firstColumn;
    }

    public int getLastRow() {
        return this.lastRow;
    }

    public int getLastColumn() {
        return this.lastColumn;
    }

    public URL getURL() {
        return this.url;
    }

    public File getFile() {
        return this.file;
    }

    public Record getRecord() {
        return super.getRecord();
    }

    public String getLocation() {
        return this.location;
    }
}
