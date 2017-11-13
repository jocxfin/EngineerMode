package com.android.engineeringmode.manualtest.modeltest;

import android.content.Context;
import android.content.Intent;

import com.android.engineeringmode.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ModelTestParser {
    private Context mContext;
    private String mFileName;
    private List<Intent> mIntentList = new ArrayList();
    private List<String> mTitleList = new ArrayList();

    public ModelTestParser(Context context, String fileName) {
        this.mContext = context;
        this.mFileName = fileName;
    }

    public boolean parse() {
        InputStream inputStream;
        Throwable th;
        boolean result = false;
        try {
            InputStream inputStream2 = new FileInputStream("/sdcard/modeltest/" + this.mFileName);
            try {
                result = parse(inputStream2);
                inputStream2.close();
                inputStream = inputStream2;
            } catch (Exception e) {
                inputStream = inputStream2;
                try {
                    Log.d("DeviceLog", "isPassed=false");
                    if (!result) {
                        return result;
                    }
                    try {
                        return parse(this.mContext.getAssets().open(this.mFileName));
                    } catch (IOException e2) {
                        return false;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        } catch (Exception e3) {
            Log.d("DeviceLog", "isPassed=false");
            if (!result) {
                return result;
            }
            return parse(this.mContext.getAssets().open(this.mFileName));
        }
        if (!result) {
            return result;
        }
        return parse(this.mContext.getAssets().open(this.mFileName));
    }

    private boolean parse(InputStream inputStream) {
        try {
            Log.i("ModelTestParser", "begin to parse");
            extractInfo(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream));
            return true;
        } catch (Exception e) {
            Log.e("ModelTestParser", "Exception happen when prase the xml file");
            e.printStackTrace();
            return false;
        }
    }

    public void extractInfo(Document doc) {
        NodeList nList = doc.getElementsByTagName("ModelItem");
        if (this.mIntentList != null) {
            this.mIntentList.clear();
        }
        if (this.mTitleList != null) {
            this.mTitleList.clear();
        }
        for (int i = 0; i < nList.getLength(); i++) {
            Element node = (Element) nList.item(i);
            String attr = node.getAttribute("id");
            Log.i("ModelTestParser", "extractInfo(), node attr = " + attr);
            this.mTitleList.add(attr);
            String intntName = node.getElementsByTagName("intent").item(0).getFirstChild().getNodeValue();
            this.mIntentList.add(new Intent(intntName));
            Log.i("ModelTestParser", "extractInfo(), node intntName = " + intntName);
        }
        Log.i("ModelTestParser", "extractInfo(), end parse");
    }

    public List<Intent> getIntentList() {
        return this.mIntentList;
    }

    public List<String> getTitleList() {
        return this.mTitleList;
    }
}
