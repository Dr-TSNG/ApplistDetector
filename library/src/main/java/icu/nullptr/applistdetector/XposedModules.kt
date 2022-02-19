package icu.nullptr.applistdetector

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class XposedModules(context: Context) : IDetector(context) {

    override val name = "Xposed Modules"

    @SuppressLint("QueryPermissions OR PMCAPermissions Needed")
    override fun run(packages: Collection<String>?, detail: Detail?): Result {
        if (packages != null) throw IllegalArgumentException("packages should be null")

        var result = Result.NOT_FOUND
        val pm = context.packageManager
        val set = if (detail == null) null else mutableSetOf<Pair<String, Result>>()
        val intent = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        for (pkg in intent) {
            if (pkg.metaData?.get("xposedmodule") != null){
                val label = pm.getApplicationLabel(pkg) as String
                result = Result.FOUND
                set?.add(label to Result.FOUND)}
        }
        if (set.isNullOrEmpty()){
            val intent = pm.queryIntentActivities(Intent(Intent.ACTION_MAIN),PackageManager.GET_META_DATA)
            for (pkg in intent) {
                val ainfo=pkg.activityInfo.applicationInfo
                if (ainfo.metaData?.get("xposedminversion") != null) {
                    val label = pm.getApplicationLabel(ainfo) as String
                    result = Result.FOUND
                    set?.add(label to Result.FOUND)
                }
            }
        }
        detail?.addAll(set!!)
        return result
    }
}
