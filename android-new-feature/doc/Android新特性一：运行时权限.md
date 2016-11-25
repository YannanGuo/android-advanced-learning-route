# Android新特性一：运行时权限

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

# 正常权限与危险权限

既然提到正常权限和危险权限，我们就需要了解哪些是正常权限，哪些是危险权限。

统权限分为几个保护级别。需要了解的两个最重要保护级别是正常权限和危险权限：

- 正常权限涵盖应用需要访问其沙盒外部数据或资源，但对用户隐私或其他应用操作风险很小的区域。例如，设置时区的权限就是正常权限。如果应用声明其需要
正常权限，系统会自动向应用授予该权限。
- 危险权限涵盖应用需要涉及用户隐私信息的数据或资源，或者可能对用户存储的数据或其他应用的操作产生影响的区域。例如，能够读取用户的联系人属于危险
权限。如果应用声明其需要危险权限，则用户必须明确向应用授予该权限。

### 正常权限

https://developer.android.com/guide/topics/security/normal-permissions.html

ACCESS_LOCATION_EXTRA_COMMANDS
ACCESS_NETWORK_STATE
ACCESS_NOTIFICATION_POLICY
ACCESS_WIFI_STATE
BLUETOOTH
BLUETOOTH_ADMIN
BROADCAST_STICKY
CHANGE_NETWORK_STATE
CHANGE_WIFI_MULTICAST_STATE
CHANGE_WIFI_STATE
DISABLE_KEYGUARD
EXPAND_STATUS_BAR
GET_PACKAGE_SIZE
INSTALL_SHORTCUT
INTERNET
KILL_BACKGROUND_PROCESSES
MODIFY_AUDIO_SETTINGS
NFC
READ_SYNC_SETTINGS
READ_SYNC_STATS
RECEIVE_BOOT_COMPLETED
REORDER_TASKS
REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
REQUEST_INSTALL_PACKAGES
SET_ALARM
SET_TIME_ZONE
SET_WALLPAPER
SET_WALLPAPER_HINTS
TRANSMIT_IR
UNINSTALL_SHORTCUT
USE_FINGERPRINT
VIBRATE
WAKE_LOCK
WRITE_SYNC_SETTINGS

### 危险权限

https://developer.android.com/guide/topics/security/permissions.html#permissions

group:android.permission-group.CONTACTS
  permission:android.permission.WRITE_CONTACTS
  permission:android.permission.GET_ACCOUNTS
  permission:android.permission.READ_CONTACTS

group:android.permission-group.PHONE
  permission:android.permission.READ_CALL_LOG
  permission:android.permission.READ_PHONE_STATE
  permission:android.permission.CALL_PHONE
  permission:android.permission.WRITE_CALL_LOG
  permission:android.permission.USE_SIP
  permission:android.permission.PROCESS_OUTGOING_CALLS
  permission:com.android.voicemail.permission.ADD_VOICEMAIL

group:android.permission-group.CALENDAR
  permission:android.permission.READ_CALENDAR
  permission:android.permission.WRITE_CALENDAR

group:android.permission-group.CAMERA
  permission:android.permission.CAMERA

group:android.permission-group.SENSORS
  permission:android.permission.BODY_SENSORS

group:android.permission-group.LOCATION
  permission:android.permission.ACCESS_FINE_LOCATION
  permission:android.permission.ACCESS_COARSE_LOCATION

group:android.permission-group.STORAGE
  permission:android.permission.READ_EXTERNAL_STORAGE
  permission:android.permission.WRITE_EXTERNAL_STORAGE

group:android.permission-group.MICROPHONE
  permission:android.permission.RECORD_AUDIO

group:android.permission-group.SMS
  permission:android.permission.READ_SMS
  permission:android.permission.RECEIVE_WAP_PUSH
  permission:android.permission.RECEIVE_MMS
  permission:android.permission.RECEIVE_SMS
  permission:android.permission.SEND_SMS
  permission:android.permission.READ_CELL_BROADCASTS

我们可以使用 adb 工具从命令行管理权限：

按组列出权限和状态：

```
$ adb shell pm list permissions -d -g
```

授予或撤销一项或多项权限：

```
$ adb shell pm [grant|revoke] <permission-name> ...
```

# 处理运行时权限

## 检查权限

如果我们的应用需要危险权限，则每次执行需要这一权限的操作时您都必须检查自己是否具有该权限。用户始终可以自由调用此权限，因此，即使应用昨天使用了相机，它
不能假设自己今天仍具有该权限。要检查是否具有某项权限，可以调用 ContextCompat.checkSelfPermission() 方法。

例如，以下代码段显示了如何检查 Activity 是否具有读取联系人的权限：

```java
// Assume thisActivity is the current activity
int permissionCheck = ContextCompat.checkSelfPermission(thisActivity,
        Manifest.permission.READ_CONTACTS);
```
        
如果应用具有此权限，方法将返回 PackageManager.PERMISSION_GRANTED，并且应用可以继续操作。如果应用不具有此权限，方法将返回 PERMISSION_DENIED，且应用必须明确向用户要求权限。

## 请求权限

如果我们的应用需要应用清单中列出的危险权限，那么，它必须要求用户授予该权限。Android 为您提供了多种权限请求方式。调用这些方法将显示一个标准的 Android 对话框，不过，您不能对它们进行自定义。

关于解释应用需要权限的原因

![](/art/request_permission_dialog.png)

```
在某些情况下，我们需要帮助用户了解您的应用为什么需要某项权限。例如，如果用户启动一个摄影应用，用户对应用要求使用相机的权限可能不会感到吃惊，但用户可能无法理解为什么此应用想要访问用户的位置或联系人。
在请求权限之前，不妨为用户提供一个解释。请记住，您不需要通过解释来说服用户；如果您提供太多解释，用户可能发现应用令人失望并将其移除。

我们也可以采用的一个方法是仅在用户已拒绝某项权限请求时提供解释。如果用户继续尝试使用需要某项权限的功能，但继续拒绝权限请求，则可能表明用户不理解应用为什么需要此权限才能提供相关功能。
对于这种情况，比较好的做法是显示解释。
```

为了帮助查找用户可能需要解释的情形，Android 提供了一个实用程序方法，即 shouldShowRequestPermissionRationale()。如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。

注：如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。如果设备规范禁止应用具有该权限，此方法也会返回 false。

如果应用尚无所需的权限，则应用必须调用一个 requestPermissions() 方法，以请求适当的权限。应用将传递其所需的权限，以及您指定用于识别此权限请求的整型请求代码。
|
此方法异步运行：它会立即返回，并且在用户响应对话框之后，系统会使用结果调用应用的回调方法，将应用传递的相同请求代码传递到 requestPermissions()。


## 处理权限请求响应

当应用请求权限时，系统将向用户显示一个对话框。当用户响应时，系统将调用应用的 onRequestPermissionsResult() 方法，向其传递用户响应。我们可以在此方法里是否已获得相应权限。
回调会将您传递的相同请求代码传递给 requestPermissions()。例如，如果应用请求 READ_CONTACTS 访问权限，则它可能采用以下回调方法

系统显示的对话框说明了您的应用需要访问的权限组；它不会列出具体权限。例如，如果您请求 READ_CONTACTS 权限，系统对话框只显示您的应用需要访问设备的联系人。用户只需要为每个权限组授予一次权限。如果您的应用请求该组中的任何其他权限（已在您的应用清单中列出），系统将自动授予应用这些权限。当您请求此权限时，系统会调用您的 onRequestPermissionsResult() 回调方法，并传递 PERMISSION_GRANTED，如果用户已通过系统对话框明确同意您的权限请求，系统将采用相同方式操作。

注：您的应用仍需要明确请求其需要的每项权限，即使用户已向应用授予该权限组中的其他权限。此外，权限分组在将来的 Android 版本中可能会发生变化。您的代码不应依赖特定权限属于或不属于相同组这种假设。

例如，假设您在应用清单中列出了 READ_CONTACTS 和 WRITE_CONTACTS。如果您请求 READ_CONTACTS 且用户授予了此权限，那么，当您请求 WRITE_CONTACTS 时，系统将立即授予您该权限，不会与用户交互。

如果用户拒绝了某项权限请求，您的应用应采取适当的操作。例如，您的应用可能显示一个对话框，解释它为什么无法执行用户已经请求但需要该权限的操作。

当系统要求用户授予权限时，用户可以选择指示系统不再要求提供该权限。这种情况下，无论应用在什么时候使用 requestPermissions() 再次要求该权限，系统都会立即拒绝此请求。系统会调用您的 onRequestPermissionsResult() 回调方法，并传递 PERMISSION_DENIED，如果用户再次明确拒绝了您的请求，系统将采用相同方式操作。这意味着当您调用 requestPermissions() 时，您不能假设已经发生与用户的任何直接交互。


