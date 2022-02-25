package icu.nullptr.applistdetector

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

class PMSundryAPIs(context: Context, override val name: String) : IDetector(context) {

    //override val name = "PM Sundry APIs"

    private fun getPackageUid(name: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                context.packageManager.getPackageUid(name, 0)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
            }
        }
        return false
    }

    private fun getInstallSourceInfo(name: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                context.packageManager.getInstallSourceInfo(name)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
            }
        }
        return false
    }

    private fun getLaunchIntentForPackage(name: String): Boolean {
        return context.packageManager.getLaunchIntentForPackage(name) != null
    }

    private val sundries = listOf(
        ::getPackageUid, ::getInstallSourceInfo, ::getLaunchIntentForPackage
    )

    override fun run(packages: Collection<String>?, detail: Detail?): Result {
        if (packages == null) throw IllegalArgumentException("packages should not be null")

        var result = Result.NOT_FOUND
        for (packageName in packages) {
            val res =
                if (sundries.any { it(packageName) }) Result.FOUND
                else Result.NOT_FOUND
            result = result.coerceAtLeast(res)
            detail?.add(packageName to res)
        }
        return result
    }
}
