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

        tasks.register<JavaCompile>("javacpp_CompileConfigFile$variantName") {
            source(javaCompile.source)
            include("com/example/faceod/uvc/presets/uvc.java")
            classpath = javaCompile.classpath
            destinationDirectory = javaCompile.destinationDirectory
        }

        tasks.register<org.bytedeco.gradle.javacpp.BuildTask>("javacpp_ParseCppHeader$variantName") {
            dependsOn("javacpp_CompileConfigFile$variantName")
            classPath = arrayOf(javaCompile.destinationDirectory.get().asFile.path)
            includePath = arrayOf(
                "$projectDir/src/main/jni/src/libusb",
                "$projectDir/src/main/jni/src/libuvc/include",
                "$projectDir/src/main/jni/include"
            )
            classOrPackageNames = arrayOf("com.example.faceod.uvc.presets.uvc") // produced by last step
            outputDirectory = File("$projectDir/src/main/java/gen")

//            properties
////            platform
//
//             println(properties)
//
//            propertyKeysAndValues = Properties().apply {
//                put("platform.root", "")
//                put("platform.compiler", "")
//
//
//                "platform.compiler" to ""
////                'platform.root' : System.getProperty('user.home') + '/Android/Sdk/ndk-bundle/',
////                'platform.compiler' : "$buildDir/$javacppPlatform/toolchain/bin/clang++".toString() ]
//
//            }
        }

        javaCompile.dependsOn("javacpp_ParseCppHeader$variantName")

        tasks.register<org.bytedeco.gradle.javacpp.BuildTask>("javacpp_BuildCompiler$variantName")  {
            dependsOn(javaCompile)
            classPath = arrayOf(javaCompile.destinationDirectory.get().asFile.path)
            classOrPackageNames = arrayOf("com.example.faceod.uvc")
            compile = false
            deleteJniFiles = false
//            outputDirectory = File("$projectDir/src/main/cpp/")
        }

    }
}

javacppBuild {
}

dependencies {
    implementation("org.bytedeco:javacpp:1.5.10")
//    implementation("org.bytedeco:javacpp-platform:1.5.10:android-arm64")
//    implementation("org.bytedeco:javacpp-platform:1.5.10:android-x86_64")
//    implementation("org.bytedeco:systems:1.5.10:android-arm64")
//    implementation("org.bytedeco:systems:1.5.10:android-x86_64")
//    implementation("org.bytedeco:systems-platform:1.5.10:android-arm64")
//    implementation("org.bytedeco:systems-platform:1.5.10:android-x86_64")
}

flutter {
    source = "../.."
}
