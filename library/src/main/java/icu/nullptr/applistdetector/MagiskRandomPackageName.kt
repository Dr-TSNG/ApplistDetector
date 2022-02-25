package icu.nullptr.applistdetector

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.android.tools.build.apkzlib.zip.ZFile
import pxb.android.axml.AxmlParser
import java.io.File

private val permissionList = arrayOf(
    "android.permission.USE_BIOMETRIC",
    "android.permission.USE_FINGERPRINT",
    "com.android.launcher.permission.INSTALL_SHORTCUT",
    "android.permission.REQUEST_INSTALL_PACKAGES",
    "android.permission.QUERY_ALL_PACKAGES"
)

class MagiskRandomPackageName(context: Context, override val name: String) : IDetector(context) {

    //override val name = "Random Package Name"

    private fun checkManifest(zFile: ZFile): Boolean {
        val manifestEntry = zFile.get("AndroidManifest.xml") ?: return false
        var permissionMatch = 0
        var launcherMatch = false

        val parser = AxmlParser(manifestEntry.open().use { it.readBytes() })
        var event = parser.next()
        while (event != AxmlParser.END_FILE) {
            if (event == AxmlParser.START_TAG) {
                when (parser.name) {
                    "uses-permission" -> {
                        for (i in 0 until parser.attrCount) {
                            if (parser.getAttrName(i) == "name") {
                                if (parser.getAttrValue(i) in permissionList) permissionMatch++
                                break
                            }
                        }
                    }
                    "category" -> {
                        for (i in 0 until parser.attrCount) {
                            if (parser.getAttrName(i) == "name") {
                                if (parser.getAttrValue(i) == "android.intent.category.LAUNCHER") launcherMatch = true
                                break
                            }
                        }
                    }
                }
            }
            event = parser.next()
        }

        return permissionMatch == permissionList.size && launcherMatch
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun run(packages: Collection<String>?, detail: Detail?): Result {
        if (packages != null) throw IllegalArgumentException("packages should be null")

        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        for (pkg in pm.queryIntentActivities(intent, PackageManager.MATCH_ALL)) {
            try {
                val pInfo = pm.getPackageInfo(pkg.activityInfo.packageName, 0)
                val aInfo = pInfo.applicationInfo
                if (aInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) continue
                val apkFile = File(aInfo.sourceDir)
                val apkSize = apkFile.length() / 1024
                if (apkSize < 20 || apkSize > 30) continue
                var traces = 0
                ZFile.openReadOnly(apkFile).use {
                    if (checkManifest(it)) traces++
                }
                if (traces == 1) {
                    detail?.add(pkg.activityInfo.packageName to Result.FOUND)
                    return Result.FOUND
                }
            } catch (e: Exception) {
            }
        }
        return Result.NOT_FOUND
    }
}
