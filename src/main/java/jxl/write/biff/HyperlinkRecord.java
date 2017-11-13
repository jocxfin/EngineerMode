package jxl.write.biff;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import jxl.Hyperlink;
import jxl.Range;
import jxl.biff.IntegerHelper;
import jxl.biff.SheetRangeImpl;
import jxl.biff.StringHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.write.WritableSheet;

public class HyperlinkRecord extends WritableRecordData {
    private static final LinkType fileLink = new LinkType();
    private static Logger logger = Logger.getLogger(HyperlinkRecord.class);
    private static final LinkType uncLink = new LinkType();
    private static final LinkType unknown = new LinkType();
    private static final LinkType urlLink = new LinkType();
    private static final LinkType workbookLink = new LinkType();
    private String contents;
    private byte[] data;
    private File file;
    private int firstColumn;
    private int firstRow;
    private int lastColumn;
    private int lastRow;
    private LinkType linkType;
    private String location;
    private boolean modified;
    private Range range;
    private WritableSheet sheet;
    private URL url;

    private static class LinkType {
        private LinkType() {
        }
    }

    protected HyperlinkRecord(Hyperlink h, WritableSheet s) {
        super(Type.HLINK);
        if (h instanceof jxl.read.biff.HyperlinkRecord) {
            copyReadHyperlink(h, s);
        } else {
            copyWritableHyperlink(h, s);
        }
    }

    private void copyReadHyperlink(Hyperlink h, WritableSheet s) {
        jxl.read.biff.HyperlinkRecord hl = (jxl.read.biff.HyperlinkRecord) h;
        this.data = hl.getRecord().getData();
        this.sheet = s;
        this.firstRow = hl.getRow();
        this.firstColumn = hl.getColumn();
        this.lastRow = hl.getLastRow();
        this.lastColumn = hl.getLastColumn();
        this.range = new SheetRangeImpl(s, this.firstColumn, this.firstRow, this.lastColumn, this.lastRow);
        this.linkType = unknown;
        if (hl.isFile()) {
            this.linkType = fileLink;
            this.file = hl.getFile();
        } else if (hl.isURL()) {
            this.linkType = urlLink;
            this.url = hl.getURL();
        } else if (hl.isLocation()) {
            this.linkType = workbookLink;
            this.location = hl.getLocation();
        }
        this.modified = false;
    }

    private void copyWritableHyperlink(Hyperlink hl, WritableSheet s) {
        HyperlinkRecord h = (HyperlinkRecord) hl;
        this.firstRow = h.firstRow;
        this.lastRow = h.lastRow;
        this.firstColumn = h.firstColumn;
        this.lastColumn = h.lastColumn;
        if (h.url != null) {
            try {
                this.url = new URL(h.url.toString());
            } catch (MalformedURLException e) {
                Assert.verify(false);
            }
        }
        if (h.file != null) {
            this.file = new File(h.file.getPath());
        }
        this.location = h.location;
        this.contents = h.contents;
        this.linkType = h.linkType;
        this.modified = true;
        this.sheet = s;
        this.range = new SheetRangeImpl(s, this.firstColumn, this.firstRow, this.lastColumn, this.lastRow);
    }

    public boolean isFile() {
        return this.linkType == fileLink;
    }

    public boolean isUNC() {
        return this.linkType == uncLink;
    }

    public boolean isURL() {
        return this.linkType == urlLink;
    }

    public boolean isLocation() {
        return this.linkType == workbookLink;
    }

    public byte[] getData() {
        if (!this.modified) {
            return this.data;
        }
        byte[] commonData = new byte[32];
        IntegerHelper.getTwoBytes(this.firstRow, commonData, 0);
        IntegerHelper.getTwoBytes(this.lastRow, commonData, 2);
        IntegerHelper.getTwoBytes(this.firstColumn, commonData, 4);
        IntegerHelper.getTwoBytes(this.lastColumn, commonData, 6);
        commonData[8] = (byte) -48;
        commonData[9] = (byte) -55;
        commonData[10] = (byte) -22;
        commonData[11] = (byte) 121;
        commonData[12] = (byte) -7;
        commonData[13] = (byte) -70;
        commonData[14] = (byte) -50;
        commonData[15] = (byte) 17;
        commonData[16] = (byte) -116;
        commonData[17] = (byte) -126;
        commonData[18] = (byte) 0;
        commonData[19] = (byte) -86;
        commonData[20] = (byte) 0;
        commonData[21] = (byte) 75;
        commonData[22] = (byte) -87;
        commonData[23] = (byte) 11;
        commonData[24] = (byte) 2;
        commonData[25] = (byte) 0;
        commonData[26] = (byte) 0;
        commonData[27] = (byte) 0;
        int optionFlags = 0;
        if (isURL()) {
            optionFlags = 3;
            if (this.contents != null) {
                optionFlags = 23;
            }
        } else if (isFile()) {
            optionFlags = 1;
            if (this.contents != null) {
                optionFlags = 21;
            }
        } else if (isLocation()) {
            optionFlags = 8;
        } else if (isUNC()) {
            optionFlags = 259;
        }
        IntegerHelper.getFourBytes(optionFlags, commonData, 28);
        if (isURL()) {
            this.data = getURLData(commonData);
        } else if (isFile()) {
            this.data = getFileData(commonData);
        } else if (isLocation()) {
            this.data = getLocationData(commonData);
        } else if (isUNC()) {
            this.data = getUNCData(commonData);
        }
        return this.data;
    }

    public String toString() {
        if (isFile()) {
            return this.file.toString();
        }
        if (isURL()) {
            return this.url.toString();
        }
        if (isUNC()) {
            return this.file.toString();
        }
        return "";
    }

    private byte[] getURLData(byte[] cd) {
        String urlString = this.url.toString();
        int dataLength = (cd.length + 20) + ((urlString.length() + 1) * 2);
        if (this.contents != null) {
            dataLength += ((this.contents.length() + 1) * 2) + 4;
        }
        byte[] d = new byte[dataLength];
        System.arraycopy(cd, 0, d, 0, cd.length);
        int urlPos = cd.length;
        if (this.contents != null) {
            IntegerHelper.getFourBytes(this.contents.length() + 1, d, urlPos);
            StringHelper.getUnicodeBytes(this.contents, d, urlPos + 4);
            urlPos += ((this.contents.length() + 1) * 2) + 4;
        }
        d[urlPos] = (byte) -32;
        d[urlPos + 1] = (byte) -55;
        d[urlPos + 2] = (byte) -22;
        d[urlPos + 3] = (byte) 121;
        d[urlPos + 4] = (byte) -7;
        d[urlPos + 5] = (byte) -70;
        d[urlPos + 6] = (byte) -50;
        d[urlPos + 7] = (byte) 17;
        d[urlPos + 8] = (byte) -116;
        d[urlPos + 9] = (byte) -126;
        d[urlPos + 10] = (byte) 0;
        d[urlPos + 11] = (byte) -86;
        d[urlPos + 12] = (byte) 0;
        d[urlPos + 13] = (byte) 75;
        d[urlPos + 14] = (byte) -87;
        d[urlPos + 15] = (byte) 11;
        IntegerHelper.getFourBytes((urlString.length() + 1) * 2, d, urlPos + 16);
        StringHelper.getUnicodeBytes(urlString, d, urlPos + 20);
        return d;
    }

    private byte[] getUNCData(byte[] cd) {
        String uncString = this.file.getPath();
        byte[] d = new byte[(((cd.length + (uncString.length() * 2)) + 2) + 4)];
        System.arraycopy(cd, 0, d, 0, cd.length);
        int urlPos = cd.length;
        IntegerHelper.getFourBytes(uncString.length() + 1, d, urlPos);
        StringHelper.getUnicodeBytes(uncString, d, urlPos + 4);
        return d;
    }

    private byte[] getFileData(byte[] cd) {
        ArrayList path = new ArrayList();
        ArrayList shortFileName = new ArrayList();
        path.add(this.file.getName());
        shortFileName.add(getShortName(this.file.getName()));
        for (File parent = this.file.getParentFile(); parent != null; parent = parent.getParentFile()) {
            path.add(parent.getName());
            shortFileName.add(getShortName(parent.getName()));
        }
        int upLevelCount = 0;
        int pos = path.size() - 1;
        boolean z = true;
        while (z) {
            if (((String) path.get(pos)).equals("..")) {
                upLevelCount++;
                path.remove(pos);
                shortFileName.remove(pos);
            } else {
                z = false;
            }
            pos--;
        }
        StringBuffer filePathSB = new StringBuffer();
        StringBuffer shortFilePathSB = new StringBuffer();
        if (this.file.getPath().charAt(1) == ':') {
            char driveLetter = this.file.getPath().charAt(0);
            if (!(driveLetter == 'C' || driveLetter == 'c')) {
                filePathSB.append(driveLetter);
                filePathSB.append(':');
                shortFilePathSB.append(driveLetter);
                shortFilePathSB.append(':');
            }
        }
        for (int i = path.size() - 1; i >= 0; i--) {
            filePathSB.append((String) path.get(i));
            shortFilePathSB.append((String) shortFileName.get(i));
            if (i != 0) {
                filePathSB.append("\\");
                shortFilePathSB.append("\\");
            }
        }
        String filePath = filePathSB.toString();
        String shortFilePath = shortFilePathSB.toString();
        int dataLength = ((((((cd.length + 4) + (shortFilePath.length() + 1)) + 16) + 2) + 8) + ((filePath.length() + 1) * 2)) + 24;
        if (this.contents != null) {
            dataLength += ((this.contents.length() + 1) * 2) + 4;
        }
        byte[] d = new byte[dataLength];
        System.arraycopy(cd, 0, d, 0, cd.length);
        int filePos = cd.length;
        if (this.contents != null) {
            IntegerHelper.getFourBytes(this.contents.length() + 1, d, filePos);
            StringHelper.getUnicodeBytes(this.contents, d, filePos + 4);
            filePos += ((this.contents.length() + 1) * 2) + 4;
        }
        int curPos = filePos;
        d[curPos] = (byte) 3;
        d[curPos + 1] = (byte) 3;
        d[curPos + 2] = (byte) 0;
        d[curPos + 3] = (byte) 0;
        d[curPos + 4] = (byte) 0;
        d[curPos + 5] = (byte) 0;
        d[curPos + 6] = (byte) 0;
        d[curPos + 7] = (byte) 0;
        d[curPos + 8] = (byte) -64;
        d[curPos + 9] = (byte) 0;
        d[curPos + 10] = (byte) 0;
        d[curPos + 11] = (byte) 0;
        d[curPos + 12] = (byte) 0;
        d[curPos + 13] = (byte) 0;
        d[curPos + 14] = (byte) 0;
        d[curPos + 15] = (byte) 70;
        curPos += 16;
        IntegerHelper.getTwoBytes(upLevelCount, d, curPos);
        curPos += 2;
        IntegerHelper.getFourBytes(shortFilePath.length() + 1, d, curPos);
        StringHelper.getBytes(shortFilePath, d, curPos + 4);
        curPos += (shortFilePath.length() + 1) + 4;
        d[curPos] = (byte) -1;
        d[curPos + 1] = (byte) -1;
        d[curPos + 2] = (byte) -83;
        d[curPos + 3] = (byte) -34;
        d[curPos + 4] = (byte) 0;
        d[curPos + 5] = (byte) 0;
        d[curPos + 6] = (byte) 0;
        d[curPos + 7] = (byte) 0;
        d[curPos + 8] = (byte) 0;
        d[curPos + 9] = (byte) 0;
        d[curPos + 10] = (byte) 0;
        d[curPos + 11] = (byte) 0;
        d[curPos + 12] = (byte) 0;
        d[curPos + 13] = (byte) 0;
        d[curPos + 14] = (byte) 0;
        d[curPos + 15] = (byte) 0;
        d[curPos + 16] = (byte) 0;
        d[curPos + 17] = (byte) 0;
        d[curPos + 18] = (byte) 0;
        d[curPos + 19] = (byte) 0;
        d[curPos + 20] = (byte) 0;
        d[curPos + 21] = (byte) 0;
        d[curPos + 22] = (byte) 0;
        d[curPos + 23] = (byte) 0;
        curPos += 24;
        IntegerHelper.getFourBytes((filePath.length() * 2) + 6, d, curPos);
        curPos += 4;
        IntegerHelper.getFourBytes(filePath.length() * 2, d, curPos);
        curPos += 4;
        d[curPos] = (byte) 3;
        d[curPos + 1] = (byte) 0;
        curPos += 2;
        StringHelper.getUnicodeBytes(filePath, d, curPos);
        curPos += (filePath.length() + 1) * 2;
        return d;
    }

    private String getShortName(String s) {
        String prefix;
        String suffix;
        int sep = s.indexOf(46);
        if (sep != -1) {
            prefix = s.substring(0, sep);
            suffix = s.substring(sep + 1);
        } else {
            prefix = s;
            suffix = "";
        }
        if (prefix.length() > 8) {
            prefix = (prefix.substring(0, 6) + "~" + (prefix.length() - 8)).substring(0, 8);
        }
        suffix = suffix.substring(0, Math.min(3, suffix.length()));
        if (suffix.length() <= 0) {
            return prefix;
        }
        return prefix + '.' + suffix;
    }

    private byte[] getLocationData(byte[] cd) {
        byte[] d = new byte[((cd.length + 4) + ((this.location.length() + 1) * 2))];
        System.arraycopy(cd, 0, d, 0, cd.length);
        int locPos = cd.length;
        IntegerHelper.getFourBytes(this.location.length() + 1, d, locPos);
        StringHelper.getUnicodeBytes(this.location, d, locPos + 4);
        return d;
    }
}
