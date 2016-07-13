package com.guoxiaoxing.middle.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Author: guoxiaoxing
 * Date: 16/7/13 下午4:27
 * Function: Custom WebViewClient
 * <p>
 * For more information, you can visit https://github.com/guoxiaoxing or contact me by
 * guoxiaoxingv@163.com
 */
public class CustomWebViewClient extends WebViewClient {
    private Context mContext;

    public CustomWebViewClient(Context context) {
        mContext = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl(request.toString());
        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (Uri.parse(url).getHost().equals("github.com/guoxiaoxing")) {
            //如果是自己站点的链接, 则用本地WebView跳转
            return false;
        }
        //如果不是自己的站点则launch别的Activity来处理
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mContext.startActivity(intent);
        return true;
    }
}  