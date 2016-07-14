package com.guoxiaoxing.middle.webview;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.guoxiaoxing.middle.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.web_view)
    WebView mWebView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        mContext = WebViewActivity.this;
        setupView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setupView() {
        setupWebView();
        mWebView.addJavascriptInterface(new WebViewInterface(mContext), "Android");
        mWebView.loadUrl(Urls.MUTIL_IMAGE);
    }

    private void setupWebView() {
        WebSettings webSettings = mWebView.getSettings();

        //JS调用
        webSettings.setJavaScriptEnabled(true);
        //缓存行为
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //图片加载
        if(Build.VERSION.SDK_INT >= 19){
            webSettings.setLoadsImagesAutomatically(true);
        }else {
            webSettings.setLoadsImagesAutomatically(false);
        }


        mWebView.setWebViewClient(new CustomWebViewClient(mContext));

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB){
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }
}
