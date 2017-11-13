package com.android.engineeringmode.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.engineeringmode.manualtest.RotationUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class SaveToExcel {
    private static final SimpleDateFormat SHEET_DATE_FORMAT = new SimpleDateFormat("yyyyMMddhhmmss");
    private static final SimpleDateFormat TIME_LABEL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ms");
    private WritableSheet mCurrSheet = null;
    private WritableWorkbook mCurrWorkbook = null;
    private MyHandler mHandler = null;
    private RotationUtils mRotationUtils = new RotationUtils();
    private int mRowNum = 0;

    private class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        public final void handleMessage(Message msg) {
            SaveToExcel.this.logD("SaveSnrToExcel.MyHandler  message = " + msg.what);
            switch (msg.what) {
                case 1001:
                    SaveToExcel.this.onStart();
                    sendEmptyMessage(1002);
                    return;
                case 1002:
                    SaveToExcel.this.writeDataToFile(SaveToExcel.this.mRotationUtils.hall_test());
                    sendEmptyMessageDelayed(1002, 50);
                    return;
                case 1003:
                    SaveToExcel.this.onStop();
                    return;
                default:
                    return;
            }
        }
    }

    public SaveToExcel() {
        HandlerThread thread = new HandlerThread("MyHandlerThread");
        thread.start();
        this.mHandler = new MyHandler(thread.getLooper());
    }

    public void onStart() {
        this.mCurrWorkbook = openExcelFile("Hall.xls");
        this.mCurrSheet = createWorkSheet(this.mCurrWorkbook);
        this.mRowNum = 0;
        this.mRotationUtils.open_hall();
    }

    public void onStop() {
        this.mHandler.removeMessages(1002);
        this.mRotationUtils.close_hall();
        closeExcelFile();
    }

    private Workbook getExistExcelFile(String fileName) {
        Workbook result = null;
        try {
            result = Workbook.getWorkbook(new File("/data/system", fileName));
        } catch (Exception e) {
            logD("catch an exception when get the exist excel file.");
        }
        return result;
    }

    private WritableWorkbook openExcelFile(String fileName) {
        Workbook existFile = getExistExcelFile(fileName);
        logD("get exist file " + existFile);
        if (existFile != null) {
            return Workbook.createWorkbook(new File("/data/system", fileName), existFile);
        }
        try {
            return Workbook.createWorkbook(new File("/data/system", fileName));
        } catch (IOException e) {
            logD("Catch an exception when open excel file!");
            return null;
        }
    }

    private WritableSheet createWorkSheet(WritableWorkbook workBook) {
        return workBook.createSheet(SHEET_DATE_FORMAT.format(new Date(System.currentTimeMillis())), workBook.getSheets().length);
    }

    private void writeDataToFile(int[] calibration) {
        try {
            if (this.mRowNum == 0) {
                this.mCurrSheet.addCell(new Label(0, 0, "calibration"));
                this.mRowNum++;
            }
            this.mCurrSheet.addCell(new Number(0, this.mRowNum, (double) calibration[2]));
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e2) {
            e2.printStackTrace();
        }
        this.mRowNum++;
    }

    private void closeExcelFile() {
        try {
            this.mCurrWorkbook.write();
            this.mCurrWorkbook.close();
        } catch (Exception e) {
            logD("Catch an exception when close excel file!");
        }
    }

    private void logD(String logMsg) {
        Log.d("skc", logMsg);
    }

    public void stopSaving() {
        this.mHandler.sendEmptyMessage(1003);
    }

    public void startSaving() {
        this.mHandler.sendEmptyMessage(1001);
    }
}
