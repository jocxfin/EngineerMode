package jxl.write.biff;

import jxl.SheetSettings;
import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class Window2Record extends WritableRecordData {
    private byte[] data;

    public Window2Record(SheetSettings settings) {
        super(Type.WINDOW2);
        int options = 0;
        if (settings.getShowGridLines()) {
            options = 0 | 2;
        }
        options = (options | 4) | 0;
        if (settings.getDisplayZeroValues()) {
            options |= 16;
        }
        options = (options | 32) | 128;
        if (settings.getHorizontalFreeze() != 0 || settings.getVerticalFreeze() != 0) {
            options = (options | 8) | 256;
        }
        if (settings.isSelected()) {
            options |= 1536;
        }
        if (settings.getPageBreakPreviewMode()) {
            options |= 2048;
        }
        this.data = new byte[18];
        IntegerHelper.getTwoBytes(options, this.data, 0);
        IntegerHelper.getTwoBytes(64, this.data, 6);
        IntegerHelper.getTwoBytes(settings.getPageBreakPreviewMagnification(), this.data, 10);
        IntegerHelper.getTwoBytes(settings.getNormalMagnification(), this.data, 12);
    }

    public byte[] getData() {
        return this.data;
    }
}
