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

# ==========================================
# Kotlinx Serialization
# ==========================================
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep Serializers
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep @Serializable annotated classes
-keep,includedescriptorclasses class **$$serializer { *; }
-keepclassmembers class * {
    *** Companion;
}
-keepclasseswithmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep all Serializable classes in navigation and feature packages
-keep @kotlinx.serialization.Serializable class com.lilin.gamelibrary.navigation.** { *; }
-keep @kotlinx.serialization.Serializable class com.lilin.gamelibrary.feature.**.* { *; }

# ==========================================
# Jetpack Compose Navigation - Type-Safe Routes
# ==========================================
# Keep all classes in feature packages (for navigation routes)
-keep class com.lilin.gamelibrary.feature.** { *; }
-keep class com.lilin.gamelibrary.navigation.** { *; }

# ==========================================
# Kotlin Reflection (Required for Navigation)
# ==========================================
-keep class kotlin.Metadata { *; }
-keep class kotlin.reflect.** { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# ==========================================
# Hilt (Dependency Injection)
# ==========================================
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# ==========================================
# Retrofit & OkHttp
# ==========================================
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# ==========================================
# Room Database
# ==========================================
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public static ** getDatabase(...);
}
