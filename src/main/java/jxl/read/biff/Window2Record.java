package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

class Window2Record extends RecordData {
    public static final Biff7 biff7 = new Biff7();
    private static Logger logger = Logger.getLogger(Window2Record.class);
    private boolean displayZeroValues;
    private boolean frozenNotSplit;
    private boolean frozenPanes;
    private int normalMagnification;
    private int pageBreakPreviewMagnification;
    private boolean pageBreakPreviewMode;
    private boolean selected;
    private boolean showGridLines;

    private static class Biff7 {
        private Biff7() {
        }
    }

    public Window2Record(Record t) {
        boolean z;
        boolean z2 = false;
        super(t);
        byte[] data = t.getData();
        int options = IntegerHelper.getInt(data[0], data[1]);
        this.selected = (options & 512) != 0;
        if ((options & 2) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.showGridLines = z;
        if ((options & 8) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.frozenPanes = z;
        if ((options & 16) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.displayZeroValues = z;
        if ((options & 256) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.frozenNotSplit = z;
        if ((options & 2048) != 0) {
            z2 = true;
        }
        this.pageBreakPreviewMode = z2;
        this.pageBreakPreviewMagnification = IntegerHelper.getInt(data[10], data[11]);
        this.normalMagnification = IntegerHelper.getInt(data[12], data[13]);
    }

    public Window2Record(Record t, Biff7 biff7) {
        boolean z;
        boolean z2 = false;
        super(t);
        byte[] data = t.getData();
        int options = IntegerHelper.getInt(data[0], data[1]);
        this.selected = (options & 512) != 0;
        if ((options & 2) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.showGridLines = z;
        if ((options & 8) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.frozenPanes = z;
        if ((options & 16) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.displayZeroValues = z;
        if ((options & 256) == 0) {
            z = false;
        } else {
            z = true;
        }
        this.frozenNotSplit = z;
        if ((options & 2048) != 0) {
            z2 = true;
        }
        this.pageBreakPreviewMode = z2;
    }

    public boolean getShowGridLines() {
        return this.showGridLines;
    }

    public boolean getDisplayZeroValues() {
        return this.displayZeroValues;
    }

    public boolean getFrozen() {
        return this.frozenPanes;
    }

    public boolean isPageBreakPreview() {
        return this.pageBreakPreviewMode;
    }
}
