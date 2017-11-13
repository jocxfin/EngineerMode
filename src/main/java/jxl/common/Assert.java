package jxl.common;

public final class Assert {
    public static void verify(boolean condition) {
        if (!condition) {
            throw new AssertionFailed();
        }
    }

    public static void verify(boolean condition, String message) {
        if (!condition) {
            throw new AssertionFailed(message);
        }
    }
}
