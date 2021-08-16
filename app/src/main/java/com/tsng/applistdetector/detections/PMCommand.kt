package com.tsng.applistdetector.detections

import android.text.TextUtils
import com.tsng.applistdetector.MyApplication.Companion.detectionAppList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

object PMCommand : IDetector() {

    override val name = "pm list packages"

    override fun execute() {
        results.clear()
        var status: Results? = null
        val packages = mutableSetOf<String>()

        try {
            val p = Runtime.getRuntime().exec("pm list packages")
            val br = BufferedReader(InputStreamReader(p.inputStream, StandardCharsets.UTF_8))
            var line = br.readLine()
            while (line != null) {
                line = line.trim { it <= ' ' }
                if (line.length > 8) {
                    val prefix = line.substring(0, 8)
                    if (prefix.equals("package:", true)) {
                        line = line.substring(8).trim { it <= ' ' }
                        if (!TextUtils.isEmpty(line)) packages.add(line)
                    }
                }
                line = br.readLine()
            }
            br.close()
            p.destroy()
            when (packages.size) {
                0 -> status = Results.PERMISSION_DENIED
                1 -> status = Results.SUSPICIOUS
            }
        } catch (e: Exception) {
            e.printStackTrace()
            status = Results.PERMISSION_DENIED
        }

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