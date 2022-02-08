package icu.nullptr.applistdetector

import android.content.Context

typealias Detail = MutableCollection<Pair<String, IDetector.Result>>

abstract class IDetector(protected val context: Context) {

    enum class Result {
        NOT_FOUND, METHOD_UNAVAILABLE, SUSPICIOUS, FOUND
    }

    abstract val name: String

    abstract fun run(packages: Collection<String>?, detail: Detail?): Result
}
