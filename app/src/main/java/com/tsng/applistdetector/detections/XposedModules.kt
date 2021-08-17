package com.tsng.applistdetector.detections

import android.content.Intent
import android.content.pm.PackageManager
import com.tsng.applistdetector.MyApplication.Companion.appContext

object XposedModules : IDetector() {

    override val name = "find xposed modules"

    override fun execute() {
        results.clear()
        val packages = mutableSetOf<String>()

        val pm = appContext.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        for (pkg in pm.queryIntentActivities(intent, PackageManager.GET_META_DATA)) {
            val aInfo = pkg.activityInfo.applicationInfo
            if (aInfo.metaData?.get("xposedmodule") != null)
                packages.add(pm.getApplicationLabel(aInfo) as String)
        }

        packages.forEach { results.add(Pair(it, Results.FOUND)) }
    }
}