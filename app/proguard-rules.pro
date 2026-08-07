# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Compose


# Retrofit
-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations

-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }

## Gson
#-keepattributes *Annotation*
#
#-keep class com.google.gson.** { *; }
#
#-keepclassmembers class * {
#    @com.google.gson.annotations.SerializedName <fields>;
#}
# Gson

-keepattributes Signature
-keepattributes *Annotation*

-keep class com.google.gson.** { *; }

# Keep TypeToken generic information
-keep class com.google.gson.reflect.TypeToken { *; }

-keep class * extends com.google.gson.reflect.TypeToken

# Keep HistoryItem model
-keep class com.harish.floatiq.data.HistoryItem { *; }
# Currency Converter
-keep class com.harish.floatiq.ui.currency.CurrencyResponse { *; }

-keep class com.harish.floatiq.ui.currency.CurrencyApi { *; }

-keep class com.harish.floatiq.ui.currency.RetrofitInstance { *; }

-keep class com.harish.floatiq.ui.currency.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
# ML Kit OCR
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**

# CameraX
-keep class androidx.camera.** { *; }
-dontwarn androidx.camera.**

# AdMob
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.ads.** { *; }
-dontwarn com.google.android.gms.ads.**

# Retrofit API Models
-keep class com.harish.floatiq.api.** { *; }

# Prevent warnings
-dontwarn org.jetbrains.annotations.**
-dontwarn javax.annotation.**

# Room
-keep class androidx.room.** { *; }
-keep interface androidx.room.** { *; }