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
var verName by extra("0.8")

val minSdkVer by extra(23)
val targetSdkVer by extra(32)
val ndkVer by extra("23.1.7779620")
val javaVer by extra(JavaVersion.VERSION_11)

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
