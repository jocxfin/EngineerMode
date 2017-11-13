package jxl.biff.formula;

import com.android.engineeringmode.functions.Light;

import jxl.WorkbookSettings;
import jxl.biff.StringHelper;
import jxl.common.Logger;

class StringValue extends Operand {
    private static final Logger logger = Logger.getLogger(StringValue.class);
    private WorkbookSettings settings;
    private String value;

    public StringValue(WorkbookSettings ws) {
        this.settings = ws;
    }

    public StringValue(String s) {
        this.value = s;
    }

    public int read(byte[] data, int pos) {
        int length = data[pos] & Light.MAIN_KEY_MAX;
        if ((data[pos + 1] & 1) != 0) {
            this.value = StringHelper.getUnicodeString(data, length, pos + 2);
            return (length * 2) + 2;
        }
        this.value = StringHelper.getString(data, length, pos + 2, this.settings);
        return length + 2;
    }

    byte[] getBytes() {
        byte[] data = new byte[((this.value.length() * 2) + 3)];
        data[0] = (byte) Token.STRING.getCode();
        data[1] = (byte) ((byte) this.value.length());
        data[2] = (byte) 1;
        StringHelper.getUnicodeBytes(this.value, data, 3);
        return data;
    }

    public void getString(StringBuffer buf) {
        buf.append("\"");
        buf.append(this.value);
        buf.append("\"");
    }
}
