package com.android.engineeringmode.wifitest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.security.KeyStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;

class WifiDialog extends AlertDialog implements OnClickListener, TextWatcher, OnItemSelectedListener {
    final boolean edit;
    private final AccessPoint mAccessPoint;
    private TextView mEapAnonymous;
    private Spinner mEapCaCert;
    private TextView mEapIdentity;
    private Spinner mEapMethod;
    private Spinner mEapUserCert;
    private final DialogInterface.OnClickListener mListener;
    private TextView mPassword;
    private Spinner mPhase2;
    private int mSecurity;
    private TextView mSsid;
    private View mView;

    static boolean requireKeyStore(WifiConfiguration config) {
        for (String value : new String[]{config.enterpriseConfig.getCaCertificateAlias(), config.enterpriseConfig.getClientCertificateAlias(), "privatekey"}) {
            if (value != null && value.startsWith("keystore://")) {
                return true;
            }
        }
        return false;
    }

    WifiDialog(Context context, DialogInterface.OnClickListener listener, AccessPoint accessPoint, boolean edit) {
        int i;
        super(context);
        this.edit = edit;
        this.mListener = listener;
        this.mAccessPoint = accessPoint;
        if (accessPoint == null) {
            i = 0;
        } else {
            i = accessPoint.security;
        }
        this.mSecurity = i;
    }

    WifiConfiguration getConfig() {
        if (this.mAccessPoint != null && this.mAccessPoint.networkId != -1 && !this.edit) {
            return null;
        }
        WifiConfiguration config = new WifiConfiguration();
        if (this.mAccessPoint == null) {
            config.SSID = AccessPoint.convertToQuotedString(this.mSsid.getText().toString());
            config.hiddenSSID = true;
        } else if (this.mAccessPoint.networkId == -1) {
            config.SSID = AccessPoint.convertToQuotedString(this.mAccessPoint.ssid);
        } else {
            config.networkId = this.mAccessPoint.networkId;
        }
        String password;
        switch (this.mSecurity) {
            case 0:
                config.allowedKeyManagement.set(0);
                return config;
            case Light.MAIN_KEY_LIGHT /*1*/:
                config.allowedKeyManagement.set(0);
                config.allowedAuthAlgorithms.set(0);
                config.allowedAuthAlgorithms.set(1);
                if (this.mPassword.length() != 0) {
                    int length = this.mPassword.length();
                    password = this.mPassword.getText().toString();
                    if ((length == 10 || length == 26 || length == 58) && password.matches("[0-9A-Fa-f]*")) {
                        config.wepKeys[0] = password;
                    } else {
                        config.wepKeys[0] = '\"' + password + '\"';
                    }
                }
                return config;
            case Light.CHARGE_RED_LIGHT /*2*/:
                config.allowedKeyManagement.set(1);
                if (this.mPassword.length() != 0) {
                    password = this.mPassword.getText().toString();
                    if (password.matches("[0-9A-Fa-f]{64}")) {
                        config.preSharedKey = password;
                    } else {
                        config.preSharedKey = '\"' + password + '\"';
                    }
                }
                return config;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                String str;
                config.allowedKeyManagement.set(2);
                config.allowedKeyManagement.set(3);
                config.enterpriseConfig.setEapMethod((int) this.mEapMethod.getSelectedItemId());
                config.enterpriseConfig.setPhase2Method((int) this.mPhase2.getSelectedItemId());
                WifiEnterpriseConfig wifiEnterpriseConfig = config.enterpriseConfig;
                if (this.mEapCaCert.getSelectedItemPosition() == 0) {
                    str = "";
                } else {
                    str = "keystore://CACERT_" + ((String) this.mEapCaCert.getSelectedItem());
                }
                wifiEnterpriseConfig.setCaCertificateAlias(str);
                wifiEnterpriseConfig = config.enterpriseConfig;
                if (this.mEapUserCert.getSelectedItemPosition() == 0) {
                    str = "";
                } else {
                    str = "keystore://USRCERT_" + ((String) this.mEapUserCert.getSelectedItem());
                }
                wifiEnterpriseConfig.setClientCertificateAlias(str);
                wifiEnterpriseConfig = config.enterpriseConfig;
                if (this.mEapIdentity.length() == 0) {
                    str = "";
                } else {
                    str = this.mEapIdentity.getText().toString();
                }
                wifiEnterpriseConfig.setIdentity(str);
                wifiEnterpriseConfig = config.enterpriseConfig;
                if (this.mEapAnonymous.length() == 0) {
                    str = "";
                } else {
                    str = this.mEapAnonymous.getText().toString();
                }
                wifiEnterpriseConfig.setAnonymousIdentity(str);
                if (this.mPassword.length() != 0) {
                    config.enterpriseConfig.setPassword(this.mPassword.getText().toString());
                }
                return config;
            default:
                return null;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        this.mView = getLayoutInflater().inflate(2130903227, null);
        setView(this.mView);
        Context context = getContext();
        Resources resources = context.getResources();
        if (this.mAccessPoint == null) {
            setTitle(2131296462);
            this.mView.findViewById(2131493596).setVisibility(0);
            this.mSsid = (TextView) this.mView.findViewById(2131493597);
            this.mSsid.addTextChangedListener(this);
            ((Spinner) this.mView.findViewById(2131493598)).setOnItemSelectedListener(this);
            setButton(-1, context.getString(2131296492), this.mListener);
        } else {
            setTitle(this.mAccessPoint.ssid);
            ViewGroup group = (ViewGroup) this.mView.findViewById(2131493540);
            DetailedState state = this.mAccessPoint.getState();
            if (state != null) {
                addRow(group, 2131296472, Summary.get(getContext(), state));
            }
            addRow(group, 2131296470, resources.getStringArray(2131099675)[this.mAccessPoint.security]);
            int level = this.mAccessPoint.getLevel();
            if (level != -1) {
                addRow(group, 2131296471, resources.getStringArray(2131099677)[level]);
            }
            WifiInfo info = this.mAccessPoint.getInfo();
            if (info != null) {
                addRow(group, 2131296473, info.getLinkSpeed() + "Mbps");
                int address = info.getIpAddress();
                if (address != 0) {
                    addRow(group, 2131296474, Formatter.formatIpAddress(address));
                }
            }
            if (this.mAccessPoint.networkId == -1 || this.edit) {
                showSecurityFields();
            }
            if (this.edit) {
                setButton(-1, context.getString(2131296492), this.mListener);
            } else {
                if (state == null && level != -1) {
                    setButton(-1, context.getString(2131296490), this.mListener);
                }
                if (this.mAccessPoint.networkId != -1) {
                    setButton(-3, context.getString(2131296491), this.mListener);
                }
            }
        }
        setButton(-2, context.getString(2131296493), this.mListener);
        super.onCreate(savedInstanceState);
        if (getButton(-1) != null) {
            validate();
        }
    }

    private void addRow(ViewGroup group, int name, String value) {
        View row = getLayoutInflater().inflate(2130903228, group, false);
        ((TextView) row.findViewById(2131493610)).setText(name);
        ((TextView) row.findViewById(2131493611)).setText(value);
        group.addView(row);
    }

    private void validate() {
        if ((this.mSsid == null || this.mSsid.length() != 0) && (!(this.mAccessPoint == null || this.mAccessPoint.networkId == -1) || (!(this.mSecurity == 1 && this.mPassword.length() == 0) && (this.mSecurity != 2 || this.mPassword.length() >= 8)))) {
            getButton(-1).setEnabled(true);
        } else {
            getButton(-1).setEnabled(false);
        }
    }

    public void onClick(View view) {
        int i;
        TextView textView = this.mPassword;
        if (((CheckBox) view).isChecked()) {
            i = 144;
        } else {
            i = 128;
        }
        textView.setInputType(i | 1);
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void afterTextChanged(Editable editable) {
        validate();
    }

    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        this.mSecurity = position;
        showSecurityFields();
        validate();
    }

    public void onNothingSelected(AdapterView parent) {
    }

    private void showSecurityFields() {
        if (this.mSecurity == 0) {
            this.mView.findViewById(2131493599).setVisibility(8);
            return;
        }
        this.mView.findViewById(2131493599).setVisibility(0);
        if (this.mPassword == null) {
            this.mPassword = (TextView) this.mView.findViewById(2131493480);
            this.mPassword.addTextChangedListener(this);
            ((CheckBox) this.mView.findViewById(2131493601)).setOnClickListener(this);
            if (!(this.mAccessPoint == null || this.mAccessPoint.networkId == -1)) {
                this.mPassword.setHint(2131296483);
            }
        }
        if (this.mSecurity != 3) {
            this.mView.findViewById(2131493603).setVisibility(8);
            return;
        }
        this.mView.findViewById(2131493603).setVisibility(0);
        if (this.mEapMethod == null) {
            this.mEapMethod = (Spinner) this.mView.findViewById(2131493604);
            this.mPhase2 = (Spinner) this.mView.findViewById(2131493605);
            this.mEapCaCert = (Spinner) this.mView.findViewById(2131493606);
            this.mEapUserCert = (Spinner) this.mView.findViewById(2131493607);
            this.mEapIdentity = (TextView) this.mView.findViewById(2131493608);
            this.mEapAnonymous = (TextView) this.mView.findViewById(2131493609);
            loadCertificates(this.mEapCaCert, "CACERT_");
            loadCertificates(this.mEapUserCert, "USRPKEY_");
            if (!(this.mAccessPoint == null || this.mAccessPoint.networkId == -1)) {
                WifiConfiguration config = this.mAccessPoint.getConfig();
                setSelection(this.mEapMethod, config.enterpriseConfig.getEapMethod());
                setSelection(this.mPhase2, config.enterpriseConfig.getPhase2Method());
                setCertificate(this.mEapCaCert, "CACERT_", config.enterpriseConfig.getCaCertificateAlias());
                setCertificate(this.mEapUserCert, "USRPKEY_", "privatekey");
                this.mEapIdentity.setText(config.enterpriseConfig.getIdentity());
                this.mEapAnonymous.setText(config.enterpriseConfig.getAnonymousIdentity());
            }
        }
    }

    private void loadCertificates(Spinner spinner, String prefix) {
        String[] certs = KeyStore.getInstance().list(prefix);
        Context context = getContext();
        String unspecified = context.getString(2131296484);
        if (certs == null || certs.length == 0) {
            certs = new String[]{unspecified};
        } else {
            String[] array = new String[(certs.length + 1)];
            array[0] = unspecified;
            System.arraycopy(certs, 0, array, 1, certs.length);
            certs = array;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(context, 17367048, certs);
        adapter.setDropDownViewResource(17367049);
        spinner.setAdapter(adapter);
    }

    private void setCertificate(Spinner spinner, String prefix, String cert) {
        prefix = "keystore://" + prefix;
        if (cert != null && cert.startsWith(prefix)) {
            setSelection(spinner, cert.substring(prefix.length()));
        }
    }

    private void setSelection(Spinner spinner, int index) {
        if (index != -1) {
            spinner.setSelection(index);
        }
    }

    private void setSelection(Spinner spinner, String value) {
        if (value != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter) spinner.getAdapter();
            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                if (value.equals(adapter.getItem(i))) {
                    spinner.setSelection(i);
                    return;
                }
            }
        }
    }
}
