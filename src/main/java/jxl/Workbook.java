package jxl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import jxl.read.biff.BiffException;
import jxl.read.biff.PasswordException;
import jxl.read.biff.WorkbookParser;
import jxl.write.WritableWorkbook;
import jxl.write.biff.WritableWorkbookImpl;

public abstract class Workbook {
    public abstract int getNumberOfSheets();

    public abstract Sheet getSheet(int i) throws IndexOutOfBoundsException;

    public abstract boolean isProtected();

    protected abstract void parse() throws BiffException, PasswordException;

    protected Workbook() {
    }

    public static String getVersion() {
        return "2.6.12";
    }

    public static Workbook getWorkbook(File file) throws IOException, BiffException {
        return getWorkbook(file, new WorkbookSettings());
    }

    public static Workbook getWorkbook(File file, WorkbookSettings ws) throws IOException, BiffException {
        FileInputStream fis = new FileInputStream(file);
        try {
            jxl.read.biff.File dataFile = new jxl.read.biff.File(fis, ws);
            fis.close();
            Workbook workbook = new WorkbookParser(dataFile, ws);
            workbook.parse();
            return workbook;
        } catch (IOException e) {
            fis.close();
            throw e;
        } catch (BiffException e2) {
            fis.close();
            throw e2;
        }
    }

    public static WritableWorkbook createWorkbook(File file) throws IOException {
        return createWorkbook(file, new WorkbookSettings());
    }

    public static WritableWorkbook createWorkbook(File file, WorkbookSettings ws) throws IOException {
        return new WritableWorkbookImpl(new FileOutputStream(file), true, ws);
    }

    public static WritableWorkbook createWorkbook(File file, Workbook in) throws IOException {
        return createWorkbook(file, in, new WorkbookSettings());
    }

    public static WritableWorkbook createWorkbook(File file, Workbook in, WorkbookSettings ws) throws IOException {
        return new WritableWorkbookImpl(new FileOutputStream(file), in, true, ws);
    }
}
