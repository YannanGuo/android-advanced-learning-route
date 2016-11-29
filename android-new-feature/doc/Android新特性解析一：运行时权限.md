# Android新特性解析一：运行时权限

作者: 郭孝星  
邮箱: guoxiaoxingse@163.com  
博客: https://guoxiaoxing.github.io/  
简书: http://www.jianshu.com/users/66a47e04215b/latest_articles

**关于作者**

>郭孝星，字仲明，非著名程序员，代码洁癖患者，爱编程，好音乐，喜烹饪，爱一切有趣的事物和人。

**关于文章**

>作者的文章都会同时发布在个人博客和简书博客上, 文章顶部也会附上文章的Github链接。如果文章中有什么疑问欢迎发邮件与我交流, 对于交流的
问题, 请描述清楚并附上代码与日志, 一般都会给予回复。如果文章中有什么错误, 也欢迎斧正。如果你觉得本文章对你有所帮助, 也欢迎去star文章, 
关注文章的最新的动态。

**文章资源**

[文章链接](https://github.com/guoxiaoxing/android-advanced-learning-route/blob/master/android-new-feature/doc/Android%E6%96%B0%E7%89%B9%E6%80%A7%E8%A7%A3%E6%9E%90%E4%B8%80%EF%BC%9A%E8%BF%90%E8%A1%8C%E6%97%B6%E6%9D%83%E9%99%90.md)
[代码链接](https://github.com/guoxiaoxing/android-advanced-learning-route/blob/master/android-new-feature/src/main/java/com/guoyannan/feature/permission/PermissionActivity.java)

在讨论运行时权限以前，我们先来回忆一下以前的权限使用，通常我们申请一个权限，必须在应用manifest文件中包含一个或多个 <uses-permission> 标记。

例如，需要监控传入的短信的应用要指定：

```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.android.app.myapp" >
    <uses-permission android:name="android.permission.RECEIVE_SMS" />
    ...
</manifest>
```

Android 6.0开始引入了新的运行时权限检查授权机制，替代了之前安装应用的时候对权限进行授权的方案。该方案将权限分为正常权限和危险权限，对于
正常权限我们想以前一样在manifest里注册即可，但是对于危险权限即便我们注册了，它还是会在运行时进行权限检查，这便是运行时权限。

具体说来：

- 如果设备运行的是 Android 6.0（API 级别 23）或更高版本，并且应用的 targetSdkVersion 是 23 或更高版本，则应用在运行时向用户请求权限。用
户可随时调用权限，因此应用在每次运行时均需检查自身是否具备所需的权限。如需了解在应用中请求权限的更多信息，请参阅使用系统权限培训指南。
- 如果设备运行的是 Android 5.1（API 级别 22）或更低版本，并且应用的 targetSdkVersion 是 22 或更低版本，则系统会在用户安装应用时要求用户
授予权限。如果将新权限添加到更新的应用版本，系统会在用户更新应用时要求授予该权限。用户一旦安装应用，他们撤销权限的唯一方式是卸载应用。通常，
权限失效会导致 SecurityException 被扔回应用。但不能保证每个地方都是这样。例如，sendBroadcast(Intent) 方法在数据传递到每个接收者时会检查
权限，在方法调用返回后，即使权限失效，您也不会收到异常。但在几乎所有情况下，权限失效会记入系统日志。

# 权限类型

系统权限分为两类，正常权限和危险权限：

- 正常权限涵盖应用需要访问其沙盒外部数据或资源，但对用户隐私或其他应用操作风险很小的区域。例如，设置时区的权限就是正常权限。如果应用声明其需要
正常权限，系统会自动向应用授予该权限。
- 危险权限涵盖应用需要涉及用户隐私信息的数据或资源，或者可能对用户存储的数据或其他应用的操作产生影响的区域。例如，能够读取用户的联系人属于危险
权限。如果应用声明其需要危险权限，则用户必须明确向应用授予该权限。

### 正常权限

- ACCESS_LOCATION_EXTRA_COMMANDS
- ACCESS_NETWORK_STATE
- ACCESS_NOTIFICATION_POLICY
- ACCESS_WIFI_STATE
- BLUETOOTH
- BLUETOOTH_ADMIN
- BROADCAST_STICKY
- CHANGE_NETWORK_STATE
- CHANGE_WIFI_MULTICAST_STATE
- CHANGE_WIFI_STATE
- DISABLE_KEYGUARD
- EXPAND_STATUS_BAR
- GET_PACKAGE_SIZE
- INSTALL_SHORTCUT
- INTERNET
- KILL_BACKGROUND_PROCESSES
- MODIFY_AUDIO_SETTINGS
- NFC
- READ_SYNC_SETTINGS
- READ_SYNC_STATS
- RECEIVE_BOOT_COMPLETED
- REORDER_TASKS
- REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
- REQUEST_INSTALL_PACKAGES
- SET_ALARM
- SET_TIME_ZONE
- SET_WALLPAPER
- SET_WALLPAPER_HINTS
- TRANSMIT_IR
- UNINSTALL_SHORTCUT
- USE_FINGERPRINT
- VIBRATE
- WAKE_LOCK
- WRITE_SYNC_SETTINGS

参考链接
https://developer.android.com/guide/topics/security/normal-permissions.html

### 危险权限

危险权限是分组的，同一组的任何一个权限被授权了，其他权限也自动被授权。例如，一旦WRITE_CONTACTS被授权了，app也有WRITE_CONTACTS
和GET_ACCOUNTS权限了。

group:android.permission-group.CONTACTS

- permission:android.permission.WRITE_CONTACTS
- permission:android.permission.GET_ACCOUNTS
- permission:android.permission.WRITE_CONTACTS

group:android.permission-group.PHONE

- permission:android.permission.READ_CALL_LOG
- permission:android.permission.READ_PHONE_STATE
- permission:android.permission.CALL_PHONE
- permission:android.permission.WRITE_CALL_LOG
- permission:android.permission.USE_SIP
- permission:android.permission.PROCESS_OUTGOING_CALLS
- permission:com.android.voicemail.permission.ADD_VOICEMAIL

group:android.permission-group.CALENDAR

- permission:android.permission.READ_CALENDAR
- permission:android.permission.WRITE_CALENDAR

group:android.permission-group.CAMERA

- permission:android.permission.CAMERA

group:android.permission-group.SENSORS

- permission:android.permission.BODY_SENSORS

group:android.permission-group.LOCATION

- permission:android.permission.ACCESS_FINE_LOCATION
- permission:android.permission.ACCESS_COARSE_LOCATION

group:android.permission-group.STORAGE

- permission:android.permission.READ_EXTERNAL_STORAGE
- permission:android.permission.WRITE_EXTERNAL_STORAGE

group:android.permission-group.MICROPHONE

- permission:android.permission.RECORD_AUDIO

group:android.permission-group.SMS

- permission:android.permission.READ_SMS
- permission:android.permission.RECEIVE_WAP_PUSH
- permission:android.permission.RECEIVE_MMS
- permission:android.permission.RECEIVE_SMS
- permission:android.permission.SEND_SMS
- permission:android.permission.READ_CELL_BROADCASTS

我们可以使用 adb 工具从命令行管理权限：

按组列出权限和状态：

```
$ adb shell pm list permissions -d -g
```

授予或撤销一项或多项权限：

```
$ adb shell pm [grant|revoke] <permission-name> ...
```

参考链接
https://developer.android.com/guide/topics/security/permissions.html#permissions

# 运行时权限处理流程

### 1 向清单添加权限

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
        package="com.example.snazzyapp">

    <uses-permission android:name="android.permission.WRITE_CONTACTS"/>
    

    <application ...>
        ...
    </application>

</manifest>
```


### 2 检查权限

如果我们的应用需要危险权限，则每次执行需要这一权限的操作时您都必须检查自己是否具有该权限。用户始终可以自由调用此权限，因此，即使应用昨天使用了相机，它
不能假设自己今天仍具有该权限。要检查是否具有某项权限，可以调用 ContextCompat.checkSelfPermission() 方法。

例如，以下代码段显示了如何检查 Activity 是否具有写入联系人的权限：

```java
// Assume thisActivity is the current activity
int permissionCheck = ContextCompat.checkSelfPermission(thisActivity,
        Manifest.permission.WRITE_CONTACTS);
```
        
如果应用具有此权限，方法将返回 PackageManager.PERMISSION_GRANTED，并且应用可以继续操作。如果应用不具有此权限，方法将返回 PERMISSION_DENIED，且应用必须明确向用户要求权限。

### 3 请求权限

![](https://github.com/guoxiaoxing/android-advanced-learning-route/raw/master/android-new-feature/art/request_permission_dialog.png)

```java
//请求权限
ActivityCompat.requestPermissions(PermissionActivity.this,
        new String[]{Manifest.permission.WRITE_CONTACTS},
        REQUEST_CODE_FOR_PERMISSION_CALLBACK);
```

该方法会弹出一个系统对话框，来供用户选择是否允许该权限申请，当然用户可能会拒绝我们的权限申请，这种情况下说明用户不理解我们
为什么要申请这个权限，这个时候最好的做法是给用户一个解释，如下所示：

```java
//是否给用户一个关于权限申请的解释
if (ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this,
        Manifest.permission.WRITE_CONTACTS)) {
    // Show an expanation to the user *asynchronously* -- don't block
    // this thread waiting for the user's response! After the user
    // sees the explanation, try again to request the permission.

} else {
    //请求权限
    ActivityCompat.requestPermissions(PermissionActivity.this,
            new String[]{Manifest.permission.WRITE_CONTACTS},
            REQUEST_CODE_FOR_PERMISSION_CALLBACK);
}
```

>注：如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选
项，此方法将返回 false。如果设备规范禁止应用具有该权限，此方法也会返回 false。此方法异步运行：它会立即返回，并且在用户响应对话框之后，系统会使用结
果调用应用的回调方法，将应用传递的相同请求代码传递到 requestPermissions()。

### 4 处理权限请求响应

当应用请求权限时，系统将向用户显示一个对话框。当用户响应时，系统将调用应用的 onRequestPermissionsResult() 方法，向其传递用户响应。我们可以在此方法里是否已获得相应权限。
回调会将您传递的相同请求代码传递给 requestPermissions()。

```java
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
        case REQUEST_CODE_FOR_PERMISSION_CALLBACK:
            //如果权限请求被拒绝，则grantResults为空
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限已经被授予，可以添加联系人了
                Toast.makeText(PermissionActivity.this, "添加联系人权限被授予", Toast.LENGTH_LONG).show();
                insertDummyContact();
            } else {
                //权限请求被拒绝
                Toast.makeText(PermissionActivity.this, "添加联系人权限被拒绝", Toast.LENGTH_LONG).show();
            }
            break;
    }
}
```

整个流程的代码如下：

```java

/******************
 * 原生代码申请权限 *
 ******************/
 
private static String DUMMY_CONTACT_NAME = "__DUMMY CONTACT from runtime permissions sample";
private static final int REQUEST_CODE_FOR_PERMISSION_CALLBACK = 0x000001;

private void requestRuntimePermission() {
    // API 23及其以后的版本
    if (Build.VERSION.SDK_INT >= 23) {
        // 检测是否已经被授权
        if (ContextCompat.checkSelfPermission(PermissionActivity.this,
                Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //是否给用户一个关于权限申请的解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this,
                    Manifest.permission.WRITE_CONTACTS)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                //请求权限
                ActivityCompat.requestPermissions(PermissionActivity.this,
                        new String[]{Manifest.permission.WRITE_CONTACTS},
                        REQUEST_CODE_FOR_PERMISSION_CALLBACK);
            }
        }
    }
    //API 23以前版本
    else {

    }
}

@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
        case REQUEST_CODE_FOR_PERMISSION_CALLBACK:
            //如果权限请求被拒绝，则grantResults为空
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限已经被授予，可以添加联系人了
                Toast.makeText(PermissionActivity.this, "添加联系人权限被授予", Toast.LENGTH_LONG).show();
                insertDummyContact();
            } else {
                //权限请求被拒绝
                Toast.makeText(PermissionActivity.this, "添加联系人权限被拒绝", Toast.LENGTH_LONG).show();
            }
            break;
    }
}

/**
 * Accesses the Contacts content provider directly to insert a new contact.
 * <p>
 * The contact is called "__DUMMY ENTRY" and only contains a name.
 */
private void insertDummyContact() {
    // Two operations are needed to insert a new contact.
    ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(2);

    // First, set up a new raw contact.
    ContentProviderOperation.Builder op =
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
    operations.add(op.build());

    // Next, set the name for the contact.
    op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                    DUMMY_CONTACT_NAME);
    operations.add(op.build());

    // Apply the operations.
    ContentResolver resolver = getContentResolver();
    try {
        resolver.applyBatch(ContactsContract.AUTHORITY, operations);
    } catch (RemoteException e) {
        Log.d(TAG, "Could not add a new contact: " + e.getMessage());
    } catch (OperationApplicationException e) {
        Log.d(TAG, "Could not add a new contact: " + e.getMessage());
    }
}
```


# 简化权限处理流程

如果每次处理运行时权限都要写辣么一堆代码，估计我们也要被累死了～～，所以也用相应的开源库来简化运行时权限的处理。试用了很多，目前感觉最好用
的是PermissionsDispatcher，该库试用使用注解的方式，动态生成类处理运行时权限，下面介绍一个它的试用流程。

[PermissionsDispatcher](https://github.com/hotchemi/PermissionsDispatcher)

![](https://github.com/guoxiaoxing/android-advanced-learning-route/raw/master/android-new-feature/art/PermissionsDispatcher.png)

### 1 安装插件

AndroidStudio安装插件PermissionsDispatcher

### 2 添加依赖

>注：当前的${latest.version}是2.2.0

For Android Gradle Plugin >= 2.2 users

To add it to your project, include the following in your app module build.gradle file:

```
dependencies {
  compile 'com.github.hotchemi:permissionsdispatcher:${latest.version}'
  annotationProcessor 'com.github.hotchemi:permissionsdispatcher-processor:${latest.version}'
}
```

For Android Gradle Plugin < 2.2 users

To add it to your project, include the following in your project build.gradle file:

```
buildscript {
  dependencies {
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
  }
}
```

And on your app module build.gradle:

```
apply plugin: 'android-apt'

dependencies {
  compile 'com.github.hotchemi:permissionsdispatcher:${latest.version}'
  apt 'com.github.hotchemi:permissionsdispatcher-processor:${latest.version}'
}
```


### 3 右键点击生成运行时权限代码

![](https://github.com/guoxiaoxing/android-advanced-learning-route/raw/master/android-new-feature/art/AndroidAnnotationsPermissionsDispatcherPlugin.png)

填写好方法名后生成的方法会带有以下4个注解：

|Annotation|Required|Description|
|---|---|---|
|`@RuntimePermissions`|**✓**|Register an `Activity` or `Fragment` to handle permissions|
|`@NeedsPermission`|**✓**|Annotate a method which performs the action that requires one or more permissions|
|`@OnShowRationale`||Annotate a method which explains why the permission/s is/are needed. It passes in a `PermissionRequest` object which can be used to continue or abort the current permission request upon user input|
|`@OnPermissionDenied`||Annotate a method which is invoked if the user doesn't grant the permissions|
|`@OnNeverAskAgain`||Annotate a method which is invoked if the user chose to have the device "never ask again" about a permission|


```java

/******************
 * 注解方式申请权限 *
 ******************/

private static String DUMMY_CONTACT_NAME = "__DUMMY CONTACT from runtime permissions sample";

private void setupView() {
    findViewById(R.id.btn_request_runtime_permission).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //insertContactWithCheck()是自动生成的方法
            PermissionActivityPermissionsDispatcher.insertContactWithCheck(PermissionActivity.this);
        }
    });
}

@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //权限申请的结果交由PermissionActivityPermissionsDispatcher来处理
    PermissionActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
}

/**
 * 需要申请权限的操作
 */
@NeedsPermission(Manifest.permission.WRITE_CONTACTS)
void insertContact() {
    insertDummyContact();
}

/**
 * 解释权限申请原因
 *
 * @param request request
 */
@OnShowRationale(Manifest.permission.WRITE_CONTACTS)
void onShowRationale(final PermissionRequest request) {
    new AlertDialog.Builder(this)
            .setMessage("解释为何申请权限")
            .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    request.proceed();
                }
            })
            .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    request.cancel();
                }
            })
            .show();
}

/**
 * 权限申请拒绝
 */
@OnPermissionDenied(Manifest.permission.WRITE_CONTACTS)
void onPermissionDenied() {
    Toast.makeText(PermissionActivity.this, "添加联系人权限被拒绝", Toast.LENGTH_LONG).show();
}

/**
 * 权限申请不再询问
 */
@OnNeverAskAgain(Manifest.permission.WRITE_CONTACTS)
void onNeverAskAgain() {
    Toast.makeText(PermissionActivity.this, "添加联系人权限不再被询问", Toast.LENGTH_LONG).show();
}

/**
 * Accesses the Contacts content provider directly to insert a new contact.
 * <p>
 * The contact is called "__DUMMY ENTRY" and only contains a name.
 */
private void insertDummyContact() {
    // Two operations are needed to insert a new contact.
    ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(2);

    // First, set up a new raw contact.
    ContentProviderOperation.Builder op =
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
    operations.add(op.build());

    // Next, set the name for the contact.
    op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                    DUMMY_CONTACT_NAME);
    operations.add(op.build());

    // Apply the operations.
    ContentResolver resolver = getContentResolver();
    try {
        resolver.applyBatch(ContactsContract.AUTHORITY, operations);
    } catch (RemoteException e) {
        Log.d(TAG, "Could not add a new contact: " + e.getMessage());
    } catch (OperationApplicationException e) {
        Log.d(TAG, "Could not add a new contact: " + e.getMessage());
    }
}
```

