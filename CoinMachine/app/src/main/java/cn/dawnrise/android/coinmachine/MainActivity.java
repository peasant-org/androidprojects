package cn.dawnrise.android.coinmachine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public final static String URLS = "urls";
    public final static String PHONES = "phones";

    private static final String logTag = "Coin";

    Thread at;
    public final static Set<String> mobiletypes = new HashSet<>();
    public final static Set<String> defaultUrls = new ArraySet<>();
    final static String[] androidVersion = {"Linux; Android 5.0", "Linux; Android 5.1", "Linux; Android 6.0", "Linux; Android 6.1", "Linux; Android 7.0", "Linux; Android 7.1.1", "Linux; Android 8.0"};
    public static SharedPreferences config;

    static {
        List<String> pl = Arrays.asList("NOAIND01", "D5288CT", "CT06", "Sun005", "20170605Q", "KINGSUN-F9", "KINGSUN-EF36", "MP1709", "MP1701", "1713-A01", "1707-A01", "1801-A01", "koobeeS12", "koobeeF1", "ASUS_X018DC", "ASUS_X00KD", "HL2001", "TCL Y660", "GIONEE F6", "GIONEE S11", "GIONEE GN5006", "GIONEE F109N", "GIONEE F109", "GIONEE S10C", "GIONEE F100SD", "GIONEE M7", "GIONEE GN5007", "GIONEE S10B", "GIONEE S10", "HLTE212T", "HLTE500T", "HLTE300T", "Hisense F23", "HLTE200T", "Hisense E77", "MEC7S", "MDE6S", "MDE6", "SM-W2018", "COL-AL10", "CLT-AL01", "CLT-AL00", "NEO-AL00", "EML-AL00", "HWI-AL00", "BLA-AL00", "ALP-AL00", "Y71A", "Y85A", "X21 UD A", "X21A", "X20 Plus UD", "Y75A", "Y79A", "X20A", "X20 Plus A", "PADM00", "PACM00", "PAAM00", "OPPOA83", "OPPOA79K", "R11s Plus", "R11s");
        for (String e : pl
                ) {
            mobiletypes.add(e);
        }

    }

    private TextView mTextMessage;

    static {
        String[] mUrls = {"https://www.baidu.com", "http://www.163.com"};
        for (String e : mUrls
                ) {
            defaultUrls.add(e);
        }
    }

    private String mUrl = "https://www.baidu.com";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MainActivity.this.setTitle(getString(R.string.app_name));
                    return true;
                default:
                    MainActivity.this.setTitle(getString(R.string.app_name) + "-" + item.getTitle());
                    return true;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        config = getSharedPreferences("config", MODE_PRIVATE);



        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        final WebView webView = findViewById(R.id.webView);
        //final WebView webView = new WebView(this);

        webView.loadUrl(mUrl);
        Log.i(logTag, "访问：" + mUrl);

        Switch sw = findViewById(R.id.airplaneswitch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//设置飞行模式开关监听器
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AirplaneNmobileData.setAirplaneModeOn(b);

            }
        });

        Button button = findViewById(R.id.start_button);
        button.setOnClickListener(new View.OnClickListener() {//开始线程
            @Override
            public void onClick(View view) {
                Set<String> urls = config.getStringSet(URLS, defaultUrls);
                Set<String> phones = config.getStringSet(PHONES, mobiletypes);
                if (at == null) {
                    at = new AccessThread(webView, 1000, MainActivity.this, phones.toArray(new String[0]), urls.toArray(new String[0]));

                }
                at.start();

            }
        });


        button = findViewById(R.id.stop_button);

        button.setOnClickListener(new View.OnClickListener() {//关闭线程
            @Override
            public void onClick(View view) {
                if (at != null && !at.isInterrupted()) {
                    Log.i(logTag, "请等待，正在停止挖矿主线程……");
                    at.interrupt();
                }
            }
        });

//        button = findViewById(R.id.weixin);
//
//        final Intent i = new Intent(this, WebViewActivity.class);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(i);
//            }
//        });

        button = findViewById(R.id.drawer);

        final Intent i1 = new Intent(this, MainDrawerActivity.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(i1);
            }
        });

        button = findViewById(R.id.btn_links);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dv = getLayoutInflater().inflate(R.layout.links_add, null);
                final TextInputEditText te = dv.findViewById(R.id.urls_editor);
                Set<String> us = config.getStringSet(URLS, defaultUrls);

                StringBuilder sb = new StringBuilder();
                for (String e : us
                        ) {
                    sb.append(e);
                    sb.append("\n");
                }
                if (sb.length() > 0)
                    sb.deleteCharAt(sb.length() - 1);
                te.getText().append(sb.toString());

                Dialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("设置URLs")
                        .setView(dv)
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //保存URLs
                                String[] ss = te.getText().toString().split("\n");
                                Set<String> set = new ArraySet<>();
                                for (String e : ss) {
                                    set.add(e);
                                }
                                config.edit().putStringSet(URLS, set).commit();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                dialog.show();

            }
        });


        button = findViewById(R.id.btn_phones);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dv = getLayoutInflater().inflate(R.layout.phones_add, null);
                final TextInputEditText te = dv.findViewById(R.id.phones_editor);

                Set<String> us = config.getStringSet(PHONES, mobiletypes);
                StringBuilder sb = new StringBuilder();
                for (String e : us
                        ) {
                    sb.append(e);
                    sb.append(",");
                }
                if (sb.length() > 0)
                    sb.deleteCharAt(sb.length() - 1);
                te.getText().append(sb.toString());

                Dialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("设置phones")
                        .setView(dv)
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //保存phones
                                String[] ss = te.getText().toString().split("[,|，]");
                                Set<String> set = new ArraySet<>();
                                for (String e : ss) {
                                    set.add(e);
                                }
                                config.edit().putStringSet(PHONES, set).commit();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                dialog.show();

            }
        });


    }


    //更改IP访问链接的进程
    public static class AccessThread extends Thread {
        //        String url;
        WebView webView;
        int times;
        Random rd, rd2, rd3;
        Handler h;
        Context mContext;
        String[] mUrls;
        String[] mPhones;
        int c1 = 10;
        int c2 = 5;
        ThreadPoolProxy threadPoolProxy;
        boolean stopflag = false;

        /*        public AccessThread(String url, final WebView webView, int times, Context context) {
                    this.url = url;
                    this.webView = webView;
                    this.times = times;
                    this.rd = new Random(11);
                    this.h = new android.os.Handler();
                    this.mContext = context;
                }*/
        public AccessThread(WebView webView, int times, Context context, String[] phones, String[] urls) {

            this.webView = webView;
            this.times = times;
            this.mContext = context;
            this.mPhones = phones;
            this.mUrls = urls;

            this.h = new android.os.Handler();
            this.rd = new Random(56558998);
            this.rd2 = new Random(26461576);
            this.rd3 = new Random(16198456);
            this.threadPoolProxy = new ThreadPoolProxy(5, 20);
        }

        public void makeStop() {
            stopflag = true;
        }

        @Override
        public void run() {
            super.run();
            stopflag = false;
            for (int i = 0; i < times; i++) {
                if (stopflag) {
                    break;//跳出循环
                }

                AirplaneNmobileData.setAirplaneModeOn(true);
                Log.i(logTag, "开启飞行模式");

                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "开启飞行模式", Toast.LENGTH_LONG).show();
                    }
                });

                try {
                    sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                AirplaneNmobileData.setAirplaneModeOn(false);
                Log.i(logTag, "关闭飞行模式");
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "关闭飞行模式", Toast.LENGTH_LONG).show();
                    }
                });

                try {
                    sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final String ipMsg = "IP:" + AirplaneNmobileData.getIPAddress(mContext);

                Log.i(logTag, ipMsg);
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, ipMsg, Toast.LENGTH_LONG).show();
                    }
                });


                //启用子线程，同一IP依次访问多个URL
                threadPoolProxy.execute(new Runnable() {
                    @Override
                    public void run() {
                        String av = androidVersion[rd.nextInt(androidVersion.length - 1)];
                        String mt = mPhones[rd.nextInt(mPhones.length - 1)];


                        //苹果：User-Agent:Mozilla/5.0 (iPhone; CPU iPhone OS 10_0_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Mobile/14A551 MicroMessenger/6.7.1 NetType/WIFI Language/zh_CN

                        //安卓
                        // User-Agent:Mozilla/5.0 (Linux; Android 7.1.1; MEIZU E3 Build/NGI77B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044113 Mobile Safari/537.36 MicroMessenger/6.6.7.1321(0x26060736) NetType/WIFI Language/zh_CN

                        String p = av + "; " + mt + "B" + rd.nextInt(200);

                        String mmv = "MicroMessenger/6.6.7";

                        String android_ua = "User-Agent:Mozilla/5.0 (" + p + ") AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044113 Mobile Safari/537.36 " + mmv + " NetType/4G Language/zh_CN";
                        String ios_ua = "User-Agent:Mozilla/5.0 (iPhone; CPU iPhone OS 10_0_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Mobile/14A551 MicroMessenger/6.7.1 NetType/4 Language/zh_CN";

                        String f_ua = null;


                        //随机模仿安卓、苹果手机
                        if (rd.nextInt(10) > 8) {
                            f_ua = ios_ua;

                        } else {
                            f_ua = android_ua;
                        }
                        final String f_ua_msg = "WebView设置" + f_ua;
                        Log.i(logTag, f_ua_msg);
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, f_ua_msg, Toast.LENGTH_LONG).show();
                            }
                        });


                        int t = rd2.nextInt(2) + 1;

                        for (int j = 0; j < t; j++) {
                            if (stopflag)
                                break;
                            final String url = mUrls[rd.nextInt(mUrls.length - 1)];
                            final String finalF_ua = f_ua;
                            try {
                                h.post(new Runnable() {//在UI线程调用WebView
                                    @Override
                                    public void run() {
                                        webView.clearHistory();
                                        webView.clearCache(false);
                                        webView.getSettings().setUserAgentString(finalF_ua);
                                        webView.loadUrl(url);
                                        Log.i(logTag, "访问：" + url);
                                        h.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(mContext, "访问：" + url, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                                sleep(rd3.nextInt(300000) + 60 * 000);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                });


                try {
                    switch (rd2.nextInt(30)) {

                        case 0:
                            sleep(rd.nextInt(600) * 1000 + 1200 * 1000);//中间隔20至30分钟

                        case 1:
                            sleep(rd.nextInt(3600) * 1000 + 1800 * 1000);//大间隔30分至1.5小时

                        default:
                            sleep(rd.nextInt(600) * 1000 + 120 * 1000);//小间隔

                    }

                } catch (InterruptedException e) {
                    makeStop();//被中断了，则终止线程
                    e.printStackTrace();
                    break;
                }

            }
            stopflag = true;
            Log.i(logTag, "挖矿主线程停止!!!");
            h.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "挖矿主线程停止!!!", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    static class TaskProgress extends AsyncTask<String, String, String> {
        String url;
        WebView webView;
        int times;

        public TaskProgress() {

        }

        public TaskProgress(String url, WebView webView, int times) {
            this.url = url;
            this.webView = webView;
            this.times = times;
        }

        @Override
        protected String doInBackground(String... strings) {

            return null;

        }
    }

}
