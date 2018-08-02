package cn.dawnrise.android.coinmachine;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class TinyWebView extends WebView {
    private ProgressBar progressbar;

    public TinyWebView(Context context) {
        super(context);
        initWebView();
    }

    public TinyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebView();
    }

    public TinyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProgressBar(context);
        initWebView();

    }

    private void initProgressBar(Context context) {
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dp2px(context, 3), 0, 0));
        //改变progressbar默认进度条的颜色（深红色）为Color.GREEN
        progressbar.setProgressDrawable(new ClipDrawable(new ColorDrawable(Color.GREEN), Gravity.LEFT, ClipDrawable.HORIZONTAL));
        addView(progressbar);
    }

    //初始化配置WebView
    protected void initWebView() {

        getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        getSettings().setSupportMultipleWindows(true);

        getSettings().setSupportZoom(false);//是否可以缩放，默认true
        getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        getSettings().setAppCacheEnabled(true);//是否使用缓存
        getSettings().setDomStorageEnabled(true);//DOM Storage

        getSettings().setLoadsImagesAutomatically(true);
        getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//加载https和http混合模式

        getSettings().setAllowContentAccess(true);
        getSettings().setAllowFileAccess(true);
        getSettings().setAllowFileAccessFromFileURLs(true);
        getSettings().setAllowUniversalAccessFromFileURLs(true);
        getSettings().setDatabaseEnabled(true);
        getSettings().setGeolocationEnabled(false);
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        getSettings().setLoadsImagesAutomatically(true);
        getSettings().setMediaPlaybackRequiresUserGesture(false);//自动播放多媒体


        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);

//模拟微信浏览器

        getSettings().setUserAgentString("User-Agent:Mozilla/5.0 (Linux; Android 6.0; zh-cn; SM-9000) AppleWebKit/536 (KHTML, like Gecko) Version/4.0 Mobile Safari/534 MicroMessenger/6.6.5");//设置用户代理，一般不用

        //setWebChromeClient(new WebChromeClient());
        //displayWebview.setWebViewClient(new WebViewClient());//不设置 下一步的网页就会在原生浏览器中打开
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
// TODO Auto-generated method stub
//返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                Log.i("Coin", "shouldOverrideUrlLoading" + url);
                view.loadUrl(url);
                return true;
            }

            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                Log.i("Coin", "shouldOverrideUrlLoading_req" + request);
                return true;
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }




 /*           @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 不要使用super，否则有些手机访问不了，因为包含了一条 handler.cancel()
                //super.onReceivedSslError(view, handler, error);
                // 接受所有网站的证书，忽略SSL错误，执行访问网页
                handler.proceed();
            }*/
        });


    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 方法描述：根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 类描述：显示WebView加载的进度情况
     */
    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);

                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
