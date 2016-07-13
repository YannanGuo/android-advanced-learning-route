#WebView使用技巧

本系列本章用来介绍WebView的使用和性能优化问题, 共分为两篇:

[WebView使用技巧]()
[WebView性能优化]()


WebView也是Android View的一种, 我们通常用它来在应用内部展示网页, 和以往一样, 我们先来简单看一下它的基本用法。

添加网络权限

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

在布局中添加WebView

```xml
<?xml version="1.0" encoding="utf-8"?>
<WebView  xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/webview"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
/>
```

使用WebView加载网页

```
WebView myWebView = (WebView) findViewById(R.id.webview);
myWebView.loadUrl("http://www.example.com");
```

以上就是WebView的简单用法, 相比大家已经十分熟悉, 下面我们就来逐一看看WebView的其他特性。

#一 JavaScript代码和Android代码的相互调用

##1.1 JavaSript代码调用Android代码

JavaScript代码和Android代码是通过addJavascriptInterface()来建立连接的, 我们来看下具体的用法。

1 设置WebView支持JavaScript

```java
webView.getSettings().setJavaScriptEnabled(true);
```

2 在Android工程里定义一个接口

```java
public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}
```

**注意**: API >= 17时, 必须在被JavaScript调用的Android方法前添加@JavascriptInterface注解, 否则将无法识别。

3 在Android代码中将该接口添加到WebView

```java
WebView webView = (WebView) findViewById(R.id.webview);
webView.addJavascriptInterface(new WebAppInterface(this), "Android");
```

这个"Android"就是我们为这个接口取的别名, 在JavaScript就可以通过Android.showToast(toast)这种方式来调用此方法。

4 在JavaScript中调用Android方法

```js
<input type="button" value="Say hello" onClick="showAndroidToast('Hello Android!')" />

<script type="text/javascript">
    function showAndroidToast(toast) {
        Android.showToast(toast);
    }
</script>
```

在JavaScript中我们不用再去实例化WebAppInterface接口, WebView会自动帮我们完成这一工作, 使它能够为WebPage所用。

#二 WebView的导航问题

##2.1 页面跳转

当我们在WebView点击链接时, 默认的WebView会直接跳转到别的浏览器中, 如果想要实现在WebView内跳转就需要设置WebViewClient, 下面我们先来
说说WebView、WebViewClient、WebChromeClient三者的区别。

- WebView: 主要负责解析和渲染网页
- WebViewClient: 辅助WebView处理各种通知和请求事件
- WebChromeClient: 辅助WebView处理JavaScript中的对话框, 网址图标和标题等

如果我们想控制不同链接的跳转方式, 我们需要继承WebViewClient重写shouldOverrideUrlLoading()方法

```java
    static class CustomWebViewClient extends WebViewClient {

        private Context mContext;

        public CustomWebViewClient(Context context) {
            mContext = context;
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
```

关于shouldOverrideUrlLoading()方法的两点说明:

1 方法返回值
返回true: Android 系统会处理URL, 一般是唤起系统浏览器。
返回false: 当前 WebView 处理URL。

由于默认放回false, 如果我们只想在WebView内处理链接跳转只需要设置mWebView.setWebViewClient(new WebViewClient())即可

```java
/** 
     * Give the host application a chance to take over the control when a new 
     * url is about to be loaded in the current WebView. If WebViewClient is not 
     * provided, by default WebView will ask Activity Manager to choose the 
     * proper handler for the url. If WebViewClient is provided, return true 
     * means the host application handles the url, while return false means the 
     * current WebView handles the url. 
     * This method is not called for requests using the POST "method". 
     * 
     * @param view The WebView that is initiating the callback. 
     * @param url The url to be loaded. 
     * @return True if the host application wants to leave the current WebView 
     *         and handle the url itself, otherwise return false. 
     */  
    public boolean shouldOverrideUrlLoading(WebView view, String url) {  
        return false;  
    }  
```

2 方法deprecated问题

shouldOverrideUrlLoading()方法在API >= 24时被标记deprecated, 它的替代方法是

```
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.toString());
            return true;
        }
```

但是public boolean shouldOverrideUrlLoading(WebView view, String url)支持更广泛的API我们在使用的时候还是它, 
关于这两个方法的讨论可以参见:

http://stackoverflow.com/questions/36484074/is-shouldoverrideurlloading-really-deprecated-what-can-i-use-instead  
http://stackoverflow.com/questions/26651586/difference-between-shouldoverrideurlloading-and-shouldinterceptrequest

##2.2 页面回退

Android的返回键, 如果想要实现WebView内网页的回退, 可以重写onKeyEvent()方法。

```java
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    // Check if the key event was the Back button and if there's history
    if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
        myWebView.goBack();
        return true;
    }
    // If it wasn't the Back key or there's no web page history, bubble up to the default
    // system behavior (probably exit the activity)
    return super.onKeyDown(keyCode, event);
}
```
#三 WebView缓存实现

在项目中如果使用到WebView控件, 当加载html页面时, 会在/data/data/包名目录下生成database与cache两个文件夹。
请求的url记录是保存在WebViewCache.db, 而url的内容是保存在WebViewCache文件夹下。

控制缓存行为

```java
WebSettings webSettings = mWebView.getSettings();
//优先使用缓存
webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); 
//只在缓存中读取
webSettings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
/不使用缓存
WwebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
```

清除缓存

```java
mWebView.clearCache(true);
mWebView.clearHistory();
```

#四 WebView Cookies

添加Cookies

```java
public void synCookies() {
    if (!CacheUtils.isLogin(this)) return;
    CookieSyncManager.createInstance(this);
    CookieManager cookieManager = CookieManager.getInstance();
    cookieManager.setAcceptCookie(true);
    cookieManager.removeSessionCookie();//移除
    String cookies = PreferenceHelper.readString(this, AppConfig.COOKIE_KEY, AppConfig.COOKIE_KEY);
    KJLoger.debug(cookies);
    cookieManager.setCookie(url, cookies);
    CookieSyncManager.getInstance().sync();
}
```

清除Cookies

```java
CookieManager.getInstance().removeSessionCookie();
```