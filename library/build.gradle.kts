val minSdkVer: Int by rootProject.extra
val targetSdkVer: Int by rootProject.extra
val ndkVer: String by rootProject.extra
val javaVer: JavaVersion by rootProject.extra

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "icu.nullptr.applistdetector.library"
    compileSdk = targetSdkVer

    buildFeatures {
        prefab = true
    }

    defaultConfig {
        minSdk = minSdkVer
        targetSdk = targetSdkVer

        externalNativeBuild.ndkBuild {
            arguments += "-j${Runtime.getRuntime().availableProcessors()}"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            consumerProguardFiles("proguard-rules.pro")
        }
    }

    externalNativeBuild.ndkBuild {
        path("src/main/cpp/Android.mk")
    }

    compileOptions {
        sourceCompatibility = javaVer
        targetCompatibility = javaVer
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("com.android.tools.build:apkzlib:7.1.1")
    implementation("de.upb.cs.swt:axml:2.1.2")
    implementation("dev.rikka.ndk.thirdparty:cxx:1.2.0")
    implementation("io.github.vvb2060.ndk:xposeddetector:2.2")
}
