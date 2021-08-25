package com.tsng.applistdetector.detections

import android.app.admin.DevicePolicyManager
import com.tsng.applistdetector.MyApplication.Companion.appContext


object AbnormalEnvironment : IDetector() {

    override val name = "abnormal environment"

    override fun execute() {
        results.clear()
        results.add(Pair("Xposed hooks", if (xposedDetector()) Results.FOUND else Results.NOT_FOUND))
        results.add(Pair("Dual", detectDual()))
        results.add(Pair("Work profile", detectWorkProfile()))
        results.add(Pair("XPrivacyLua", detectFile("/data/system/xlua")))
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

    private fun detectWorkProfile(): Results {
        val devicePolicyManager = appContext.getSystemService(DevicePolicyManager::class.java)
        val activeAdmins = devicePolicyManager.activeAdmins
        return if (activeAdmins != null) Results.SUSPICIOUS else Results.NOT_FOUND
    }

    private external fun xposedDetector(): Boolean
}