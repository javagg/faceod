import com.android.build.gradle.internal.tasks.NdkTask
import org.bytedeco.gradle.javacpp.BuildTask
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
    id("org.bytedeco.gradle-javacpp-build") version "1.5.10"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
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
        jvmTarget = "17"
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
        ndk.abiFilters += listOf("x86_64", "arm64-v8a")

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

    applicationVariants.all {
        val variantName = name.capitalize()
        val javaCompile = tasks.getByName<JavaCompile>("compile${variantName}JavaWithJavac")

        tasks.register<JavaCompile>("javacppCompileJava$variantName") {
            source(javaCompile.source)
            include("com/example/faceod/uvc/presets/uvc.java")
            classpath = javaCompile.classpath
            destinationDirectory = javaCompile.destinationDirectory
        }

        tasks.register<BuildTask>("javacppBuildParser$variantName") {
            dependsOn("javacppCompileJava$variantName")
            classPath = arrayOf(javaCompile.destinationDirectory.get().asFile.path)
            includePath = arrayOf(
                "$projectDir/src/main/jni/src/libuvc/include",
                "$projectDir/src/main/jni/include"
            )
            classOrPackageNames = arrayOf("com.example.faceod.uvc.presets.uvc") // produced by last step
            outputDirectory = File("$projectDir/src/main/jni/generated")
        }

        javaCompile.dependsOn("javacppBuildParser$variantName")

        tasks.register<BuildTask>("javacppBuildCompiler$variantName") {
            dependsOn(javaCompile)
            classPath = arrayOf(javaCompile.destinationDirectory.get().asFile.path)
            classOrPackageNames = arrayOf("com.example.faceod.uvc.global.uvc")
            includePath = arrayOf(
                "$projectDir/src/main/jni/src/libusb/libusb",
            )
            compile = false
            deleteJniFiles = false
            outputDirectory = file("$projectDir/src/main/jni/generated")
            properties = "android"
        }
        // Picks up the C++ files listed in CMakeLists.txt
        tasks.getByName("externalNativeBuild$variantName").dependsOn("javacppBuildCompiler$variantName")
    }
}

dependencies {
    implementation("org.bytedeco:javacpp:1.5.10")
}

flutter {
    source = "../.."
}
