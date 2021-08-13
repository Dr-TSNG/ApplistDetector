package com.tsng.applistdetector.detections

import com.tsng.applistdetector.MyApplication.Companion.appContext
import com.tsng.applistdetector.detections.IDetector.Results

object PMGetInstalledApplications : IDetector {

    override val name = "pm getInstalledApplications"
    override var status: Results? = null
    override var listGenerated: Set<String>? = null

    override fun runDetection(packageName: String): Results {
        if (listGenerated == null) generateList()
        return if (listGenerated!!.contains(packageName)) Results.FOUND else Results.NOT_FOUND
    }

    private fun generateList() {
        status = null
        val packages = mutableSetOf<String>()

        for (pkg in appContext.packageManager.getInstalledApplications(0))
            packages.add(pkg.packageName)

        if (packages.size <= 1) status = Results.SUSPICIOUS
        listGenerated = packages
    }
}