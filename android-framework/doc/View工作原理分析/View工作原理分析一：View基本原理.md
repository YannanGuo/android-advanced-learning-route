# View工作原理分析一：View基本原理

作者: 郭孝星  
邮箱: guoxiaoxingse@gmail.com  
博客: https://guoxiaoxing.github.io/  
简书: http://www.jianshu.com/users/66a47e04215b/latest_articles

**关于作者**

>郭孝星，字仲明，非著名程序员，代码洁癖患者，爱编程，好音乐，喜烹饪，爱一切有趣的事物和人。

**关于文章**

>作者的文章都会同时发布在个人博客和简书博客上, 文章顶部也会附上文章的Github链接。如果文章中有什么疑问欢迎发邮件与我交流, 对于交流的问
题, 请描述清楚并附上代码与日志, 我一般都会给予回复。如果文章中有什么错误, 也欢迎斧正。如果你觉得本文章对你有所帮助, 也欢迎去star文
章, 关注文章的最新的动态。

View是Android所有控件的基类，我们先看一下整个View体系：

![](https://github.com/guoxiaoxing/android-advanced-learning-route/raw/master/android-framework/doc/View工作原理分析/art/view-system.png)

# View坐标

提到View的基本属性，我们先来看一下View坐标系，Android坐标系是一个三维坐标，Z轴向上，X轴向右，Y轴向下。这样讲比较抽象，我们先来看一张图：

![](https://github.com/guoxiaoxing/android-advanced-learning-route/raw/master/android-framework/doc/View工作原理分析/art/view-coordinate.png)

相信通过上面的图形描述，大家对View中各种坐标的概念已经比较明白了，下面简单的再说一下View类中各种方法的含义。

View宽高方法

```java
getHeight()：layout后有效，返回值是mRight-mLeft，一般会参考measure的宽度（measure可能没用），但不是必须的。
getWidth()：layout后有效，返回值是mBottom-mTop，一般会参考measure的高度（measure可能没用），但不是必须的。
getMeasuredWidth()：返回measure过程得到的mMeasuredWidth值，供layout参考，或许没用。
getMeasuredHeight()：返回measure过程得到的mMeasuredHeight值，供layout参考，或许没用。
```

View静态坐标方法

>通过如下方法可以获得View到其父控件（ViewGroup）的距离：

```java
getTop()：获取View自身顶边到其父布局顶边的距离
getLeft()：获取View自身左边到其父布局左边的距离
getRight()：获取View自身右边到其父布局左边的距离
getBottom()：获取View自身底边到其父布局顶边的距离
getX()：返回值为getLeft()+getTranslationX()，当setTranslationX()时getLeft()不变，getX()变。
getY()：返回值为getTop()+getTranslationY()，当setTranslationY()时getTop()不变，getY()变。
```

MotionEvent坐标方法

>上图那个灰色的点就是我们触摸的点，我们知道无论是View还是ViewGroup，最终的点击事件都会由onTouchEvent(MotionEvent event)方法来处理，MotionEvent也提供了各种获取焦点坐标的方法：

```java
getX()：获取点击事件距离控件左边的距离，即视图坐标
getY()：获取点击事件距离控件顶边的距离，即视图坐标
getRawX()：获取点击事件距离整个屏幕左边距离，即绝对坐标
getRawY()：获取点击事件距离整个屏幕顶边的的距离，即绝对坐标
```

以上便是我们常用的Android坐标系的各种概念和方法。

另外上图中也提到类StatusBar，AppBar以及布局绘制区域这些概念，我们顺便也把如何获取它们的size的方法也写在下面。

```java
//获取屏幕区域的宽高等尺寸获取
DisplayMetrics metrics = new DisplayMetrics();
getWindowManager().getDefaultDisplay().getMetrics(metrics);
int widthPixels = metrics.widthPixels;
int heightPixels = metrics.heightPixels;
```

```java
//应用程序App区域宽高等尺寸获取
Rect rect = new Rect();
getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
```

```java
//获取状态栏高度
Rect rect= new Rect();
getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
int statusBarHeight = rectangle.top;
```

```java
//View布局区域宽高等尺寸获取
Rect rect = new Rect();  
getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(rect);  
```

# TouchSlop

>TouchSlop是系统所能识别的出的最小滑动距离。

获取方式：

```java
ViewConfiguartion.get(getContext()).getScaledTouchSlop();
```

既然说是最小滑动距离，换句话说如果手指在屏幕上滑动的距离小于这个值，那么系统不认为我们进行栏滑动操作。这个值通常可以用来做
滑动过滤，比方说我们在处理滑动时，如果两次滑动的距离小于这个值，我们就可以认为未达到滑动距离的临界值，即认为它没有滑动。