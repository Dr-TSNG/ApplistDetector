package com.tsng.applistdetector.detections

import android.content.Intent
import android.content.pm.PackageManager
import com.tsng.applistdetector.MyApplication.Companion.appContext
import com.tsng.applistdetector.MyApplication.Companion.detectionAppList

object PMQueryIntentActivities : IDetector() {
    override val name = "pm queryIntentActivities"

    override fun execute() {
        results.clear()
        var status: Results? = null
        val packages = mutableSetOf<String>()

        val intent = Intent(Intent.ACTION_MAIN)
        for (pkg in appContext.packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL))
            packages.add(pkg.activityInfo.packageName)
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