package icu.nullptr.applistdetector

import android.annotation.SuppressLint
import android.content.Context

class FileDetection(context: Context, private val useSyscall: Boolean) : IDetector(context) {

    override val name = if (useSyscall) "Syscall File Detection" else "Libc File Detection"

    companion object {
        @JvmStatic
        private external fun nativeDetect(path: String, useSyscall: Boolean): Int

        @JvmStatic
        fun detect(path: String, useSyscall: Boolean) = Result.values()[nativeDetect(path, useSyscall)]
    }

    @SuppressLint("SdCardPath")
    override fun run(packages: Collection<String>?, detail: Detail?): Result {
        if (packages == null) throw IllegalArgumentException("packages should not be null")

        var result = Result.NOT_FOUND
        for (packageName in packages) {
            val res = maxOf(
                Result.NOT_FOUND,
                detect("/data/data/$packageName", useSyscall),
                detect("/storage/emulated/0/Android/data/$packageName", useSyscall),
                detect("/storage/emulated/0/Android/media/$packageName", useSyscall),
                detect("/storage/emulated/0/Android/obb/$packageName", useSyscall)
            )
            result = result.coerceAtLeast(res)
            detail?.add(packageName to res)
        }
        return result
    }
}
