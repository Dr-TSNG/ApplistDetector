val verCode: Int by rootProject.extra
val verName: String by rootProject.extra

val minSdkVer: Int by rootProject.extra
val targetSdkVer: Int by rootProject.extra
val ndkVer: String by rootProject.extra
val javaVer: JavaVersion by rootProject.extra

val composeVersion = "1.2.0-alpha02"

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = targetSdkVer
    ndkVersion = ndkVer

    buildFeatures {
        compose = true
        prefab = true
    }

    defaultConfig {
        applicationId = "icu.nullptr.applistdetector"
        minSdk = minSdkVer
        targetSdk = targetSdkVer
        versionCode = verCode
        versionName = verName
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
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
    implementation(project(":library"))

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.material:material-icons-extended:1.0.5")
    implementation("androidx.compose.material3:material3:1.0.0-alpha04")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("com.google.android.material:material:1.5.0")
}
