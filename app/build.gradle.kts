val verCode: Int by rootProject.extra
val verName: String by rootProject.extra

val minSdkVer: Int by rootProject.extra
val targetSdkVer: Int by rootProject.extra
val ndkVer: String by rootProject.extra
val javaVer: JavaVersion by rootProject.extra

val composeVersion = "1.2.0-alpha03"

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

        signingConfigs.create("config") {
            val androidStoreFile = project.findProperty("fileDir") as String?
            if (!androidStoreFile.isNullOrEmpty()) {
                storeFile = file(androidStoreFile)
                storePassword = project.property("storePassword") as String
                keyAlias = project.property("keyAlias") as String
                keyPassword = project.property("keyPassword") as String
            }
        }
    }

    buildTypes {
        debug {
            signingConfig = if (signingConfigs["config"].storeFile != null) signingConfigs["config"] else signingConfigs["debug"]
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            signingConfig = if (signingConfigs["config"].storeFile != null) signingConfigs["config"] else signingConfigs["debug"]
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
    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")
}
