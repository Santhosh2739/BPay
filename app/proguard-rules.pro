-keepattributes InnerClasses
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#-keep public class com.ypaycash.production.android.database.tables.Tables
#-keepclassmembers class com.ypaycash.production.android.database.tables.Tables** {*;}
-keep class com.microblink.** { *; }
-keepclassmembers class com.microblink.** { *; }
-dontwarn android.hardware.**
-dontwarn android.support.v4.**


-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


#facebook
-keep class com.facebook.** { *; }
-keepattributes Signature

######END OF ANDROID STANDARD APP PROGUARD SETTINGS#######################
######START OF GSON SPECIFIC CONFIG####################################


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

-dontwarn net.fortuna.ical4j.model.CalendarFactory
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class app.core.json.** { *; }
-keep class app.core.json.forceface.TransTypeInterface
-keep class ycash.wallet.json.pojo.** {*;}
-keep class org.jsoup.**{*;}
#-dontwarn javax.annotation.**
#-dontwarn org.apache.lang.**
#-dontwarn android.support.**



#   FOR THE MICROBLINK ID
-dontwarn com.google.**
#-keep class com.microblink.** { *; }
#-keepclassmembers class com.microblink.** { *; }
#-dontwarn android.hardware.**
#-dontwarn android.support.v4.**