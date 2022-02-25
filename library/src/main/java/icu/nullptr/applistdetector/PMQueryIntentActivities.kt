package icu.nullptr.applistdetector

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class PMQueryIntentActivities(context: Context, override val name: String) : IDetector(context) {

    //override val name = "PM Intent Queries"

    @SuppressLint("QueryPermissionsNeeded")
    override fun run(packages: Collection<String>?, detail: Detail?): Result {
        if (packages == null) throw IllegalArgumentException("packages should not be null")

        var result = Result.NOT_FOUND
        val list = mutableSetOf<String>()

        val intent = Intent(Intent.ACTION_MAIN)
        for (pkg in context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL))
            list.add(pkg.activityInfo.packageName)
        if (list.size == 0) result = Result.METHOD_UNAVAILABLE
        if (list.size == 1) result = Result.SUSPICIOUS

        for (packageName in packages) {
            val res =
                if (list.contains(packageName)) Result.FOUND
                else Result.NOT_FOUND
            result = result.coerceAtLeast(res)
            detail?.add(packageName to res)
        }
        return result
    }
}
