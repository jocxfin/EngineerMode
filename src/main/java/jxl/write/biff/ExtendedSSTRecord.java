package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class ExtendedSSTRecord extends WritableRecordData {
    private int[] absoluteStreamPositions;
    private int currentStringIndex = 0;
    private int numberOfStrings;
    private int[] relativeStreamPositions;

    public ExtendedSSTRecord(int newNumberOfStrings) {
        super(Type.EXTSST);
        this.numberOfStrings = newNumberOfStrings;
        int numberOfBuckets = getNumberOfBuckets();
        this.absoluteStreamPositions = new int[numberOfBuckets];
        this.relativeStreamPositions = new int[numberOfBuckets];
        this.currentStringIndex = 0;
    }

    public int getNumberOfBuckets() {
        int numberOfStringsPerBucket = getNumberOfStringsPerBucket();
        if (numberOfStringsPerBucket == 0) {
            return 0;
        }
        return ((this.numberOfStrings + numberOfStringsPerBucket) - 1) / numberOfStringsPerBucket;
    }

    public int getNumberOfStringsPerBucket() {
        return ((this.numberOfStrings + 128) - 1) / 128;
    }

    public void addString(int absoluteStreamPosition, int relativeStreamPosition) {
        this.absoluteStreamPositions[this.currentStringIndex] = absoluteStreamPosition + relativeStreamPosition;
        this.relativeStreamPositions[this.currentStringIndex] = relativeStreamPosition;
        this.currentStringIndex++;
    }

    public byte[] getData() {
        int numberOfBuckets = getNumberOfBuckets();
        byte[] data = new byte[((numberOfBuckets * 8) + 2)];
        IntegerHelper.getTwoBytes(getNumberOfStringsPerBucket(), data, 0);
        for (int i = 0; i < numberOfBuckets; i++) {
            IntegerHelper.getFourBytes(this.absoluteStreamPositions[i], data, (i * 8) + 2);
            IntegerHelper.getTwoBytes(this.relativeStreamPositions[i], data, (i * 8) + 6);
        }
        return data;
    }
}
