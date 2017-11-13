package com.android.engineeringmode;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MosConfigFactory extends Activity {
    private int extra_command;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 6) {
                MosConfigFactory.this.finish();
            } else if (msg.what == 7) {
                SystemClock.sleep(100);
                MosConfigFactory.this.resultTextView.setText("NONE_CONFIG_FOUND");
                Log.e("MosConfigFactory", "no config inside!");
            }
        }
    };
    private Messenger mMessenger;
    private Handler mResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle != null) {
                if (bundle.containsKey("RESULT_IMPORT")) {
                    String content = bundle.getString("RESULT_IMPORT");
                    if (false) {
                        int max;
                        int length = content.length();
                        if (length % 128 == 0) {
                            max = length / 128;
                        } else {
                            max = (length / 128) + 1;
                        }
                        for (int i = 0; i < max; i++) {
                            int count = (i + 1) * 128;
                            String str = "";
                            try {
                                str = new String(content.substring(i * 128, count <= length ? count : length).getBytes("UTF-8"), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                str = "";
                            }
                        }
                        MosConfigFactory.this.mHandler.sendEmptyMessageDelayed(6, 100);
                    } else {
                        Log.i("MosConfigFactory", "import fail");
                        MosConfigFactory.this.resultTextView.setText("FAIL");
                    }
                } else if (bundle.containsKey("RESULT_EXPORT")) {
                    Log.i("MosConfigFactory", "export result : " + bundle.getBoolean("RESULT_EXPORT"));
                    MosConfigFactory.this.finish();
                }
            }
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            MosConfigFactory.this.mMessenger = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            MosConfigFactory.this.mMessenger = new Messenger(service);
            MosConfigFactory.this.doitNow();
        }
    };
    private TextView resultTextView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initService();
        this.resultTextView = new TextView(this);
        setContentView(this.resultTextView);
        this.resultTextView.setTextSize(40.0f);
        this.resultTextView.setText("TESTING");
        this.resultTextView.setGravity(17);
        this.resultTextView.setTextColor(-65536);
    }

    private boolean shellRun(String command) {
        IOException e;
        InterruptedException e2;
        Throwable th;
        Process process = null;
        BufferedReader bufferedReader = null;
        boolean result = false;
        try {
            byte[] b = new byte[1024];
            process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (true) {
                try {
                    String line = bufferedReader2.readLine();
                    if (line == null) {
                        break;
                    }
                    Log.e("MosConfigFactory", "line:" + line);
                } catch (IOException e3) {
                    e = e3;
                    bufferedReader = bufferedReader2;
                } catch (InterruptedException e4) {
                    e2 = e4;
                    bufferedReader = bufferedReader2;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = bufferedReader2;
                }
            }
            process.waitFor();
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (IOException e5) {
                }
            }
            if (process != null) {
                process.destroy();
                result = true;
            }
            return result;
        } catch (IOException e6) {
            e = e6;
            try {
                e.printStackTrace();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e7) {
                    }
                }
                if (process == null) {
                    return false;
                }
                process.destroy();
                return true;
            } catch (Throwable th3) {
                th = th3;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e8) {
                    }
                }
                if (process != null) {
                    process.destroy();
                }
                throw th;
            }
        } catch (InterruptedException e9) {
            e2 = e9;
            e2.printStackTrace();
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e10) {
                }
            }
            if (process == null) {
                return false;
            }
            process.destroy();
            return true;
        }
    }

    private void doitNow() {
        String action = getIntent().getStringExtra("order");
        if (action != null && action.equals("import")) {
            this.extra_command = 6;
            sendCommandToService(this.extra_command);
        } else if (action != null && action.equals("export")) {
            shellRun("rm /persist/mos_config.ini");
            shellRun("touch /persist/mos_config.ini");
            shellRun("chmod 664 /persist/mos_config.ini");
            this.extra_command = 7;
            this.mHandler.sendEmptyMessage(7);
        }
    }

    private void initService() {
        Log.d("MosConfigFactory", "MosConfigFactory --- initService");
        bindService(new Intent("com.oppo.sdcard.command"), this.mServiceConnection, 1);
    }

    protected void onDestroy() {
        unbindService(this.mServiceConnection);
        super.onDestroy();
    }

    private void sendCommandToService(int command) {
        Message message = Message.obtain(null, command);
        message.replyTo = new Messenger(this.mResultHandler);
        message.arg1 = command;
        message.setData(new Bundle());
        try {
            this.mMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
