package com.tsng.applistdetector.detections

import android.text.TextUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import com.tsng.applistdetector.detections.IDetector.Results

object PMCommand : IDetector {

    override val name = "pm list packages"
    override var status: Results? = null
    override var listGenerated: Set<String>? = null

    override fun runDetection(packageName: String): Results {
        if (listGenerated == null) generateList()
        return if (listGenerated!!.contains(packageName)) Results.FOUND else Results.NOT_FOUND
    }

    private fun generateList() {
        status = null
        val packages = mutableSetOf<String>()

        try {
            val p = Runtime.getRuntime().exec("pm list packages")
            val br = BufferedReader(InputStreamReader(p.inputStream, StandardCharsets.UTF_8))
            var line: String
            while (br.readLine().also { line = it } != null) {
                line = line.trim { it <= ' ' }
                if (line.length > 8) {
                    val prefix = line.substring(0, 8)
                    if (prefix.equals("package:", ignoreCase = true)) {
                        line = line.substring(8).trim { it <= ' ' }
                        if (!TextUtils.isEmpty(line)) packages.add(line)
                    }
                }
            }
            br.close()
            p.destroy()
            when (packages.size) {
                0 -> status = Results.PERMISSION_DENIED
                1 -> status = Results.SUSPICIOUS
            }
        } catch (e: Exception) {
            status = Results.PERMISSION_DENIED
        }

        listGenerated = packages
    }
}