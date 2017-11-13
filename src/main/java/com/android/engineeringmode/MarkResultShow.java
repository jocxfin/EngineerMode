package com.android.engineeringmode;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.util.ExternFunction;
import com.oem.util.Feature;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MarkResultShow extends Activity implements OnClickListener {
    private final String firstCalibrationFilePath = "/persist/rear_camera_dual_led_calibration";
    private boolean mConnected = false;
    private ExternFunction mExFunction;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                MarkResultShow.this.mConnected = true;
                MarkResultShow.this.updateMarks();
            }
        }
    };
    private ListView mMarks;
    private final String secondCalibrationFilePath = "/persist/second_rear_camera_dual_led_calibration";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903146);
        this.mMarks = (ListView) findViewById(2131493352);
        this.mExFunction = new ExternFunction(this);
        this.mExFunction.registerOnServiceConnected(this.mHandler, 1, null);
        setButtonClickListner();
    }

    protected void onDestroy() {
        if (this.mExFunction != null) {
            this.mExFunction.unregisterOnServiceConnected(this.mHandler);
            this.mExFunction.dispose();
        }
        super.onDestroy();
    }

    private void updateMarks() {
        byte[] buff = this.mExFunction.getProductLineTestFlag();
        if (buff != null) {
            Map<String, String> items;
            boolean contain;
            String testItem;
            Log.i("MarkResultShow", "" + buff.length);
            ArrayList<Map<String, String>> data = new ArrayList();
            int i = 0;
            while (i < buff.length && i < 79) {
                int summary_text_id = -1;
                switch (i) {
                    case 0:
                        summary_text_id = 2131297308;
                        break;
                    case Light.MAIN_KEY_LIGHT /*1*/:
                        summary_text_id = 2131297309;
                        break;
                    case Light.CHARGE_RED_LIGHT /*2*/:
                        summary_text_id = 2131297310;
                        break;
                    case Light.CHARGE_GREEN_LIGHT /*3*/:
                        summary_text_id = 2131297311;
                        break;
                    case 4:
                        summary_text_id = 2131297312;
                        break;
                    case 5:
                        summary_text_id = 2131297313;
                        break;
                    case Light.MAIN_KEY_NORMAL /*6*/:
                        summary_text_id = 2131297314;
                        break;
                    case 7:
                        summary_text_id = 2131297315;
                        break;
                    case 8:
                        summary_text_id = 2131297316;
                        break;
                    case 9:
                        summary_text_id = 2131297317;
                        break;
                    case 10:
                        summary_text_id = 2131297318;
                        break;
                    case 11:
                        summary_text_id = 2131297319;
                        break;
                    case 12:
                        summary_text_id = 2131297320;
                        break;
                    case 13:
                        summary_text_id = 2131297321;
                        break;
                    case 14:
                        summary_text_id = 2131297322;
                        break;
                    case 15:
                        summary_text_id = 2131297323;
                        break;
                    case 16:
                        summary_text_id = 2131297324;
                        break;
                    case 17:
                        summary_text_id = 2131297325;
                        break;
                    case 18:
                        summary_text_id = 2131297326;
                        break;
                    case 19:
                        summary_text_id = 2131297327;
                        break;
                    case 20:
                        summary_text_id = 2131297492;
                        break;
                    case 21:
                        summary_text_id = 2131297493;
                        break;
                    case 22:
                        summary_text_id = 2131297449;
                        break;
                    case 23:
                        if (Feature.isSdcardSupported(this)) {
                            summary_text_id = 2131297509;
                            break;
                        }
                        break;
                    case 24:
                        summary_text_id = 2131297416;
                        break;
                    case 26:
                        summary_text_id = 2131297505;
                        break;
                    case 27:
                        summary_text_id = 2131297659;
                        break;
                    case 28:
                        summary_text_id = 2131297506;
                        break;
                    case 29:
                        summary_text_id = 2131297508;
                        break;
                    case 30:
                        summary_text_id = 2131297362;
                        break;
                    case 31:
                        summary_text_id = 2131296903;
                        break;
                    case 32:
                        summary_text_id = 2131297336;
                        break;
                    case 33:
                        summary_text_id = 2131296694;
                        break;
                    case 34:
                        summary_text_id = 2131296748;
                        break;
                    case 35:
                        summary_text_id = 2131296772;
                        break;
                    case 36:
                        summary_text_id = 2131297514;
                        break;
                    case 37:
                        summary_text_id = 2131296968;
                        break;
                    case 38:
                        summary_text_id = 2131297212;
                        break;
                    case 39:
                        summary_text_id = 2131297272;
                        break;
                    case 40:
                        summary_text_id = 2131297515;
                        break;
                    case 41:
                        summary_text_id = 2131296415;
                        break;
                    case 42:
                        summary_text_id = 2131296611;
                        break;
                    case 43:
                        summary_text_id = 2131296776;
                        break;
                    case 44:
                        summary_text_id = 2131297198;
                        break;
                    case 45:
                        summary_text_id = 2131297512;
                        break;
                    case 46:
                        summary_text_id = 2131297029;
                        break;
                    case 47:
                        summary_text_id = 2131296819;
                        break;
                    case 48:
                        summary_text_id = 2131296971;
                        break;
                    case 49:
                        summary_text_id = 2131297236;
                        break;
                    case 50:
                        summary_text_id = 2131296955;
                        break;
                    case 51:
                        summary_text_id = 2131297003;
                        break;
                    case 52:
                        summary_text_id = 2131296982;
                        break;
                    case 53:
                        summary_text_id = 2131297104;
                        break;
                    case 54:
                        if (Feature.isNfcSupported(this)) {
                            summary_text_id = 2131297218;
                            break;
                        }
                        break;
                    case 56:
                        summary_text_id = 2131296692;
                        break;
                    case 57:
                        summary_text_id = 2131297209;
                        break;
                    case 58:
                        summary_text_id = 2131297504;
                        break;
                    case 59:
                        summary_text_id = 2131297503;
                        break;
                    case 60:
                        summary_text_id = 2131297478;
                        break;
                    case 62:
                        summary_text_id = 2131297494;
                        break;
                    case 63:
                        summary_text_id = 2131296747;
                        break;
                    case 64:
                        summary_text_id = 2131296715;
                        break;
                    case 67:
                        summary_text_id = 2131297559;
                        break;
                    case 68:
                        summary_text_id = 2131297578;
                        break;
                    case 70:
                        summary_text_id = 2131297507;
                        break;
                    case 71:
                        summary_text_id = 2131297510;
                        break;
                    case 72:
                        summary_text_id = 2131297511;
                        break;
                    case 73:
                        summary_text_id = 2131296952;
                        break;
                    case 76:
                        summary_text_id = 2131297589;
                        break;
                    case 77:
                        summary_text_id = 2131296342;
                        break;
                    case 78:
                        summary_text_id = 2131297650;
                        break;
                }
                if (!(summary_text_id == -1 || buff[i] == (byte) 0 || buff[i] == (byte) 2)) {
                    items = new HashMap();
                    items.put("TestItem", getResources().getString(summary_text_id));
                    if (i == 72) {
                        items.put("Result", "" + buff[i]);
                    } else if (buff[i] == (byte) 0) {
                        items.put("Result", getResources().getString(2131297513));
                    } else if (buff[i] == (byte) 1) {
                        items.put("Result", getResources().getString(2131296272));
                    } else if (buff[i] == (byte) 2) {
                        items.put("Result", getResources().getString(2131296273));
                    }
                    data.add(items);
                }
                i++;
            }
            if (isTheDualCalibrationFileExist("/persist/rear_camera_dual_led_calibration") && data != null) {
                if (data.size() > 0) {
                    contain = false;
                    i = 0;
                    while (i < data.size()) {
                        testItem = (String) ((Map) data.get(i)).get("TestItem");
                        if (testItem == null || !testItem.equals(getResources().getString(2131297553))) {
                            i++;
                        } else {
                            contain = true;
                            if (!contain) {
                                items = new HashMap();
                                items.put("TestItem", getResources().getString(2131297553));
                                items.put("Result", getResources().getString(2131296272));
                                data.add(items);
                            }
                        }
                    }
                    if (contain) {
                        items = new HashMap();
                        items.put("TestItem", getResources().getString(2131297553));
                        items.put("Result", getResources().getString(2131296272));
                        data.add(items);
                    }
                } else {
                    items = new HashMap();
                    items.put("TestItem", getResources().getString(2131297553));
                    items.put("Result", getResources().getString(2131296272));
                    data.add(items);
                }
            }
            if (isTheDualCalibrationFileExist("/persist/second_rear_camera_dual_led_calibration") && data != null) {
                if (data.size() > 0) {
                    contain = false;
                    i = 0;
                    while (i < data.size()) {
                        testItem = (String) ((Map) data.get(i)).get("TestItem");
                        if (testItem == null || !testItem.equals(getResources().getString(2131297554))) {
                            i++;
                        } else {
                            contain = true;
                            if (!contain) {
                                items = new HashMap();
                                items.put("TestItem", getResources().getString(2131297554));
                                items.put("Result", getResources().getString(2131296272));
                                data.add(items);
                            }
                        }
                    }
                    if (contain) {
                        items = new HashMap();
                        items.put("TestItem", getResources().getString(2131297554));
                        items.put("Result", getResources().getString(2131296272));
                        data.add(items);
                    }
                } else {
                    items = new HashMap();
                    items.put("TestItem", getResources().getString(2131297554));
                    items.put("Result", getResources().getString(2131296272));
                    data.add(items);
                }
            }
            this.mMarks.setAdapter(new SimpleAdapter(this, data, 17367044, new String[]{"TestItem", "Result"}, new int[]{16908308, 16908309}));
        }
    }

    private boolean isTheDualCalibrationFileExist(String path) {
        File file = new File(path);
        Log.v("MarkResultShow", "checkoutTheFile file= " + file);
        if (file == null) {
            return false;
        }
        Log.v("MarkResultShow", "checkoutTheFile + =  filestring = " + file.getAbsolutePath() + "exists =" + file.exists());
        return file.exists();
    }

    private void setButtonClickListner() {
        ((Button) findViewById(2131493351)).setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131493351:
                Log.d("MarkResultShow", "MarkResultShow clearButton clicked");
                clearAllMarks();
                updateMarks();
                return;
            default:
                return;
        }
    }

    private void clearAllMarks() {
        if (isTheDualCalibrationFileExist("/persist/rear_camera_dual_led_calibration")) {
            new File("/persist/rear_camera_dual_led_calibration").delete();
        }
        if (isTheDualCalibrationFileExist("/persist/second_rear_camera_dual_led_calibration")) {
            new File("/persist/second_rear_camera_dual_led_calibration").delete();
        }
        if (this.mConnected) {
            byte[] buff = this.mExFunction.getProductLineTestFlag();
            if (buff != null) {
                for (int i = 0; i <= 79; i++) {
                    buff[i] = (byte) 0;
                }
                this.mExFunction.setProductLineTestFlag(buff);
            } else {
                return;
            }
        }
        Toast.makeText(this, "Connecting service,please wait...", 3000).show();
    }
}
