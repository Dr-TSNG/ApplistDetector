package com.tsng.applistdetector.detections

import android.content.pm.PackageManager
import android.os.Build
import com.tsng.applistdetector.MyApplication.Companion.appContext
import com.tsng.applistdetector.MyApplication.Companion.detectionAppList

object PMGetPackageUid : IDetector() {
    override val name = "pm getPackageUid"

    override fun execute() {
        results.clear()

        for (packageName in detectionAppList) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    appContext.packageManager.getPackageUid(packageName, 0)
                    results.add(Pair(packageName, Results.FOUND))
                } catch (e: PackageManager.NameNotFoundException) {
                    results.add(Pair(packageName, Results.NOT_FOUND))
                }
            } else {
                results.add(Pair(packageName, Results.PERMISSION_DENIED))
            }
        }
    }
}