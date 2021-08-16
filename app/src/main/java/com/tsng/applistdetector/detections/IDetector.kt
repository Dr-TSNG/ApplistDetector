package com.tsng.applistdetector.detections

import com.tsng.applistdetector.MyApplication.Companion.detectionAppList

interface IDetector {
    companion object {
        val basicAppList = listOf(
            "com.topjohnwu.magisk",
            "de.robv.android.xposed.installer",
            "org.meowcat.edxposed.manager",
            "org.lsposed.manager",
            "me.weishu.exp",
            "moe.shizuku.redirectstorage"
        )
    }

    enum class Results {
        NOT_FOUND, PERMISSION_DENIED, SUSPICIOUS, FOUND
    }

    val name: String
    var status: Results?
    var listGenerated: Set<String>?

    fun runDetection(packageName: String): Results
}