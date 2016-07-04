# Android进阶学习路线

学习目标：  

1. 深入Java  
2. 深入Android系统  
4. 深入Linux内核  
4. 了解编译原理等CS基本知识  

#一 技术图谱

##1.1 Java

1. Java基本数据类型与表达式，分支循环。
2. String和StringBuffer的使用、正则表达式。
3. 面向对象的抽象，封装，继承，多态，类与对象，对象初始化和回收；构造函数、this关键字、方法和方法的参数传递过程、static关键字、内部类。
4. 对象实例化过程、方法的覆盖、final关键字、抽象类、接口、继承的优点和缺点剖析；对象的多态性：子类和父类之间的转换、抽象类和接口在多态中的应用、多态带来的好处。
5. Java异常处理，异常的机制原理。
6. 常用的设计模式：Singleton、Template、Strategy模式。
7. JavaAPI介绍：种基本数据类型包装类，System和Runtime类，Date和DateFomat类等。
8. Java集合介绍：Collection、Set、List、ArrayList、LinkedList、Hashset、Map、HashMap、Iterator等常用集合类API。
9. JavaI/O输入输出流：File和FileRandomAccess类，字节流InputStream和OutputStream，字符流Reader和Writer，以及相应实现类，IO性能分析，字节和字符的转化流，包装流的概念，以及常用包装类，计算机编码。
10. Java高级特性：反射和泛型。
11. 多线程原理：如何在程序中创建多线程(Thread、Runnable)，线程安全问题，线程的同步，线程之间的通讯、死锁。

##1.2 Android

###1.2.1 基础内容

####1 Android基础控件

ViewGroup及其子类

TextView及其子类

ImageView及其子类

Dialog及其子类

Menu及其子类

Progressbar及其子类

AdapterView及其子类

####2 Android高级控件

ScrollView
RecyclerView

####3 Android自定义控件


####4 Android四大组件

Activity

AndroidManifest.xml文件的作用
Intent的使用方法
使用Intent传递数据的方法
启动Activity的方法
IntentFilter的使用方法
Activity Group的使用方法

Fragment

ContentProvider

BroadcastReceiver

####5 Android网络编程与数据存储

1).基于Android平台的HTTP通讯：

Http协议回顾
使用Get方法向服务器提交数据的方法
使用POST方法向服务器提交数据的实现方法
使用Http协议实现多线程下载
使用Http协议实现断点续传

2).Android数据存储技术：

SQLite3数据库简介
SQL语句回顾
SQLite3编程接口介绍
SQLite3事务管理
SQLite3游标使用方法
SQLite3性能分析
访问SDCard的方法
访问SharedPreferences的方法

###1.2.2 中级内容

[Android四大组件工作流程与原理分析]()

[View工作原理与事件体系]()

[Window工作流程与原理分析]()

[Android动画工作流程与原理分析]()

[Android线程与线程池原理分析]()

[Android跨进程通信原理分析]()

[Android性能优化技巧]()

###1.2.3 高级内容

继续加深理解”稍微深入的知识点“中所定义的内容
了解系统核心机制：
了解SystemServer的启动过程
了解主线程的消息循环模型
了解AMS和PMS的工作原理
能够回答问题”一个应用存在多少个Window？“
了解四大组件的大概工作流程
…
基本知识点的细节
Activity的启动模式以及异常情况下不同Activity的表现
Service的onBind和onReBind的关联
onServiceDisconnected(ComponentName className)和binderDied()的区别
AsyncTask在不同版本上的表现细节
线程池的细节和参数配置
…
熟悉设计模式，有架构意识学习方法

#二 博客写作

博客写作计划同步开启，坚持做原创有态度有内涵的博客。博客托管到Github上并同步发布到各大平台。

博客数量：每周两篇

博客平台：[个人博客](https://guoxiaoxing.github.io/)  
         [简书博客](http://www.jianshu.com/users/66a47e04215b/latest_articles)  
         [CSDN博客](http://blog.csdn.net/allenwells)
          
#三 项目演练

完整的项目对技术的精进是大有裨益的，打算先以一个相机项目作为开端，来加入学习到的各种技术点。

##3.1 相机项目

