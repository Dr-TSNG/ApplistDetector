package com.tsng.applistdetector.detections

import android.Manifest.permission
import com.tsng.applistdetector.MyApplication.Companion.appContext
import com.tsng.applistdetector.detections.IDetector.Results

object PMGetPackagesHoldingPermissions : IDetector {

    override val name = "pm getPackagesHoldingPermissions"
    override var status: Results? = null
    override var listGenerated: Set<String>? = null

    override fun runDetection(packageName: String): Results {
        if (listGenerated == null) generateList()
        return if (listGenerated!!.contains(packageName)) Results.FOUND else Results.NOT_FOUND
    }

    private fun generateList() {
        status = null
        val packages = mutableSetOf<String>()

        val permissions: MutableList<String> = ArrayList()
        for (field in permission::class.java.fields) {
            try {
                if (field.type == String::class.java) permissions.add(field[null] as String)
            } catch (ignored: Exception) {
            }
        }
        for (pkg in appContext.packageManager.getPackagesHoldingPermissions(permissions.toTypedArray(), 0))
            packages.add(pkg.packageName)

        if (packages.size <= 1) status = Results.SUSPICIOUS
        listGenerated = packages
    }
}