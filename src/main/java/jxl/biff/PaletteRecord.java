package jxl.biff;

import com.android.engineeringmode.functions.Light;

import jxl.format.Colour;
import jxl.format.RGB;
import jxl.read.biff.Record;

public class PaletteRecord extends WritableRecordData {
    private boolean dirty;
    private boolean initialized;
    private boolean read;
    private RGB[] rgbColours;

    public PaletteRecord(Record t) {
        super(t);
        this.rgbColours = new RGB[56];
        this.initialized = false;
        this.dirty = false;
        this.read = true;
    }

    public PaletteRecord() {
        super(Type.PALETTE);
        this.rgbColours = new RGB[56];
        this.initialized = true;
        this.dirty = false;
        this.read = false;
        Colour[] colours = Colour.getAllColours();
        for (Colour c : colours) {
            setColourRGB(c, c.getDefaultRGB().getRed(), c.getDefaultRGB().getGreen(), c.getDefaultRGB().getBlue());
        }
    }

    public byte[] getData() {
        if (this.read && !this.dirty) {
            return getRecord().getData();
        }
        byte[] data = new byte[226];
        IntegerHelper.getTwoBytes(56, data, 0);
        for (int i = 0; i < 56; i++) {
            int pos = (i * 4) + 2;
            data[pos] = (byte) ((byte) this.rgbColours[i].getRed());
            data[pos + 1] = (byte) ((byte) this.rgbColours[i].getGreen());
            data[pos + 2] = (byte) ((byte) this.rgbColours[i].getBlue());
        }
        return data;
    }

    private void initialize() {
        byte[] data = getRecord().getData();
        int numrecords = IntegerHelper.getInt(data[0], data[1]);
        for (int i = 0; i < numrecords; i++) {
            int pos = (i * 4) + 2;
            this.rgbColours[i] = new RGB(IntegerHelper.getInt(data[pos], (byte) 0), IntegerHelper.getInt(data[pos + 1], (byte) 0), IntegerHelper.getInt(data[pos + 2], (byte) 0));
        }
        this.initialized = true;
    }

    public void setColourRGB(Colour c, int r, int g, int b) {
        int pos = c.getValue() - 8;
        if (pos >= 0 && pos < 56) {
            if (!this.initialized) {
                initialize();
            }
            this.rgbColours[pos] = new RGB(setValueRange(r, 0, Light.MAIN_KEY_MAX), setValueRange(g, 0, Light.MAIN_KEY_MAX), setValueRange(b, 0, Light.MAIN_KEY_MAX));
            this.dirty = true;
        }
    }

    private int setValueRange(int val, int min, int max) {
        return Math.min(Math.max(val, min), max);
    }
}
