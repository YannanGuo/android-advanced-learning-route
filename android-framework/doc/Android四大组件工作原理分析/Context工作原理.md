Context的功能

1. 描述的是一个应用程序环境的信息，即上下文。
2. 该类是一个抽象(abstract class)类，Android提供了该抽象类的具体实现类(后面我们会讲到是ContextIml类)。
3. 通过它我们可以获取应用程序的资源和类，也包括一些应用级别操作，例如：启动一个Activity，发送广播，接受Intent信息等。


Context的创建时机

1. 创建Application对象时， 而且整个App共一个Application对象
2. 创建Service对象时
3. 创建Activity对象时

Context的获取

1. getApplication()：返回Context对象
2. getApplicaionContexxt()： 返回Context对象，作用域更广。
3. getBaseContext()： 返回ContextImpl对象