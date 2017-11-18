#在运行状态中出现报错现象
-keepattributes EnclosingMethod
#极光推送 
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#注解代码混淆
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#指定代码的压缩级别
-optimizationpasses 5
#Okio相关
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

#UIL相关
-keep class com.nostra13.universalimageloader.** { *; }
-keepclassmembers class com.nostra13.universalimageloader.** {*;}
-dontwarn com.nostra13.universalimageloader.**


#是否使用大小写混合
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

#不优化输入的类文件
-dontoptimize

#混淆时是否记录日志
-verbose
#因为混淆的问题，点击又会没有反应，这是因为openFileChooser()是系统api，所以需要在混淆是不混淆该方法
-keepclassmembers class * extends android.webkit.WebChromeClient{
public void openFileChooser(...);
}

#混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#微信分享
-keep class com.tencent.mm.**{*;}
-keep public class com.tencent.** {*;}
-keep class com.tencent.mm.sdk.** {*;}

#jni动态库
-keepclasseswithmembernames class * {
    native <methods>;
}

#资源文件
-keep public class cn.jishiyu11.xjsjd.R$*{
   public static final int *;
}
#gson混淆配置
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * implements java.io.Serializable {*;}
-dontobfuscate

#友盟
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#芝麻信用
-keep class com.alipayzhima.**{*;}
-keep class com.android.moblie.zmxy.antgroup.creditsdk.**{*;}
-keep class com.antgroup.zmxy.mobile.android.container.**{*;}
-keep class org.json.alipayzhima.**{*;}

-dontwarn android.net.**
-keep class android.net.SSLCertificateSocketFactory {*;}

-keep class com.unionpay.mobile.android.**{*;}

-keep public abstract interface com.asqw.android.Listener{
public protected <methods>;
}

-keep public class com.asqw.android{
public void Start(java.lang.String);
}

-keepclasseswithmembernames class * { native <methods>;
}

-keepclasseswithmembers class * { public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {public void *(android.view.View);
}

-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepattributes *Annotation*
#js交互
-keepattributes *JavascriptInterface*

-keep class cn.jishiyu11.xjsjd.EntityClass.**{
	*;
}

-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }
-keepattributes Signature
-ignorewarnings

#Support library
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

#Support v7
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

#Support v4
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
