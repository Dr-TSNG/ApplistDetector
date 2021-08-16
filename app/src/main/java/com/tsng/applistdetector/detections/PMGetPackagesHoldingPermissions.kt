package com.tsng.applistdetector.detections

import android.Manifest.permission
import com.tsng.applistdetector.MyApplication.Companion.appContext
import com.tsng.applistdetector.MyApplication.Companion.detectionAppList

object PMGetPackagesHoldingPermissions : IDetector() {

    override val name = "pm getPackagesHoldingPermissions"

    override fun execute() {
        results.clear()
        var status: Results? = null
        val packages = mutableSetOf<String>()

        val permissions = mutableListOf<String>()
        for (field in permission::class.java.fields) {
            try {
                if (field.type == String::class.java) permissions.add(field[null] as String)
            } catch (ignored: Exception) {
            }
        }
        for (pkg in appContext.packageManager.getPackagesHoldingPermissions(permissions.toTypedArray(), 0))
            packages.add(pkg.packageName)
        if (packages.size <= 1) status = Results.SUSPICIOUS

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