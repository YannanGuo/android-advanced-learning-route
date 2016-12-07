# View工作原理分析二：View事件体系

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

这一篇文章我们来学习View事件的分发与处理，这个知识点在动画、自定义View等很多方面有着重要的应用。我们先来看看事件体系中三个最重要的方法。

```java
public boolean dispatchTouchEvent(MotionEvent ev)
```

View/ViewGroup处理事件分发的发起者，View/ViewGroup接收到触控事件最先调起的就是这个方法，然后在该方法中判断是否处理拦截或是将事件分发给子容器

```java
public boolean onInterceptTouchEvent(MotionEvent ev)
```

ViewGroup专用，通过该方法可以达到控件事件的分发方向，一般可以在该方法中判断将事件给ViewGroup独吞或是它继续传递给子容器，是处理事件冲突的最佳地点

```java
public boolean onTouchEvent(MotionEvent event)
```

触控事件的真正处理者，最后每个事件都会在这里被处理

通过上面的介绍，我们对这三个方法有了大致的印象，下面我们图文并茂地描述一下，当我们点击了一个View之后整个点击事件的分发与处理。

当我们用手指点击了app上的一个页面，事件编产生并开始传递了，最先产生的事件是Down，最先接收到事件的是Activity，事件的产生与传递如下所示：

事件产生顺序

>Down -> Move -> ... -> Move -> Up

事件传递顺序

>Activity -> Window -> View -> View -> View -> ...

可以看到View最后接收到事件，View接收了事件之后便会按照事件分发机制去分发事件：

1 dispatchTouchEvent(MotionEvent ev)首先进行事件的分发 如果事件能够传递到当前View，那么该方法一定会被调用，返回值受当前View的onTouchEvent()和
下级View的dispatchTouchEvent()影响，表示是否消耗当前事件。返回true表示消耗，返回false表示不消耗。   
2 onInterceptTouchEvent(MotionEvent ev)在dispatchTouchEvent(MotionEvent ev)方法内部调用，用来判断是否拦截某个事件，如果当前View
拦截了某个事件，那么在同一个事件序列中，此方法不会再被调用，返回值表示时候拦截某个事件，返回true表示拦截，返回false表示不拦截。    
3 onTouchEvent(MotionEvent event)也在onInterceptTouchEvent(MotionEvent ev)方法内部被调用，用来处理点击事件，返回值表示是否消耗当
前事件，返回true表示消耗，返回false表示不消耗。如果不消耗，则在同一个事件序列中，当前View无法再次接收到该事件。     

我们可以用一段伪代码来形象的描述这个过程：

```java
MotionEvent ev;//down or move or up or others...
viewgroup.dispatchTouchEvent(ev);
public boolean dispatchTouchEvent(MotionEvent ev){
 boolean isConsumed = false;
   if(onInterceptTouchEvent(ev)){
     isCousumed = this.onTouchEvent(ev);
   }else{
      isConsumed = childView.dispatchTouchEvent(ev);
   }
   return isConsumed;
   }
```

到此是不是感觉，哇塞～，原来事件分发这么简单:)，然而很遗憾的告诉你，这只是个提纲，大概地描述了一下整个事件的分发和处理过程。下面我们来进一步的补充细节。

下面我们再说几个关于事件分发与处理相关的结论。

1 正常情况下，一个事件序列只能被一个View拦截消耗。    
2 ViewGroup默认不拦截任何事件。   
3 VIew的onTouchEvent()方法默认会消耗事件（返回true），除非它是不可点击的（clickable和longclickable同时为false，另外enable属性并不影响onTouchEvent()）
的返回值，哪怕一个enable为false，只要clickable与longclickable有一个为true，onTouchEvent()就返回true。）。

>注意：View是否为clickable是分情况的，比方说Button的clickable＝true，TextView的clickable=false。但是TextView也可以设置点击监听，那是因为TextView.setOnClickListener()
方法里会调用setClickable(true)。 

