package jxl.write.biff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jxl.Cell;
import jxl.CellType;
import jxl.Range;
import jxl.biff.SheetRangeImpl;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.write.Blank;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

class MergedCells {
    private static Logger logger = Logger.getLogger(MergedCells.class);
    private ArrayList ranges = new ArrayList();
    private WritableSheet sheet;

    public MergedCells(WritableSheet ws) {
        this.sheet = ws;
    }

    void add(Range r) {
        this.ranges.add(r);
    }

    Range[] getMergedCells() {
        Range[] cells = new Range[this.ranges.size()];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = (Range) this.ranges.get(i);
        }
        return cells;
    }

    private void checkIntersections() {
        ArrayList newcells = new ArrayList(this.ranges.size());
        Iterator mci = this.ranges.iterator();
        while (mci.hasNext()) {
            SheetRangeImpl r = (SheetRangeImpl) mci.next();
            Iterator i = newcells.iterator();
            boolean intersects = false;
            while (i.hasNext() && !intersects) {
                if (((SheetRangeImpl) i.next()).intersects(r)) {
                    logger.warn("Could not merge cells " + r + " as they clash with an existing set of merged cells.");
                    intersects = true;
                }
            }
            if (!intersects) {
                newcells.add(r);
            }
        }
        this.ranges = newcells;
    }

    private void checkRanges() {
        int i = 0;
        while (i < this.ranges.size()) {
            try {
                SheetRangeImpl range = (SheetRangeImpl) this.ranges.get(i);
                Cell tl = range.getTopLeft();
                Cell br = range.getBottomRight();
                boolean found = false;
                for (int c = tl.getColumn(); c <= br.getColumn(); c++) {
                    for (int r = tl.getRow(); r <= br.getRow(); r++) {
                        if (this.sheet.getCell(c, r).getType() != CellType.EMPTY) {
                            if (found) {
                                logger.warn("Range " + range + " contains more than one data cell.  " + "Setting the other cells to blank.");
                                this.sheet.addCell(new Blank(c, r));
                            } else {
                                found = true;
                            }
                        }
                    }
                }
                i++;
            } catch (WriteException e) {
                Assert.verify(false);
                return;
            }
        }
    }

    void write(File outputFile) throws IOException {
        if (this.ranges.size() != 0) {
            if (!((WritableSheetImpl) this.sheet).getWorkbookSettings().getMergedCellCheckingDisabled()) {
                checkIntersections();
                checkRanges();
            }
            if (this.ranges.size() >= 1020) {
                int numRecordsRequired = (this.ranges.size() / 1020) + 1;
                int pos = 0;
                for (int i = 0; i < numRecordsRequired; i++) {
                    int numranges = Math.min(1020, this.ranges.size() - pos);
                    ArrayList cells = new ArrayList(numranges);
                    for (int j = 0; j < numranges; j++) {
                        cells.add(this.ranges.get(pos + j));
                    }
                    outputFile.write(new MergedCellsRecord(cells));
                    pos += numranges;
                }
                return;
            }
            outputFile.write(new MergedCellsRecord(this.ranges));
        }
    }
}
