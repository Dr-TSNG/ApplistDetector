package icu.nullptr.applistdetector

import android.content.Context
import android.text.TextUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class PMCommand(context: Context) : IDetector(context) {

    override val name = "PM Command"

    override fun run(packages: Collection<String>?, detail: Detail?): Result {
        if (packages == null) throw IllegalArgumentException("packages should not be null")

        var result = Result.NOT_FOUND
        val list = mutableSetOf<String>()
        try {
            val p = Runtime.getRuntime().exec("pm list packages")
            BufferedReader(InputStreamReader(p.inputStream, StandardCharsets.UTF_8)).use { br ->
                var line = br.readLine()
                while (line != null) {
                    line = line.trim { it <= ' ' }
                    if (line.length > 8) {
                        val prefix = line.substring(0, 8)
                        if (prefix.equals("package:", true)) {
                            line = line.substring(8).trim { it <= ' ' }
                            if (!TextUtils.isEmpty(line)) list.add(line)
                        }
                    }
                    line = br.readLine()
                }
            }
            p.destroy()
            if (list.size == 0) result = Result.METHOD_UNAVAILABLE
            if (list.size == 1) result = Result.SUSPICIOUS
        } catch (e: Exception) {
            e.printStackTrace()
            result = Result.METHOD_UNAVAILABLE
        }

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
