package jxl.biff;

public interface WorkbookMethods {
    String getName(int i) throws NameRangeException;

    int getNameIndex(String str);
}
