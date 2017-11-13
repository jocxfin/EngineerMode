package com.android.engineeringmode.autotest;

import android.content.Context;

import com.android.engineeringmode.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public abstract class BaseParser {
    protected Context mContext = null;
    private final List<BaseItem> mItems = new ArrayList();

    public abstract void extractInfo(List<BaseItem> list, Document document);

    public abstract String getDefaultParsePath();

    public BaseParser(Context context) {
        this.mContext = context;
        parse();
    }

    public final List<BaseItem> getItems() {
        return this.mItems;
    }

    public final int getItemCount() {
        List<BaseItem> itemList = getItems();
        if (itemList == null) {
            return 0;
        }
        return itemList.size();
    }

    public final BaseItem getItemAt(int index) {
        List<BaseItem> itemList = getItems();
        if (itemList == null) {
            return null;
        }
        BaseItem baseItem = null;
        if (index >= 0 && index < itemList.size()) {
            baseItem = (BaseItem) itemList.get(index);
        }
        return baseItem;
    }

    public final void setItemDuration(long duration) {
        List<BaseItem> itemList = getItems();
        if (itemList != null) {
            for (BaseItem item : itemList) {
                if (item != null) {
                    item.setDuration(duration);
                }
            }
        }
    }

    public final long getItemDuration(int index) {
        BaseItem item = getItemAt(index);
        return item != null ? item.getDuration() : -1;
    }

    public final boolean isItemToggled(int index) {
        BaseItem item = getItemAt(index);
        if (item == null || !item.isToggleEnabled()) {
            return false;
        }
        return item.isToggled();
    }

    public final boolean parse() {
        String path = getDefaultParsePath();
        if (path == null) {
            return false;
        }
        return parse(path);
    }

    public final boolean parse(String fileName) {
        boolean result = false;
        InputStream inputStream = null;
        try {
            inputStream = this.mContext.getAssets().open(fileName);
            result = parse(inputStream);
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e2) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        }
        return result;
    }

    public final boolean parse(InputStream inputStream) {
        try {
            Log.i("BaseParser", "begin to parse");
            extractInfo(this.mItems, DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream));
            return true;
        } catch (Exception e) {
            Log.e("BaseParser", "Exception happen when prase the xml file");
            e.printStackTrace();
            return false;
        }
    }
}
