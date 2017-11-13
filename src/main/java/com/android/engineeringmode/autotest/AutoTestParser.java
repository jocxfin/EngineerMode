package com.android.engineeringmode.autotest;

import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AutoTestParser extends BaseParser {
    public AutoTestParser(Context context) {
        super(context);
    }

    public void extractInfo(List<BaseItem> itemList, Document doc) {
        NodeList nList = doc.getElementsByTagName("AutoTestItem");
        itemList.clear();
        String productName = SystemProperties.get("ro.product.name", "oppo");
        for (int i = nList.getLength() - 1; i >= 0; i--) {
            Element node = (Element) nList.item(i);
            String attr = node.getAttribute("id");
            String intntName = node.getElementsByTagName("intent").item(0).getFirstChild().getNodeValue();
            if ((!"N5207".equals(productName) && !"N5200".equals(productName) && !"N5206".equals(productName)) || !intntName.equals("com.android.engineeringmode.autotest.FrontCameraTest")) {
                Intent intent = new Intent(intntName);
                intent.putExtra("key_if_can_manual_exited", true);
                intent.putExtra("key_is_autotest", true);
                intent.putExtra("key_is_autoaging", false);
                itemList.add(new BaseItem(intent, attr));
            }
        }
    }

    public String getDefaultParsePath() {
        return "autotestlist.xml";
    }
}
