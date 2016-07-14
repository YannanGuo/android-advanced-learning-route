#WebView性能优化

#一 优化网页加载速度

默认情况html代码下载到WebView后，webkit开始解析网页各个节点，发现有外部样式文件或者外部脚本文件时，会异步发起网络请求下载文件，但如果
在这之前也有解析到image节点，那势必也会发起网络请求下载相应的图片。在网络情况较差的情况下，过多的网络请求就会造成带宽紧张，影响到css或
js文件加载完成的时间，造成页面空白loading过久。解决的方法就是告诉WebView先不要自动加载图片，等页面finish后再发起图片加载。

设置WebView, 先禁止加载图片

```java
WebSettings webSettings = mWebView.getSettings();

//图片加载
if(Build.VERSION.SDK_INT >= 19){
    webSettings.setLoadsImagesAutomatically(true);
}else {
    webSettings.setLoadsImagesAutomatically(false);
}
```

覆写WebViewClient的onPageFinished()方法, 页面加载结束后再加载图片

```java
@Override
public void onPageFinished(WebView view, String url) {
    super.onPageFinished(view, url);
    if (!view.getSettings().getLoadsImagesAutomatically()) {
        view.getSettings().setLoadsImagesAutomatically(true);
    }
}
```

**注意**: 4.4以上系统在onPageFinished时再恢复图片加载时,如果存在多张图片引用的是相同的src时，会只有一个image标签得到加载，因而对于这样的系统我们就先直接加载。

#二 硬件加速页面闪烁问题

4.0以上的系统我们开启硬件加速后，WebView渲染页面更加快速，拖动也更加顺滑。但有个副作用就是，当WebView视图被整体遮住一块，然后突然恢复时（比如使用SlideMenu将WebView从侧边
滑出来时），这个过渡期会出现白块同时界面闪烁。解决这个问题的方法是在过渡期前将WebView的硬件加速临时关闭，过渡期后再开启，如下所示:

过度前关闭硬件加速

```java
if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB){
    mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
}
```

过度前开启硬件加速

```java
if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB){
    mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
}
```