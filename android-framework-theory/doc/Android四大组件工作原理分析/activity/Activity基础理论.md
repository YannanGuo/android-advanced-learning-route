# Activity基础理论

作者: 郭彦南  
邮箱: guoyannanv@163.com  
博客: https://YannanGuo.github.io/  
知乎: https://www.zhihu.com/people/YannanGuo

**关于作者**

>Android Coder一枚, 目前就职于杭州大搜车汽车汽车服务有限公司。爱技术、爱烹饪、爱小提琴、爱一切新鲜有趣的事物。
人生格言: 不想当程序员的歌手不是好厨师。

**关于文章**

>作者的每一篇文章都会同时发布在Github、CSDN和知乎上, 文章顶部也会附上Github上的文章链接和代码链接。如果文章中有什么疑问欢迎发邮件与
我交流, 对于交流的问题, 请描述清楚并附上代码与日志, 我一般都会给予回复。如果文章中有什么错误, 也欢迎斧正。如果你觉得本文章对你
有所帮助, 也欢迎去star文章, 关注文章的最新的动态。

# 一 Activity生命周期

onCreate

onAttachFragment

onContentChanged

onStart

onRestoreInstanceState

onPostCreate

onResume

onPostResume

onAccachedToWindow

onCreateOptionsMenu

onPause

onSaveInstanceState

onStop

onDestory

Activity与Fragment生命周期对比图:

![](https://github.com/YannanGuo/android-advanced-learning-route/blob/master/doc/Android%E5%88%9D%E7%BA%A7%E5%86%85%E5%AE%B9/Android%E5%9B%9B%E5%A4%A7%E7%BB%84%E4%BB%B6%E5%9F%BA%E7%A1%80%E7%90%86%E8%AE%BA/art/complete_android_fragment_lifecycle.png)

图来自于https://github.com/xxv/android-lifecycle

# 二 Activity启动模式