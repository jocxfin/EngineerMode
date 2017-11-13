package jxl.write.biff;

public class RowsExceededException extends JxlWriteException {
    public RowsExceededException() {
        super(maxRowsExceeded);
    }
}
