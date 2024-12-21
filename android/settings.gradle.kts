pluginManagement {
    val prop = java.util.Properties()
//    val flutterSdkPath = Properties().apply {
        val fis = java.io.FileInputStream("local.properties")
        prop.load(fis);
        val flutterSdkPath = prop.getProperty("flutter.sdk")
//        flutterSdkPath
//    }
//    val fis = FileInputStream("local.properties")
//    prop.load(fis);
//    val flutterSdkPath = prop.getProperty("flutter.sdk")
//    flutterSdkPath
//    var properties = Properties()
//    def flutterSdkPath = {
//        def properties = new Properties()
//        file("local.properties").withInputStream { properties.load(it) }
//        def flutterSdkPath = properties.getProperty("flutter.sdk")
//        assert flutterSdkPath != null, "flutter.sdk not set in local.properties"
//        return flutterSdkPath
//    }()
//
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