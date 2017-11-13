package com.android.engineeringmode.autoaging;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.engineeringmode.autotest.BaseItem;
import com.android.engineeringmode.autotest.BaseParser;
import com.android.engineeringmode.autotest.ParserFactory;

import java.util.Calendar;

public class AutoAgingSettingsActivity extends Activity {
    private ListView mListView;
    private BaseParser mParser;
    private PromptDialog mPromptDialog;
    private OnClickListener mSelectClickListener = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case 2131492914:
                    AutoAgingSettingsActivity.this.selectAll();
                    return;
                case 2131492915:
                    AutoAgingSettingsActivity.this.selectNone();
                    return;
                default:
                    return;
            }
        }
    };
    private String mSelectedList = "All of the items has selected";
    private TimeInputCallbak mTimeInputCallbak = new TimeInputCallbak();

    private class AutoAgingSettingsAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private OnClickListener mOnClickListener = new OnClickListener() {
            public void onClick(View v) {
                boolean z = false;
                Object tag = v.getTag();
                if (tag != null && (tag instanceof Integer)) {
                    BaseItem item = (BaseItem) AutoAgingSettingsActivity.this.mListView.getAdapter().getItem(((Integer) tag).intValue());
                    if (item != null) {
                        switch (v.getId()) {
                            case 2131492917:
                                if (!item.isEnabled()) {
                                    z = true;
                                }
                                item.setEnabled(z);
                                AutoAgingSettingsActivity autoAgingSettingsActivity;
                                if (item.isEnabled()) {
                                    autoAgingSettingsActivity = AutoAgingSettingsActivity.this;
                                    autoAgingSettingsActivity.mSelectedList = autoAgingSettingsActivity.mSelectedList + "+" + item.getTitle() + "/";
                                } else {
                                    autoAgingSettingsActivity = AutoAgingSettingsActivity.this;
                                    autoAgingSettingsActivity.mSelectedList = autoAgingSettingsActivity.mSelectedList + "-" + item.getTitle() + "/";
                                }
                                AutoAgingSettingsActivity.this.mListView.invalidateViews();
                                break;
                            case 2131492919:
                                if (!item.isToggled()) {
                                    z = true;
                                }
                                item.setToggled(z);
                                break;
                        }
                    }
                }
            }
        };
        private BaseParser mParser;
        private OnClickListener mTimeClickListener = new OnClickListener() {
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && (tag instanceof Integer)) {
                    BaseItem item = (BaseItem) AutoAgingSettingsActivity.this.mListView.getAdapter().getItem(((Integer) tag).intValue());
                    if (item != null) {
                        AutoAgingSettingsActivity.this.mTimeInputCallbak.setTarget(item);
                        AutoAgingSettingsActivity.this.mPromptDialog.show();
                    }
                }
            }
        };

        public AutoAgingSettingsAdapter(Context context, BaseParser parser) {
            this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
            this.mParser = parser;
        }

        public int getCount() {
            return this.mParser.getItemCount();
        }

        public Object getItem(int position) {
            return this.mParser.getItemAt(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView != null ? convertView : null;
            if (view == null) {
                view = this.mInflater.inflate(2130903053, null);
            }
            BaseItem item = (BaseItem) getItem(position);
            if (item == null) {
                return view;
            }
            String title = item.getTitle();
            boolean enabled = item.isEnabled();
            View panelView = view.findViewById(2131492918);
            TextView textView = (TextView) view.findViewById(2131492916);
            CheckBox checkBox = (CheckBox) view.findViewById(2131492917);
            ToggleButton toggleButton = (ToggleButton) view.findViewById(2131492919);
            TextView timeView = (TextView) view.findViewById(2131492920);
            checkBox.setTag(Integer.valueOf(position));
            toggleButton.setTag(Integer.valueOf(position));
            timeView.setTag(Integer.valueOf(position));
            checkBox.setOnClickListener(this.mOnClickListener);
            toggleButton.setOnClickListener(this.mOnClickListener);
            timeView.setOnClickListener(this.mTimeClickListener);
            textView.setText(title);
            checkBox.setChecked(enabled);
            if (item.isToggleEnabled()) {
                toggleButton.setVisibility(0);
                toggleButton.setTextOn(item.getToggledText(true));
                toggleButton.setTextOff(item.getToggledText(false));
            } else {
                toggleButton.setVisibility(4);
            }
            if (enabled) {
                panelView.setVisibility(0);
                if (item.isToggleEnabled()) {
                    toggleButton.setChecked(item.isToggled());
                }
                long duration = item.getDuration();
                if (duration >= 0) {
                    duration /= 1000;
                }
                timeView.setText(String.valueOf(duration));
            } else {
                panelView.setVisibility(8);
            }
            return view;
        }
    }

    private interface PromptCallback {
        String getText();

        void onPrompt(String str);
    }

    private class PromptDialog {
        private PromptCallback mCallback;
        private DialogInterface.OnClickListener mClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        private AlertDialog mDialog;
        private OnDismissListener mDismissListener = new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                PromptDialog.this.prompt();
            }
        };
        private EditText mPromptView;

        public PromptDialog(Context context) {
            this.mDialog = new Builder(context).create();
            View customView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(2130903187, null);
            this.mPromptView = (EditText) customView.findViewById(2131493184);
            this.mDialog.setView(customView);
            this.mDialog.setCancelable(true);
            this.mDialog.setOnDismissListener(this.mDismissListener);
            this.mDialog.setButton(-1, context.getString(2131296260), this.mClickListener);
        }

        public void show() {
            if (!this.mDialog.isShowing()) {
                if (this.mCallback != null) {
                    this.mPromptView.setText(this.mCallback.getText());
                }
                this.mPromptView.selectAll();
                this.mDialog.show();
            }
        }

        public void setCallback(PromptCallback callback) {
            this.mCallback = callback;
        }

        public void setTitle(int titleId) {
            this.mDialog.setTitle(titleId);
        }

        private void prompt() {
            String promptText = this.mPromptView.getText().toString();
            if (this.mCallback != null) {
                this.mCallback.onPrompt(promptText);
            }
        }
    }

    private class TimeInputCallbak implements PromptCallback {
        private BaseItem mItem;

        private TimeInputCallbak() {
        }

        public void setTarget(BaseItem item) {
            this.mItem = item;
        }

        public void onPrompt(String text) {
            if (this.mItem != null) {
                long time = -1;
                if (!(text == null || text.equals(""))) {
                    try {
                        time = Long.parseLong(text);
                    } catch (NumberFormatException e) {
                        time = -1;
                    }
                }
                BaseItem baseItem = this.mItem;
                if (time >= 0) {
                    time *= 1000;
                }
                baseItem.setDuration(time);
                AutoAgingSettingsActivity.this.mListView.invalidateViews();
            }
        }

        public String getText() {
            if (this.mItem == null) {
                return null;
            }
            long duration = this.mItem.getDuration();
            if (duration >= 0) {
                duration /= 1000;
            }
            return String.valueOf(duration);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(2131296925);
        setContentView(2130903052);
        this.mParser = ParserFactory.getInstance().getAutoagingParser(getApplicationContext());
        this.mPromptDialog = new PromptDialog(this);
        this.mPromptDialog.setCallback(this.mTimeInputCallbak);
        this.mPromptDialog.setTitle(2131296927);
        this.mListView = (ListView) findViewById(2131492913);
        this.mListView.setAdapter(new AutoAgingSettingsAdapter(this, this.mParser));
        findViewById(2131492914).setOnClickListener(this.mSelectClickListener);
        findViewById(2131492915).setOnClickListener(this.mSelectClickListener);
    }

    private void selectAll() {
        for (int i = 0; i < this.mListView.getAdapter().getCount(); i++) {
            BaseItem item = (BaseItem) this.mListView.getAdapter().getItem(i);
            if (item != null) {
                item.setEnabled(true);
            }
        }
        this.mSelectedList = "All of the items has selected";
        this.mListView.invalidateViews();
    }

    private void selectNone() {
        for (int i = 0; i < this.mListView.getAdapter().getCount(); i++) {
            BaseItem item = (BaseItem) this.mListView.getAdapter().getItem(i);
            if (item != null) {
                item.setEnabled(false);
            }
        }
        this.mSelectedList = "";
        this.mListView.invalidateViews();
    }

    protected void onStop() {
        Log.d("AutoAgingSettingsActivity", "onStop");
        Calendar now = Calendar.getInstance();
        String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--AutoAgingSettingsActivity--" + this.mSelectedList + "\n";
        super.onStop();
    }
}
