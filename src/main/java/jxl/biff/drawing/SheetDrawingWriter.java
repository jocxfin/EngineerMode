package jxl.biff.drawing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jxl.WorkbookSettings;
import jxl.biff.IntegerHelper;
import jxl.common.Logger;
import jxl.write.biff.File;

public class SheetDrawingWriter {
    private static Logger logger = Logger.getLogger(SheetDrawingWriter.class);
    private Chart[] charts = new Chart[0];
    private ArrayList drawings;
    private boolean drawingsModified;

    public SheetDrawingWriter(WorkbookSettings ws) {
    }

    public void setDrawings(ArrayList dr, boolean mod) {
        this.drawings = dr;
        this.drawingsModified = mod;
    }

    public void write(File outputFile) throws IOException {
        if (this.drawings.size() != 0 || this.charts.length != 0) {
            boolean modified = this.drawingsModified;
            int numImages = this.drawings.size();
            Iterator i = this.drawings.iterator();
            while (i.hasNext() && !modified) {
                if (((DrawingGroupObject) i.next()).getOrigin() != Origin.READ) {
                    modified = true;
                }
            }
            if (!(numImages <= 0 || modified || ((DrawingGroupObject) this.drawings.get(0)).isFirst())) {
                modified = true;
            }
            if (numImages == 0 && this.charts.length == 1 && this.charts[0].getMsoDrawingRecord() == null) {
                modified = false;
            }
            if (modified) {
                int i2;
                byte[] data;
                Chart chart;
                Object[] spContainerData = new Object[(this.charts.length + numImages)];
                int length = 0;
                EscherContainer firstSpContainer = null;
                for (i2 = 0; i2 < numImages; i2++) {
                    EscherContainer spc = ((DrawingGroupObject) this.drawings.get(i2)).getSpContainer();
                    if (spc != null) {
                        data = spc.getData();
                        spContainerData[i2] = data;
                        if (i2 != 0) {
                            length += data.length;
                        } else {
                            firstSpContainer = spc;
                        }
                    }
                }
                for (i2 = 0; i2 < this.charts.length; i2++) {
                    EscherContainer spContainer = this.charts[i2].getSpContainer();
                    data = spContainer.setHeaderData(spContainer.getBytes());
                    spContainerData[i2 + numImages] = data;
                    if (i2 == 0 && numImages == 0) {
                        firstSpContainer = spContainer;
                    } else {
                        length += data.length;
                    }
                }
                DgContainer dgContainer = new DgContainer();
                dgContainer.add(new Dg(this.charts.length + numImages));
                EscherRecord spgrContainer = new SpgrContainer();
                EscherRecord spContainer2 = new SpContainer();
                spContainer2.add(new Spgr());
                spContainer2.add(new Sp(ShapeType.MIN, 1024, 5));
                spgrContainer.add(spContainer2);
                spgrContainer.add(firstSpContainer);
                dgContainer.add(spgrContainer);
                byte[] firstMsoData = dgContainer.getData();
                IntegerHelper.getFourBytes(IntegerHelper.getInt(firstMsoData[4], firstMsoData[5], firstMsoData[6], firstMsoData[7]) + length, firstMsoData, 4);
                IntegerHelper.getFourBytes(IntegerHelper.getInt(firstMsoData[28], firstMsoData[29], firstMsoData[30], firstMsoData[31]) + length, firstMsoData, 28);
                if (numImages > 0 && ((DrawingGroupObject) this.drawings.get(0)).isFormObject()) {
                    Object msodata2 = new byte[(firstMsoData.length - 8)];
                    System.arraycopy(firstMsoData, 0, msodata2, 0, msodata2.length);
                    Object firstMsoData2 = msodata2;
                }
                outputFile.write(new MsoDrawingRecord(firstMsoData));
                if (numImages <= 0) {
                    chart = this.charts[0];
                    outputFile.write(chart.getObjRecord());
                    outputFile.write(chart);
                } else {
                    ((DrawingGroupObject) this.drawings.get(0)).writeAdditionalRecords(outputFile);
                }
                i2 = 1;
                while (i2 < spContainerData.length) {
                    byte[] bytes = (byte[]) spContainerData[i2];
                    if (i2 < numImages && ((DrawingGroupObject) this.drawings.get(i2)).isFormObject()) {
                        byte[] bytes2 = new byte[(bytes.length - 8)];
                        System.arraycopy(bytes, 0, bytes2, 0, bytes2.length);
                        bytes = bytes2;
                    }
                    outputFile.write(new MsoDrawingRecord(bytes));
                    if (i2 >= numImages) {
                        chart = this.charts[i2 - numImages];
                        outputFile.write(chart.getObjRecord());
                        outputFile.write(chart);
                    } else {
                        ((DrawingGroupObject) this.drawings.get(i2)).writeAdditionalRecords(outputFile);
                    }
                    i2++;
                }
                i = this.drawings.iterator();
                while (i.hasNext()) {
                    ((DrawingGroupObject) i.next()).writeTailRecords(outputFile);
                }
                return;
            }
            writeUnmodified(outputFile);
        }
    }

    private void writeUnmodified(File outputFile) throws IOException {
        if (this.charts.length != 0 || this.drawings.size() != 0) {
            Iterator i;
            DrawingGroupObject d;
            if (this.charts.length == 0 && this.drawings.size() != 0) {
                i = this.drawings.iterator();
                while (i.hasNext()) {
                    d = (DrawingGroupObject) i.next();
                    outputFile.write(d.getMsoDrawingRecord());
                    d.writeAdditionalRecords(outputFile);
                }
                i = this.drawings.iterator();
                while (i.hasNext()) {
                    ((DrawingGroupObject) i.next()).writeTailRecords(outputFile);
                }
            } else if (this.drawings.size() == 0 && this.charts.length != 0) {
                for (Chart curChart : this.charts) {
                    if (curChart.getMsoDrawingRecord() != null) {
                        outputFile.write(curChart.getMsoDrawingRecord());
                    }
                    if (curChart.getObjRecord() != null) {
                        outputFile.write(curChart.getObjRecord());
                    }
                    outputFile.write(curChart);
                }
            } else {
                byte[] cbytes;
                int numDrawings = this.drawings.size();
                int length = 0;
                EscherContainer[] spContainers = new EscherContainer[(this.charts.length + numDrawings)];
                boolean[] isFormObject = new boolean[(this.charts.length + numDrawings)];
                for (i = 0; i < numDrawings; i++) {
                    d = (DrawingGroupObject) this.drawings.get(i);
                    spContainers[i] = d.getSpContainer();
                    if (i > 0) {
                        length += spContainers[i].getLength();
                    }
                    if (d.isFormObject()) {
                        isFormObject[i] = true;
                    }
                }
                for (i = 0; i < this.charts.length; i++) {
                    spContainers[i + numDrawings] = this.charts[i].getSpContainer();
                    length += spContainers[i + numDrawings].getLength();
                }
                DgContainer dgContainer = new DgContainer();
                dgContainer.add(new Dg(this.charts.length + numDrawings));
                EscherRecord spgrContainer = new SpgrContainer();
                EscherRecord spContainer = new SpContainer();
                spContainer.add(new Spgr());
                spContainer.add(new Sp(ShapeType.MIN, 1024, 5));
                spgrContainer.add(spContainer);
                spgrContainer.add(spContainers[0]);
                dgContainer.add(spgrContainer);
                byte[] firstMsoData = dgContainer.getData();
                IntegerHelper.getFourBytes(IntegerHelper.getInt(firstMsoData[4], firstMsoData[5], firstMsoData[6], firstMsoData[7]) + length, firstMsoData, 4);
                IntegerHelper.getFourBytes(IntegerHelper.getInt(firstMsoData[28], firstMsoData[29], firstMsoData[30], firstMsoData[31]) + length, firstMsoData, 28);
                if (isFormObject[0]) {
                    cbytes = new byte[(firstMsoData.length - 8)];
                    System.arraycopy(firstMsoData, 0, cbytes, 0, cbytes.length);
                    firstMsoData = cbytes;
                }
                outputFile.write(new MsoDrawingRecord(firstMsoData));
                ((DrawingGroupObject) this.drawings.get(0)).writeAdditionalRecords(outputFile);
                for (i = 1; i < spContainers.length; i++) {
                    byte[] bytes2 = spContainers[i].setHeaderData(spContainers[i].getBytes());
                    if (isFormObject[i]) {
                        cbytes = new byte[(bytes2.length - 8)];
                        System.arraycopy(bytes2, 0, cbytes, 0, cbytes.length);
                        bytes2 = cbytes;
                    }
                    outputFile.write(new MsoDrawingRecord(bytes2));
                    if (i >= numDrawings) {
                        Chart chart = this.charts[i - numDrawings];
                        outputFile.write(chart.getObjRecord());
                        outputFile.write(chart);
                    } else {
                        ((DrawingGroupObject) this.drawings.get(i)).writeAdditionalRecords(outputFile);
                    }
                }
                i = this.drawings.iterator();
                while (i.hasNext()) {
                    ((DrawingGroupObject) i.next()).writeTailRecords(outputFile);
                }
            }
        }
    }

    public void setCharts(Chart[] ch) {
        this.charts = ch;
    }

    public Chart[] getCharts() {
        return this.charts;
    }
}
