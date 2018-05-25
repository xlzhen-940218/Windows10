-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆

#support.v4/v7包不混淆
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** { *; }
-dontwarn android.support.**    # 忽略警告


-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆    
    public static **[] values();    
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆  
    public static final android.os.Parcelable$Creator *;
}

-keep class com.alibaba.fastjson.** { *; }
-dontwarn com.alibaba.fastjson.**

-keep class com.escapevirus.** { *; }
-dontwarn com.escapevirus.**

-keep class de.greenrobot.event.** { *; }
-dontwarn de.greenrobot.event.**

-keep class com.wanjian.cockroach.** { *; }
-dontwarn com.wanjian.cockroach.**


-keep class com.xpping.windows10.entity.** { *; }
-dontwarn com.xpping.windows10.entity.**

-keep class com.xpping.windows10.fragment.base.** { *; }
-dontwarn com.xpping.windows10.fragment.base.**

-keep class com.xpping.windows10.activity.base.** { *; }
-dontwarn com.xpping.windows10.activity.base.**

-keep class com.windows.explorer.** { *; }
-dontwarn com.windows.explorer.**

-keep class com.albums.** { *; }
-dontwarn com.albums.**

-keep class com.application.interlockplugin.** { *; }
-dontwarn com.application.interlockplugin.**

-keep class com.xlzhen.cathouse.entity.** { *; }
-dontwarn com.xlzhen.cathouse.entity.**

-keep class com.baidu.translate.entity.** { *; }
-dontwarn com.baidu.translate.entity.**

-keep class com.xpping.windows10.utils.** { *; }
-dontwarn com.xpping.windows10.utils.**

-keep class com.xpping.windows10.db.** { *; }
-dontwarn com.xpping.windows10.db.**

-keep class com.xpping.windows10.receiver.** { *; }
-dontwarn com.xpping.windows10.receiver.**

-keepclassmembers class ** {
public void onEvent*(**);
}

# webview + js,保留跟 javascript相关的属性
-keepattributes *JavascriptInterface*
# keep 使用 webview 的类
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
    public void *(android.webkit.webView, jav.lang.String);
}

#保留JavascriptInterface中的方法
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

#这个根据自己的project来设置，这个类用来与js交互，所以这个类中的 字段 ，方法， 等尽量保持
-keepclassmembers public class com.desaco.webViewJavascriptBridge.WVJBWebViewClient{
   <fields>;
   <methods>;
   public *;
   private *;
}
#这个类 必须保留，这个类在WVJBWebViewClient中传递数据，如果被混淆 会导致一些callback无法调用
-keep class com.desaco.webViewJavascriptBridge.WVJBWebViewClient$WVJBMessage
#类中成员的变量名也不能混淆，这些变量名被作为json中的字段，不能改变。
-keepclassmembers class com.desaco.webViewJavascriptBridge.WVJBWebViewClient$WVJBMessage{
    <fields>;
}

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-keep interface com.squareup.okhttp.** { *; }

-dontwarn com.squareup.okhttp.**

-keep class com.liulishuo.filedownloader.** { *; }
-dontwarn com.liulishuo.filedownloader.**

-keep class org.codehaus.mojo.** { *; }
-dontwarn org.codehaus.mojo.**

-keep class org.slf4j.** { *; }
-dontwarn org.slf4j.**

-keep class com.qq.** { *; }
-dontwarn com.qq.**

-keep class okio.** { *; }
-dontwarn okio.**

-keep class pinyindb.** { *; }
-dontwarn pinyindb.**

-keep class com.j256.ormlite.** { *; }
-dontwarn com.j256.ormlite.**

-keep class com.tencent.** { *; }
-dontwarn com.tencent.**

-keep class demo.** { *; }
-dontwarn demo.**

-keep class org.apache.commons.** { *; }
-dontwarn org.apache.commons.**

-keep class com.turn.ttorrent.** { *; }
-dontwarn com.turn.ttorrent.**

-keep class net.sourcegorge.pinyin4j.** { *; }
-dontwarn net.sourcegorge.pinyin4j.**

-keep class com.xpping.windows10.entity.** { *; }
-dontwarn com.xpping.windows10.entity.**

-keep class cn.bingoogolapple.refreshlayout.** { *; }
-dontwarn cn.bingoogolapple.refreshlayout.**

-keep class com.facebook.** { *; }
-dontwarn com.facebook.**

-keep class pub.devrel.easypermissions.** { *; }
-dontwarn pub.devrel.easypermissions.**

-keep class jcifs.http.** { *; }
-dontwarn jcifs.http.**

-keep class com.hp.hpl.sparta.** { *; }
-dontwarn com.hp.hpl.sparta.**

-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keep public class com.xpping.windows10.R$*{
    public static final int *;
}

-keep public class com.application.interlockplugin.R$*{
    public static final int *;
}

-keepattributes SourceFile,LineNumberTable
