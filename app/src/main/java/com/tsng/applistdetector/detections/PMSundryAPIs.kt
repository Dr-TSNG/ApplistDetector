package com.tsng.applistdetector.detections

import android.content.pm.PackageManager
import android.os.Build
import com.tsng.applistdetector.MyApplication.Companion.appContext
import com.tsng.applistdetector.MyApplication.Companion.detectionAppList

object PMSundryAPIs : IDetector() {

    override val name = "pm sundry APIs"

    private val sundries = arrayOf<(String) -> Boolean>(
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    appContext.packageManager.getPackageUid(it, 0)
                    return@arrayOf true
                } catch (e: PackageManager.NameNotFoundException) {
                }
            }
            return@arrayOf false
        },
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    appContext.packageManager.getInstallSourceInfo(it)
                    return@arrayOf true
                } catch (e: PackageManager.NameNotFoundException) {
                }
            }
            return@arrayOf false
        },
        {
            return@arrayOf appContext.packageManager.getLaunchIntentForPackage(it) != null
        }
    )

    override fun execute() {
        results.clear()

        for (packageName in detectionAppList) {
            var result = Results.NOT_FOUND
            for (method in sundries) {
                if (method(packageName)) {
                    result = Results.FOUND
                    break
                }
            }
            results.add(Pair(packageName, result))
        }
    }
}