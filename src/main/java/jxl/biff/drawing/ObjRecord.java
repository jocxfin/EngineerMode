package jxl.biff.drawing;

import com.android.engineeringmode.functions.Light;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.read.biff.Record;

public class ObjRecord extends WritableRecordData {
    public static final ObjType ARC = new ObjType(4, "Arc");
    public static final ObjType BUTTON = new ObjType(7, "Button");
    public static final ObjType CHART = new ObjType(5, "Chart");
    public static final ObjType CHECKBOX = new ObjType(11, "Checkbox");
    public static final ObjType COMBOBOX = new ObjType(20, "Combo Box");
    public static final ObjType DIALOGUEBOX = new ObjType(15, "Dialogue Box");
    public static final ObjType EDITBOX = new ObjType(13, "Edit Box");
    public static final ObjType EXCELNOTE = new ObjType(25, "Excel Note");
    public static final ObjType FORMCONTROL = new ObjType(20, "Form Combo Box");
    public static final ObjType GROUP = new ObjType(0, "Group");
    public static final ObjType GROUPBOX = new ObjType(19, "Group Box");
    public static final ObjType LABEL = new ObjType(14, "Label");
    public static final ObjType LINE = new ObjType(1, "Line");
    public static final ObjType LISTBOX = new ObjType(18, "List Box");
    public static final ObjType MSOFFICEDRAWING = new ObjType(30, "MS Office Drawing");
    public static final ObjType OPTION = new ObjType(12, "Option");
    public static final ObjType OVAL = new ObjType(3, "Oval");
    public static final ObjType PICTURE = new ObjType(8, "Picture");
    public static final ObjType POLYGON = new ObjType(9, "Polygon");
    public static final ObjType RECTANGLE = new ObjType(2, "Rectangle");
    public static final ObjType SCROLLBAR = new ObjType(17, "Scrollbar");
    public static final ObjType SPINBOX = new ObjType(16, "Spin Box");
    public static final ObjType TEXT = new ObjType(6, "Text");
    public static final ObjType UNKNOWN = new ObjType(Light.MAIN_KEY_MAX, "Unknown");
    private static final Logger logger = Logger.getLogger(ObjRecord.class);
    private int objectId;
    private boolean read;
    private ObjType type;

    private static final class ObjType {
        private static ObjType[] types = new ObjType[0];
        public String desc;
        public int value;

        ObjType(int v, String d) {
            this.value = v;
            this.desc = d;
            ObjType[] oldtypes = types;
            types = new ObjType[(types.length + 1)];
            System.arraycopy(oldtypes, 0, types, 0, oldtypes.length);
            types[oldtypes.length] = this;
        }

        public String toString() {
            return this.desc;
        }

        public static ObjType getType(int val) {
            ObjType retval = ObjRecord.UNKNOWN;
            for (int i = 0; i < types.length && retval == ObjRecord.UNKNOWN; i++) {
                if (types[i].value == val) {
                    retval = types[i];
                }
            }
            return retval;
        }
    }

    public ObjRecord(Record t) {
        super(t);
        byte[] data = t.getData();
        int objtype = IntegerHelper.getInt(data[4], data[5]);
        this.read = true;
        this.type = ObjType.getType(objtype);
        if (this.type == UNKNOWN) {
            logger.warn("unknown object type code " + objtype);
        }
        this.objectId = IntegerHelper.getInt(data[6], data[7]);
    }

    ObjRecord(int objId, ObjType t) {
        super(Type.OBJ);
        this.objectId = objId;
        this.type = t;
    }

    public byte[] getData() {
        if (this.read) {
            return getRecord().getData();
        }
        if (this.type == PICTURE || this.type == CHART) {
            return getPictureData();
        }
        if (this.type == EXCELNOTE) {
            return getNoteData();
        }
        if (this.type == COMBOBOX) {
            return getComboBoxData();
        }
        Assert.verify(false);
        return null;
    }

    private byte[] getPictureData() {
        byte[] data = new byte[38];
        IntegerHelper.getTwoBytes(21, data, 0);
        IntegerHelper.getTwoBytes(18, data, 2);
        IntegerHelper.getTwoBytes(this.type.value, data, 4);
        IntegerHelper.getTwoBytes(this.objectId, data, 6);
        IntegerHelper.getTwoBytes(24593, data, 8);
        IntegerHelper.getTwoBytes(7, data, 22);
        IntegerHelper.getTwoBytes(2, data, 24);
        IntegerHelper.getTwoBytes(65535, data, 26);
        int pos = 22 + 6;
        IntegerHelper.getTwoBytes(8, data, pos);
        IntegerHelper.getTwoBytes(2, data, 30);
        IntegerHelper.getTwoBytes(1, data, 32);
        pos += 6;
        IntegerHelper.getTwoBytes(0, data, pos);
        IntegerHelper.getTwoBytes(0, data, 36);
        pos += 4;
        return data;
    }

    private byte[] getNoteData() {
        byte[] data = new byte[52];
        IntegerHelper.getTwoBytes(21, data, 0);
        IntegerHelper.getTwoBytes(18, data, 2);
        IntegerHelper.getTwoBytes(this.type.value, data, 4);
        IntegerHelper.getTwoBytes(this.objectId, data, 6);
        IntegerHelper.getTwoBytes(16401, data, 8);
        IntegerHelper.getTwoBytes(13, data, 22);
        IntegerHelper.getTwoBytes(22, data, 24);
        int pos = 22 + 26;
        IntegerHelper.getTwoBytes(0, data, pos);
        IntegerHelper.getTwoBytes(0, data, 50);
        pos += 4;
        return data;
    }

    private byte[] getComboBoxData() {
        byte[] data = new byte[70];
        IntegerHelper.getTwoBytes(21, data, 0);
        IntegerHelper.getTwoBytes(18, data, 2);
        IntegerHelper.getTwoBytes(this.type.value, data, 4);
        IntegerHelper.getTwoBytes(this.objectId, data, 6);
        IntegerHelper.getTwoBytes(0, data, 8);
        IntegerHelper.getTwoBytes(12, data, 22);
        IntegerHelper.getTwoBytes(20, data, 24);
        data[36] = (byte) 1;
        data[38] = (byte) 4;
        data[42] = (byte) 16;
        data[46] = (byte) 19;
        data[48] = (byte) -18;
        data[49] = (byte) 31;
        data[52] = (byte) 4;
        data[56] = (byte) 1;
        data[57] = (byte) 6;
        data[60] = (byte) 2;
        data[62] = (byte) 8;
        data[64] = (byte) 64;
        int pos = 22 + 44;
        IntegerHelper.getTwoBytes(0, data, pos);
        IntegerHelper.getTwoBytes(0, data, 68);
        pos += 4;
        return data;
    }

    public Record getRecord() {
        return super.getRecord();
    }

    public ObjType getType() {
        return this.type;
    }

    public int getObjectId() {
        return this.objectId;
    }
}
