buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.1")
    }
}

plugins {
    kotlin("android") version "1.6.10" apply false
}

val verCode by extra(12)
var verName by extra("0.1")

val minSdkVer by extra(23)
val targetSdkVer by extra(32)
val ndkVer by extra("23.1.7779620")
val javaVer by extra(JavaVersion.VERSION_11)

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
