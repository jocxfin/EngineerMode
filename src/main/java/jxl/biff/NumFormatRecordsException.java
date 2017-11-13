package jxl.biff;

public class NumFormatRecordsException extends Exception {
    public NumFormatRecordsException() {
        super("Internal error:  max number of FORMAT records exceeded");
    }
}
