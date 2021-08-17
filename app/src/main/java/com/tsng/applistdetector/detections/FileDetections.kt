package com.tsng.applistdetector.detections

import android.annotation.SuppressLint
import com.tsng.applistdetector.MyApplication.Companion.detectionAppList

class FileDetections(private val useSyscall: Boolean) : IDetector() {
    override val name = (if (useSyscall) "syscall" else "libc") + " file detection"

    @SuppressLint("SdCardPath")
    override fun execute() {
        for (packageName in detectionAppList) {
            val res = listOf(
                detect("/data/data/$packageName", useSyscall),
                detect("/storage/emulated/0/Android/data/$packageName", useSyscall),
                detect("/storage/emulated/0/Android/media/$packageName", useSyscall),
                detect("/storage/emulated/0/Android/obb/$packageName", useSyscall)
            )
            var result = Results.NOT_FOUND
            res.forEach { if (result < it) result = it }
            results.add(Pair(packageName, result))
        }
    }

    private external fun detect(path: String, useSyscall: Boolean): Results
}