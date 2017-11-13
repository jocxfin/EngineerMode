package jxl.common;

public class AssertionFailed extends RuntimeException {
    public AssertionFailed() {
        printStackTrace();
    }

    public AssertionFailed(String s) {
        super(s);
    }
}
