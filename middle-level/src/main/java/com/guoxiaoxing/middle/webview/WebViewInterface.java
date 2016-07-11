package com.guoxiaoxing.middle.webview;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Author: guoxiaoxing
 * Date: 16/7/11 上午11:29
 * Function:
 *
 * how to use in js:
 *
 * <input type="button" value="Say hello" onClick="showAndroidToast('Hello Android!')" />
 * <script type="text/javascript">
 *   function showAndroidToast(toast) {
 *   Android.showToast(toast);
 8   }
 * </script>
 *
 * For more information, you can visit https://github.com/guoxiaoxing or contact me by
 * guoxiaoxingv@163.com
 */
public class WebViewInterface {

    Context mContext;

    public WebViewInterface(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}  