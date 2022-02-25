package icu.nullptr.applistdetector

import android.content.Context

class AbnormalEnvironment(context: Context, private val subusybox: Boolean,
                          override val name: String
) : IDetector(context) {

    //override val name = if (subusybox) "SuBusybox File Detection" else "Abnormal Environment"

    private external fun detectXposed(): Boolean

    private fun detectDual(): Result{
        var filedir=context.filesDir.path
        return if(filedir.startsWith("/data/user")&& !filedir.startsWith("/data/user/0"))
            Result.SUSPICIOUS
        else Result.NOT_FOUND
    }

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
        if (!subusybox){
            add("Xposed hooks" to if (detectXposed()) Result.FOUND else Result.NOT_FOUND)
            add("Dual / Work profile" to detectDual())
            add(Pair("HMA (old version)", detectFile("/data/misc/hide_my_applist")))
            add("XPrivacyLua" to detectFile("/data/system/xlua"))
            add("TWRP" to detectFile("/storage/emulated/0/TWRP"))
            add(Pair("Xposed Edge", detectFile("/data/system/xedge")))
            add(Pair("Riru Clipboard", detectFile("/data/misc/clipboard")))
        }else{
        val places = arrayOf("/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/")
        for (where in places) {
            val sufile=where+"su"
            val busyboxfile=where+"busybox"
            add("SuFile("+sufile+")" to detectFile(sufile))
            add("BusyboxFile("+busyboxfile+")" to detectFile(busyboxfile))
        }}
        return result
}}
