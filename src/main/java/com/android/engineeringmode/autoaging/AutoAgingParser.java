package com.android.engineeringmode.autoaging;

import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;

import com.android.engineeringmode.autotest.BaseItem;
import com.android.engineeringmode.autotest.BaseParser;
import com.oem.util.Feature;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AutoAgingParser extends BaseParser {
    public AutoAgingParser(Context context) {
        super(context);
        setItemDuration(30000);
    }

    public void extractInfo(List<BaseItem> itemList, Document doc) {
        NodeList nList = doc.getElementsByTagName("AutoTestItem");
        itemList.clear();
        String productName = SystemProperties.get("ro.product.name", "oppo");
        for (int i = 0; i < nList.getLength(); i++) {
            Element node = (Element) nList.item(i);
            String attr = node.getAttribute("id");
            String toggleOn = node.getAttribute("toggleOn");
            String toggleOff = node.getAttribute("toggleOff");
            String packageName = node.getAttribute("package");
            String intntName = node.getElementsByTagName("intent").item(0).getFirstChild().getNodeValue();
            if (!(("N5207".equals(productName) || "N5200".equals(productName) || "N5206".equals(productName)) && intntName.equals("com.android.engineeringmode.autoaging.FrontCameraTest")) && (Feature.isDualBackCameraSupported(this.mContext) || !intntName.equals("com.android.engineeringmode.autoaging.SecondBackCameraTest"))) {
                Intent intent = new Intent(intntName);
                if (!(packageName == null || packageName.equals(""))) {
                    intent.setPackage(packageName);
                }
                intent.putExtra("key_if_can_manual_exited", true);
                intent.putExtra("key_is_autotest", false);
                intent.putExtra("key_is_autoaging", true);
                BaseItem item = new BaseItem(intent, attr);
                item.setToggleText(toggleOn, toggleOff);
                itemList.add(item);
            }
        }
    }

    public String getDefaultParsePath() {
        return "autoaginglist.xml";
    }
}
