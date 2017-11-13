package com.android.engineeringmode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.XmlResourceParser;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

public class EngineerModeActionReceiver extends BroadcastReceiver {
    private int action_list_xml;
    private String mAction;
    private Map<Integer, String> mActionMap;
    private String mOPPOAction;
    private Map<Integer, String> mOrderMap;
    private String region;

    public EngineerModeActionReceiver() {
        this.mAction = "com.oem.engineermode.EngineerModeMain";
        this.mOPPOAction = "com.oppo.engineermode.EngineerModeMain";
        this.mOrderMap = new HashMap();
        this.mActionMap = new HashMap();
        this.action_list_xml = 2130968591;
        this.region = "";
        this.region = SystemProperties.get("persist.sys.oem.region", "CN");
    }

    public void onReceive(Context context, Intent intent) {
        Log.d("wangwei", "onReceive intent.getAction() = " + intent.getAction());
        if (intent.getAction() != null) {
            Intent intentAction;
            if (intent.getAction().equals("android.provider.Telephony.SECRET_CODE")) {
                Object host = intent.getData() != null ? intent.getData().getHost() : null;
                if ("3439".equals(host)) {
                    SystemProperties.set("oem.cust.flag", "1");
                    Toast.makeText(context, 2131297667, 3000).show();
                } else if ("3392".equals(host)) {
                    SystemProperties.set("oem.cust.flag", "0");
                    Toast.makeText(context, 2131297668, 3000).show();
                } else {
                    intentAction = new Intent();
                    intentAction.addFlags(268435456);
                    if ("818".equals(host)) {
                        intentAction.setAction("com.android.engineeringmode.NetworkSearch_New");
                        intentAction.putExtra("Step_Num", "Third");
                    } else if ("838".equals(host)) {
                        intentAction.setAction("com.android.engineeringmode.NetworkSearch_New");
                        intentAction.putExtra("Step_Num", "Fourth");
                    } else if ("7668".equals(host)) {
                        intentAction.setAction("com.android.engineeringmode.manualtest.CheckRootStatusActivity");
                    }
                    context.startActivity(intentAction);
                }
            } else if (intent.getAction().equals(this.mAction) || intent.getAction().equals(this.mOPPOAction)) {
                int index;
                String order = intent.getStringExtra("order");
                String action = "null";
                readXMLFile(this.mOrderMap, this.mActionMap, context);
                for (index = 0; index < this.mOrderMap.size(); index++) {
                    if (order.equals(this.mOrderMap.get(Integer.valueOf(index))) && action.equals("null")) {
                        action = (String) this.mActionMap.get(Integer.valueOf(index));
                    }
                }
                if (!this.region.equals("CN")) {
                    if (action != null && order.equals("*#8888#")) {
                        String fileName = "";
                        fileName = deleteFile(new File("/data/reserve/"));
                        if (fileName.equals("")) {
                            Toast.makeText(context, "No file need to delete", 2000).show();
                        } else {
                            Toast.makeText(context, "ok! Delete: " + fileName, 2000).show();
                        }
                    }
                    if (action != null && (order.equals("*#9889#") || order.equals("*#9886#"))) {
                        String key = "screensaver_enabled";
                        if (order.equals("*#9889#")) {
                            Secure.putInt(context.getContentResolver(), key, 1);
                            Toast.makeText(context, "Enable screensaver", 2000).show();
                        } else {
                            Secure.putInt(context.getContentResolver(), key, 0);
                            Toast.makeText(context, "Disable screensaver", 2000).show();
                        }
                        context.sendBroadcast(new Intent(action));
                        return;
                    } else if (action != null && (order.equals("*#8668#") || order.equals("*#8669#"))) {
                        context.sendBroadcast(new Intent(action));
                        return;
                    }
                }
                SharedPreferences sp = context.getSharedPreferences("switch_activated", 0);
                Editor e = sp.edit();
                if (order.equals("*#3954391#")) {
                    e.putBoolean("switch_activated", true);
                    e.commit();
                    Toast.makeText(context, "Switch activated", 2000).show();
                } else if (order.equals("*#391#") && !sp.getBoolean("switch_activated", true)) {
                } else {
                    if (action != null && order.equals("*#8011#")) {
                        SystemProperties.set("persist.sys.allcommode", "true");
                        Global.putInt(context.getContentResolver(), "adb_enabled", 1);
                        SystemProperties.set("persist.sys.adb.engineermode", "0");
                        SystemClock.sleep(100);
                        SystemProperties.set("persist.sys.usb.config", "diag,adb");
                        SystemProperties.set("sys.usb.config", "diag,adb");
                        Toast.makeText(context, "config success!", 3000).show();
                    } else if (action != null && order.equals("*#8000#")) {
                        context.sendBroadcast(new Intent("com.oem.oemlogkit.getLog"));
                        Toast.makeText(context, "start log and dump success!", 3000).show();
                    } else if (action != null && order.equals("*#818#")) {
                        intentAction = new Intent(action);
                        intentAction.addFlags(268435456);
                        intentAction.putExtra("Step_Num", "First");
                        context.startActivity(intentAction);
                    } else if (action == null || !order.equals("*#838#")) {
                        if (action != "null") {
                            intentAction = new Intent(action);
                            intentAction.putExtra("order", order);
                            intentAction.addFlags(268435456);
                            context.startActivity(intentAction);
                        }
                        if (!this.region.equals("CN") && action == "null") {
                            Map<Integer, String> orderMap = new HashMap();
                            Map<Integer, String> languageMap = new HashMap();
                            String lauguage = "null";
                            readLanXMLFile(orderMap, languageMap, context);
                            for (index = 0; index < orderMap.size(); index++) {
                                if (order.equals(orderMap.get(Integer.valueOf(index))) && lauguage.equals("null")) {
                                    lauguage = (String) languageMap.get(Integer.valueOf(index));
                                }
                            }
                            Log.d("EngineerModeActionReceiver", "lauguage=" + lauguage);
                            if (lauguage != "null") {
                                Intent intentLanSwitch = new Intent("com.android.engineeringmode.LanguageSwitchActivity");
                                intentLanSwitch.putExtra("language_str", lauguage);
                                intentLanSwitch.addFlags(268435456);
                                context.startActivity(intentLanSwitch);
                            }
                        }
                    } else {
                        intentAction = new Intent(action);
                        intentAction.addFlags(268435456);
                        intentAction.putExtra("Step_Num", "Second");
                        context.startActivity(intentAction);
                    }
                }
            }
        }
    }

    public void readXMLFile(Map<Integer, String> OrderMap, Map<Integer, String> ActionMap, Context context) {
        XmlResourceParser xrp = context.getResources().getXml(this.action_list_xml);
        while (xrp.getEventType() != 1) {
            try {
                if (xrp.getEventType() == 2) {
                    if (xrp.getName().equals("action")) {
                        OrderMap.put(Integer.valueOf(Integer.parseInt(xrp.getAttributeValue(0))), xrp.getAttributeValue(1));
                        ActionMap.put(Integer.valueOf(Integer.parseInt(xrp.getAttributeValue(0))), xrp.getAttributeValue(2));
                    }
                } else if (xrp.getEventType() != 3 && xrp.getEventType() == 4) {
                }
                xrp.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                return;
            } catch (IOException e2) {
                e2.printStackTrace();
                return;
            }
        }
    }

    public String deleteFile(File file) {
        String filePath = "";
        if (file != null) {
            if (file.isFile()) {
                file.delete();
                return filePath;
            }
            String[] childFilePaths = file.list();
            if (childFilePaths != null) {
                for (String childFilePath : childFilePaths) {
                    filePath = filePath + childFilePath + "  ";
                    deleteFile(new File(file.getAbsolutePath() + "/" + childFilePath));
                }
            }
        }
        return filePath;
    }

    public void readLanXMLFile(Map<Integer, String> OrderMap, Map<Integer, String> LanguageMap, Context context) {
        XmlResourceParser xrp = context.getResources().getXml(2130968598);
        while (xrp.getEventType() != 1) {
            try {
                if (xrp.getEventType() == 2) {
                    if (xrp.getName().equals("language")) {
                        OrderMap.put(Integer.valueOf(Integer.parseInt(xrp.getAttributeValue(0))), xrp.getAttributeValue(1));
                        LanguageMap.put(Integer.valueOf(Integer.parseInt(xrp.getAttributeValue(0))), xrp.getAttributeValue(2));
                    }
                } else if (xrp.getEventType() != 3 && xrp.getEventType() == 4) {
                }
                xrp.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                return;
            } catch (IOException e2) {
                e2.printStackTrace();
                return;
            }
        }
    }
}
