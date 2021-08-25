package com.tsng.applistdetector.detections

abstract class IDetector {
    companion object {
        val basicAppList = listOf(
            "com.topjohnwu.magisk",
            "de.robv.android.xposed.installer",
            "org.meowcat.edxposed.manager",
            "org.lsposed.manager",
            "top.canyie.dreamland.manager",
            "me.weishu.exp"
        )
    }

    enum class Results {
        NOT_FOUND, PERMISSION_DENIED, SUSPICIOUS, FOUND
    }

    val results = mutableListOf<Pair<String, Results>>()

    abstract val name: String
    abstract fun execute()
}
