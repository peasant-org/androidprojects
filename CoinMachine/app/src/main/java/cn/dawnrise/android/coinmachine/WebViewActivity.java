package cn.dawnrise.android.coinmachine;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView wv = findViewById(R.id.tinyWebView);
        wv.loadUrl("http://1750528_3881663.tbzun.cn./fDDw9yS7/7dW6X/Wtt69Tn/GBNyG.htm?GXTd2=9BdvGy&HqK9tJ8JT=y2y7YEWvMi&5DMJ=NRxqHwH347&dETqLftHa=p5kyx7Dw");
    }
}
