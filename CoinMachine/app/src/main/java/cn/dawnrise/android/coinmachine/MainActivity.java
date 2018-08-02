package cn.dawnrise.android.coinmachine;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String logTag = "Coin";
    static String[] mobiletype = {"NOAIND01", "D5288CT", "CT06", "Sun005", "20170605Q", "KINGSUN-F9", "KINGSUN-EF36", "MP1709", "MP1701", "1713-A01", "1707-A01", "1801-A01", "koobeeS12", "koobeeF1", "ASUS_X018DC", "ASUS_X00KD", "HL2001", "TCL Y660", "GIONEE F6", "GIONEE S11", "GIONEE GN5006", "GIONEE F109N", "GIONEE F109", "GIONEE S10C", "GIONEE F100SD", "GIONEE M7", "GIONEE GN5007", "GIONEE S10B", "GIONEE S10", "HLTE212T", "HLTE500T", "HLTE300T", "Hisense F23", "HLTE200T", "Hisense E77", "MEC7S", "MDE6S", "MDE6", "SM-W2018", "COL-AL10", "CLT-AL01", "CLT-AL00", "NEO-AL00", "EML-AL00", "HWI-AL00", "BLA-AL00", "ALP-AL00", "Y71A", "Y85A", "X21 UD A", "X21A", "X20 Plus UD", "Y75A", "Y79A", "X20A", "X20 Plus A", "PADM00", "PACM00", "PAAM00", "OPPOA83", "OPPOA79K", "R11s Plus", "R11s"};
    static String[] androidVersion = {"Linux; Android 5.0", "Linux; Android 5.1", "Linux; Android 6.0", "Linux; Android 6.1", "Linux; Android 7.0", "Linux; Android 7.1.1", "Linux; Android 8.0"};
    Thread at;
    private TextView mTextMessage;
    private String mUrl = "http://1750528_3881663.tbzun.cn./fDDw9yS7/7dW6X/Wtt69Tn/GBNyG.htm?GXTd2=9BdvGy&HqK9tJ8JT=y2y7YEWvMi&5DMJ=NRxqHwH347&dETqLftHa=p5kyx7Dw";
    private String[] mUrls = {
            "http://1750528_3877969.0vswi.cn./NJFd9Ef7T/fSbdW3dF4/mkG6A7Nqp/x79yeX/Gk92S.htm?rM8epFiR=Y7tYWYpp&j4NPi=gLep&8LdJ3yL=F8qkHHT&eLDmtdKK=FPSL&73xDSx=62kkj&from=singlemessage",
            "http://1750528_3874854.78inst.cn./3pskF/5n74/24TmytbJK.jsp?qySkiErAj4=tgJjHqps&QjTk45g4rY=qqYB&wV6Hi6=vdhy&from=singlemessage",
            "http://1750528_3875167.gxxie.com.cn./SGyhBv/9yJGSj4E7/wqPvp.htm?qW3bt=XHev&4yLehN3=r5mx4s&9bFRvea=qW3a6KmLb&from=singlemessage",
            "http://1750528_3873685.6wkxt.cn./vVKS?3W7RS9ain=tswKG&from=singlemessage",
            "http://1750528_3881663.tbzun.cn./fDDw9yS7/7dW6X/Wtt69Tn/GBNyG.htm?GXTd2=9BdvGy&HqK9tJ8JT=y2y7YEWvMi&5DMJ=NRxqHwH347&dETqLftHa=p5kyx7Dw",
    };
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
                MobileData.setAirplaneModeOn(b);

            }
        });

        Button button = findViewById(R.id.start_button);
        button.setOnClickListener(new View.OnClickListener() {//开始线程
            @Override
            public void onClick(View view) {
                if (at == null) {
                    at = new AccessThread(webView, 1000, MainActivity.this, mUrls);
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

        button = findViewById(R.id.weixin);

        final Intent i = new Intent(this, WebViewActivity.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(i);
            }
        });

        button = findViewById(R.id.drawer);

        final Intent i1 = new Intent(this, MainDrawerActivity.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(i1);
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
        public AccessThread(WebView webView, int times, Context context, String... urls) {
            this.mUrls = urls;
            this.webView = webView;
            this.times = times;
            this.rd = new Random(56558998);
            this.h = new android.os.Handler();
            this.mContext = context;
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

                MobileData.setAirplaneModeOn(true);
                Log.i(logTag, "开启飞行模式");
                try {
                    sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                MobileData.setAirplaneModeOn(false);
                Log.i(logTag, "关闭飞行模式");

                try {
                    sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i(logTag, "ip:" + MobileData.getIPAddress(mContext));

                //在UI线程调用WebView
                threadPoolProxy.execute(new Runnable() {
                    @Override
                    public void run() {
                        String av = androidVersion[rd.nextInt(androidVersion.length - 1)];
                        String mt = mobiletype[rd.nextInt(mobiletype.length) - 1];


                        //苹果：User-Agent:Mozilla/5.0 (iPhone; CPU iPhone OS 10_0_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Mobile/14A551 MicroMessenger/6.7.1 NetType/WIFI Language/zh_CN

                        //安卓
                        // User-Agent:Mozilla/5.0 (Linux; Android 7.1.1; MEIZU E3 Build/NGI77B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044113 Mobile Safari/537.36 MicroMessenger/6.6.7.1321(0x26060736) NetType/WIFI Language/zh_CN

                        String p = av + "; " + mt + "B" + rd.nextInt(200);

                        String mmv = "MicroMessenger/6.6.7";

                        String android_ua = "User-Agent:Mozilla/5.0 (" + p + ") AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044113 Mobile Safari/537.36 " + mmv + " NetType/4G Language/zh_CN";
                        String ios_ua = "User-Agent:Mozilla/5.0 (iPhone; CPU iPhone OS 10_0_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Mobile/14A551 MicroMessenger/6.7.1 NetType/4 Language/zh_CN";

                        String f_ua = null;
                        if (rd.nextInt(10) > 8) {
                            f_ua = ios_ua;

                        } else {
                            f_ua = android_ua;


                        }
                        Log.i(logTag, "WebView设置" + f_ua);


                        int t = rd2.nextInt(5);

                        for (int j = 0; j < t; j++) {
                            if (stopflag)
                                break;
                            final String url = mUrls[rd.nextInt(mUrls.length - 1)];
                            final String finalF_ua = f_ua;
                            try {
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        webView.clearHistory();
                                        webView.clearCache(false);
                                        webView.getSettings().setUserAgentString(finalF_ua);
                                        webView.loadUrl(url);
                                    }
                                });
                                sleep(rd3.nextInt(300000) + 20 * 000);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Log.i(logTag, "访问：" + url);
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
                            sleep(rd.nextInt(600) * 1000 + 60 * 1000);//小间隔

                    }

                } catch (InterruptedException e) {
                    makeStop();//被中断了，则终止线程
                    e.printStackTrace();
                    break;
                }

            }
            stopflag = true;
            Log.i(logTag, "挖矿主线程停止");

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
