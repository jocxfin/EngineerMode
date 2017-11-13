package jxl;

public interface Sheet {
    Cell getCell(int i, int i2);

    int getColumns();

    String getName();

    int getRows();

    SheetSettings getSettings();
}
