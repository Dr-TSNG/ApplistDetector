val minSdkVer: Int by rootProject.extra
val targetSdkVer: Int by rootProject.extra
val compileSdkVer: Int by rootProject.extra
val ndkVer: String by rootProject.extra
val javaVer: JavaVersion by rootProject.extra

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "icu.nullptr.applistdetector.library"
    compileSdk = compileSdkVer
    ndkVersion = ndkVer

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
    implementation("com.android.tools.build:apkzlib:7.2.2")
    implementation("io.github.vvb2060.ndk:xposeddetector:2.2")
}
