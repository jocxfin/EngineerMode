package jxl.write.biff;

import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class TemplateRecord extends WritableRecordData {
    public TemplateRecord() {
        super(Type.TEMPLATE);
    }

    public byte[] getData() {
        return new byte[0];
    }
}
