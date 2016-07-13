package com.guoxiaoxing.middle.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Author: guoxiaoxing
 * Date: 16/7/13 下午6:12
 * Function: Custom WebView
 * <p>
 * For more information, you can visit https://github.com/guoxiaoxing or contact me by
 * guoxiaoxingv@163.com
 */
public class CustomWebView extends WebView {
    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean hasVerticalScrollbar() {
        return computeHorizontalScrollRange() > computeHorizontalScrollExtent();
    }

    float mCurrContentHeight;
    float mThreshold;

    @Override
    protected void onScrollChanged(int newX, int newY, int oldX, int oldY) {
        super.onScrollChanged(newX, newY, oldX, oldY);
        if (newY != oldY) {
            float contentHeight = getContentHeight() * getScale();
            // 当前内容高度下从未触发过, 浏览器存在滚动条且滑动到将抵底部位置
            if (mCurrContentHeight != contentHeight && newY > 0 && contentHeight <= newY + getHeight() + mThreshold) {
                // TODO Something...
                mCurrContentHeight = contentHeight;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
        }
        return super.onTouchEvent(event);
    }
}