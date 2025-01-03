pluginManagement {
     val flutterSdkPath = java.util.Properties().let {
         it.load(java.io.FileInputStream("$rootDir/local.properties"))
         it.getProperty("flutter.sdk")
     }
//    assert(flutterSdkPath!=null, "flutter.sdk not set in local.properties")

    includeBuild("$flutterSdkPath/packages/flutter_tools/gradle")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("dev.flutter.flutter-plugin-loader") version "1.0.0"
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
}

include(":app")