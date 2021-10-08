package com.tsng.applistdetector.detections

import com.tsng.applistdetector.MyApplication.Companion.appContext

object AbnormalEnvironment : IDetector() {

    override val name = "abnormal environment"

    override fun execute() {
        results.clear()
        results.add(Pair("Xposed hooks", if (xposedDetector()) Results.FOUND else Results.NOT_FOUND))
        results.add(Pair("Dual / Work profile", detectDual()))
        results.add(Pair("XPrivacyLua", detectFile("/data/system/xlua")))
        results.add(Pair("Thanox", detectFile("/data/system/thanos")))
    }

    private fun detectFile(path: String): Results {
        var res = FileDetections.detect(path, true)
        if (res == Results.PERMISSION_DENIED) res = FileDetections.detect(path, false)
        if (res == Results.FOUND) res = Results.SUSPICIOUS
        return res
    }

    private fun detectDual(): Results {
        val filesDir = appContext.filesDir.path
        return if (filesDir.startsWith("/data/user") && !filesDir.startsWith("/data/user/0/"))
            Results.SUSPICIOUS
        else Results.NOT_FOUND
    }

    private external fun xposedDetector(): Boolean
}