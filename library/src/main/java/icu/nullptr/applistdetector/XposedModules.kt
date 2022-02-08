package icu.nullptr.applistdetector

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class XposedModules(context: Context) : IDetector(context) {

    override val name = "Xposed Modules"

    @SuppressLint("QueryPermissionsNeeded")
    override fun run(packages: Collection<String>?, detail: Detail?): Result {
        var result = Result.NOT_FOUND
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        val set = if (detail == null) null else mutableSetOf<Pair<String, Result>>()
        for (pkg in pm.queryIntentActivities(intent, PackageManager.GET_META_DATA)) {
            val aInfo = pkg.activityInfo.applicationInfo
            if (aInfo.metaData?.get("xposedminversion") != null) {
                val label = pm.getApplicationLabel(aInfo) as String
                result = Result.FOUND
                set?.add(label to Result.FOUND)
            }
        }
        detail?.addAll(set!!)
        return result
    }
}
