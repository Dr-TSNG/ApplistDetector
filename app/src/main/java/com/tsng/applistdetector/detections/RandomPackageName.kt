package com.tsng.applistdetector.detections

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.android.tools.build.apkzlib.zip.ZFile
import com.tsng.applistdetector.MyApplication.Companion.appContext
import org.jf.dexlib2.Opcodes
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import java.io.File

object RandomPackageName : IDetector() {

    override val name = "magisk random package name"

    override fun execute() {
        results.clear()

        val pm = appContext.packageManager
        val intent = Intent(Intent.ACTION_MAIN)
        for (pkg in pm.queryIntentActivities(intent, PackageManager.MATCH_ALL)) {
            try {
                val pInfo = pm.getPackageInfo(pkg.activityInfo.packageName, 0)
                val aInfo = pInfo.applicationInfo
                if (aInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) continue
                val apkFile = File(aInfo.sourceDir)
                val apkSize = apkFile.length() / 1024
                if (apkSize < 20 || apkSize > 30) continue

                val zFile = ZFile.openReadOnly(apkFile)
                val dex = zFile["classes.dex"]?.read() ?: continue
                zFile.close()

                if (checkDex(dex)) results.add(Pair(pkg.activityInfo.packageName, Results.FOUND))
            } catch (e: Exception) {
            }
        }
    }

    private fun checkDex(dex: ByteArray): Boolean {
        val dexFile = DexBackedDexFile(Opcodes.getDefault(), dex)
        var hasCurrentApk = false
        var hasUpdateApk = false
        dexFile.stringReferences.forEach {
            when (it.string) {
                "current.apk" -> hasCurrentApk = true
                "update.apk" -> hasUpdateApk = true
            }
        }
        return hasCurrentApk && hasUpdateApk
    }
}