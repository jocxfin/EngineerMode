package jxl.write;

import jxl.biff.drawing.Drawing;
import jxl.biff.drawing.DrawingGroup;
import jxl.biff.drawing.DrawingGroupObject;

public class WritableImage extends Drawing {
    public static ImageAnchorProperties MOVE_AND_SIZE_WITH_CELLS = Drawing.MOVE_AND_SIZE_WITH_CELLS;
    public static ImageAnchorProperties MOVE_WITH_CELLS = Drawing.MOVE_WITH_CELLS;
    public static ImageAnchorProperties NO_MOVE_OR_SIZE_WITH_CELLS = Drawing.NO_MOVE_OR_SIZE_WITH_CELLS;

    public WritableImage(DrawingGroupObject d, DrawingGroup dg) {
        super(d, dg);
    }

    public byte[] getImageData() {
        return super.getImageData();
    }
}
