buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
    }
}

plugins {
    kotlin("android") version "1.7.0" apply false
}

fun String.execute(currentWorkingDir: File = file("./")): String {
    val byteOut = java.io.ByteArrayOutputStream()
    exec {
        workingDir = currentWorkingDir
        commandLine = split("\\s".toRegex())
        standardOutput = byteOut
    }
    return String(byteOut.toByteArray()).trim()
}

val verCode by extra("git rev-list HEAD --count".execute().toInt())
var verName by extra("2.3")

val minSdkVer by extra(23)
val targetSdkVer by extra(33)
val ndkVer by extra("25.0.8775105")
val javaVer by extra(JavaVersion.VERSION_11)

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
