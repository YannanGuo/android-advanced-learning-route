#自定义View的方法与技巧

痛哟前面几篇文章, 我们对View的工作原理与事件体系有了相应的理解, 这是自定义View的基础。下面我们就结合实例具体的阐述一下如何进行
View的自定义

#一 自定义View的流程
##1.1 自定义View
###1 继承View

```
public class CustomView extends View {

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
```

###2 定义Style

我们可以在attr.xml文件中定义declare-styleable, 每个attr的format可以去多种值, 如下所示:

- reference
- string
- color
- dimension
- boolean
- integer
- float
- fraction
- enum
- flag


```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="CustomView">
        <attr name="showText" format="boolean"/>
        <attr name="labelPosition" format="enum">
            <enum name="left" value="0"/>
            <enum name="right" value="1"/>
        </attr>
    </declare-styleable>
</resources>
```

###3 读取Style

当我们定义了Style, 我们就可以在CustomView初始化的时候读取Style。Style的读取是通过TypedArray进行的。

什么是TypedArray?

>TypedArray继承于Object, 是一个数组容器, 用于存放通过obtainStyledAttributes(AttributeSet, int[], int, int)或 obtainAttributes(AttributeSet, int[])读取的数组。
它可以检索每个styleable对应的索引值。注意TypedArray是共享资源, 使用完成后要调用recycle()方法。

```
public class CustomView extends View {

    private boolean mShowText;
    private int mTextPosition;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleProperty(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleProperty(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        handleProperty(context, attrs);
    }

    public boolean isShowText() {
        return mShowText;
    }

    public void setShowText(boolean showText) {
        mShowText = showText;
        invalidate();//重新绘制布局
        requestLayout();//重新计算大小
    }

    public int getTextPosition() {
        return mTextPosition;
    }

    public void setTextPosition(int textPosition) {
        mTextPosition = textPosition;
    }

    private void handleProperty(Context context, AttributeSet attrs){
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);
        try {
            mShowText = typedArray.getBoolean(R.styleable.CustomView_showText, false);
            mTextPosition = typedArray.getInteger(R.styleable.CustomView_labelPosition, 0);
        } finally {
            typedArray.recycle();
        }
    }

```

对于这些属性, 由于它只能在View初始化的时候读取, 为了提交View的动态性, 我们通常都会给一对getter和setter方法来处理属性值。

###4 使用Style

声明引用

```
xmlns:app="http://schemas.android.com/apk/res-auto"
```

使用Style

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_custom"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.guoxiaoxing.middle.customview.CustomActivity">

    <com.guoxiaoxing.middle.customview.CustomView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:labelPosition="left"
        app:showText="true"/>

</LinearLayout>

```

##1.2 处理View绘制

View的绘制是自定义View中的重要一环, 它决定了View的外观。关于绘制的处理通常在onDraw()方法中进行。

```
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void initPaint() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        if (mTextHeight == 0) {
            mTextHeight = mTextPaint.getTextSize();
        } else {
            mTextPaint.setTextSize(mTextHeight);
        }

        mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setStyle(Paint.Style.FILL);
        mPiePaint.setTextSize(mTextHeight);

        mShadowPaint = new Paint(0);
        mShadowPaint.setColor(0xff101010);
        mShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
    }
```

##1.3 处理View布局

##1.3 处理View交互

##1.4 优化View性能

#二 自定义View的类型

##1.1 继承View重写onDraw方法

##1.2 继承ViewGroup派生特殊Layout

##1.3 继承特定View


#三 自定义View的技巧