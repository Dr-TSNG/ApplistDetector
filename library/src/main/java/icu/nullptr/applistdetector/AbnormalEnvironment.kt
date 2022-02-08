package icu.nullptr.applistdetector

import android.content.Context

class AbnormalEnvironment(context: Context) : IDetector(context) {

    override val name = "Abnormal Environment"

    private external fun detectXposed(): Boolean

    private external fun detectDual(): Boolean

    private fun detectFile(path: String): Result {
        var res = FileDetection.detect(path, true)
        if (res == Result.METHOD_UNAVAILABLE) res = FileDetection.detect(path, false)
        if (res == Result.FOUND) res = Result.SUSPICIOUS
        return res
    }

    override fun run(packages: Collection<String>?, detail: Detail?): Result {
        var result = Result.NOT_FOUND
        val add: (Pair<String, Result>) -> Unit = {
            result = result.coerceAtLeast(it.second)
            detail?.add(it)
        }
        add("Xposed hooks" to if (detectXposed()) Result.FOUND else Result.NOT_FOUND)
        add("Dual / Work profile" to if (detectDual()) Result.SUSPICIOUS else Result.NOT_FOUND)
        add("XPrivacyLua" to detectFile("/data/system/xlua"))
        add("TWRP" to detectFile("/storage/emulated/0/TWRP"))
        return result
    }
}
