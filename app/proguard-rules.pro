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

# ── AutoAppOrganizer specific rules ──────────────────────────────

# Keep data models used by Gson serialization
-keep class com.autoapporganizer.model.** { *; }
-keep class com.autoapporganizer.core.classification.** { *; }
-keep class com.autoapporganizer.core.feedback.** { *; }
-keep class com.autoapporganizer.core.agent.** { *; }
-keep class com.autoapporganizer.core.perception.ScreenElement$Source { *; }

# Keep Gson TypeToken generic signatures
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# Keep EncryptedSharedPreferences
-keep class androidx.security.crypto.** { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
