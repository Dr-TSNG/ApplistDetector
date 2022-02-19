package icu.nullptr.applistdetector

import android.content.Context

/**
 *Created by byxiaorun on 2022/2/20/0020.
 */
class Accessibility (context: Context,var accList: List<String>,
                     var accenable:Boolean = false) : IDetector(context) {
    override val name = "accessibility checker"
    override fun run(packages: Collection<String>?, detail: Detail?): Result {
        var result = Result.NOT_FOUND
        val add: (Pair<String, Result>) -> Unit = {
            result = result.coerceAtLeast(it.second)
            detail?.add(it)
        }
        if (accenable==true){
            add(Pair("AccessibilitySERVICES.isEnabled", Result.FOUND))

        }else{
            add(Pair("AccessibilitySERVICES", Result.NOT_FOUND))
        }
        if (accList.isNotEmpty()) {
            accList.forEach { add(Pair(it, Result.FOUND)) }
        } else {
            add(Pair("AccessibilityList", Result.NOT_FOUND))
        }
        return result
    }

}