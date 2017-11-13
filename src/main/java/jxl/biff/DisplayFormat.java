package jxl.biff;

public interface DisplayFormat {
    int getFormatIndex();

    void initialize(int i);

    boolean isBuiltIn();

    boolean isInitialized();
}
