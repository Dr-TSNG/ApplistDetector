package com.tsng.applistdetector.detections

import com.tsng.applistdetector.MyApplication.Companion.appContext
import com.tsng.applistdetector.MyApplication.Companion.detectionAppList

object PMGetInstalledApplications : IDetector() {

    override val name = "pm getInstalledApplications"

    override fun execute() {
        results.clear()
        var status: Results? = null
        val packages = mutableSetOf<String>()

        for (pkg in appContext.packageManager.getInstalledApplications(0))
            packages.add(pkg.packageName)
        if (packages.size == 0) status = Results.PERMISSION_DENIED
        if (packages.size == 1) status = Results.SUSPICIOUS

        for (packageName in detectionAppList) {
            val result = when {
                status != null -> status
                packages.contains(packageName) -> Results.FOUND
                else -> Results.NOT_FOUND
            }
            results.add(Pair(packageName, result))
        }
    }
}