plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
    id("org.bytedeco.gradle-javacpp-build") version "1.5.10"
}

android {
    namespace = "com.example.faceod"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17" //JavaVersion.VERSION_17
    }

    defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId = "com.example.faceod"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName

        externalNativeBuild {}
        ndk {
            abiFilters += listOf("x86_64", "arm64-v8a")
        }
    }

    sourceSets.getByName("main") {
        java.srcDir("src/main/jni/generated")
    }

    buildTypes {
        release {
            // TODO: Add your own signing config for the release build.
            // Signing with the debug keys for now, so `flutter run --release` works.
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    externalNativeBuild {
        cmake {
            path = File("src/main/jni/CMakeLists.txt")
        }
    }
}

dependencies {
    api("org.bytedeco:javacpp:1.5.10")
}

//android.applicationVariants.all { variant ->
//    def variantName = variant.name.capitalize()
//    def javaCompile = project.tasks.getByName("compile${variantName}JavaWithJavac")
//    def configureCMake = project.tasks.findAll {
//        it.name.startsWith("configureCMake$variantName")
//    }
//
//    task "javacpp_CompileConfigFile$variantName"(type: JavaCompile) {
//        include('com/example/faceod/uvc/UvcCameraConfig.java')
//        source(javaCompile.source)
//        destinationDir(javaCompile.destinationDir)
//        classpath = javaCompile.classpath
//    }
//
//    println(javaCompile.destinationDir)
//    task "javacpp_ParseCppHeader$variantName"(type: org.bytedeco.gradle.javacpp.BuildTask) {
//        dependsOn("javacpp_CompileConfigFile$variantName")
//        classPath = [javaCompile.destinationDir]
//        includePath = [
//                "$projectDir/src/main/jni/src/libuvc/include",
//                "$projectDir/src/main/jni/include"
//        ]
//        classOrPackageNames = ['com.example.faceod.uvc.UvcCameraConfig'] // produced by last step
//    }
//
//    javaCompile.dependsOn "javacpp_ParseCppHeader$variantName"
//
//    // Generates jnijavacpp.cpp and jniNativeLibrary.cpp
//   task "javacpp_BuildCompiler$variantName"(type: org.bytedeco.gradle.javacpp.BuildTask) {
//       dependsOn javaCompile
//       classPath = [javaCompile.destinationDir]
//       classOrPackageNames = ['com.example.faceod.uvc.UvcCamera']
//       compile = false
//       deleteJniFiles = false
//       outputDirectory = file("$projectDir/src/main/cpp/")
//   }
//
//    // Picks up the C++ files listed in CMakeLists.txt
////    configureCMake.forEach {
////        it.dependsOn "javacppBuildCompiler$variantName"
////    }
//}

flutter {
    source = "../.."
}
