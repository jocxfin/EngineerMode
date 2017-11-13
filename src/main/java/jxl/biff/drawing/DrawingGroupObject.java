package jxl.biff.drawing;

import java.io.IOException;

import jxl.write.biff.File;

public interface DrawingGroupObject {
    String getImageFilePath();

    MsoDrawingRecord getMsoDrawingRecord();

    int getObjectId();

    Origin getOrigin();

    int getShapeId();

    EscherContainer getSpContainer();

    boolean isFirst();

    boolean isFormObject();

    void setDrawingGroup(DrawingGroup drawingGroup);

    void setObjectId(int i, int i2, int i3);

    void writeAdditionalRecords(File file) throws IOException;

    void writeTailRecords(File file) throws IOException;
}
