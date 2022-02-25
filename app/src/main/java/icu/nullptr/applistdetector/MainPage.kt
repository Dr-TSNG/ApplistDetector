package icu.nullptr.applistdetector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import icu.nullptr.applistdetector.MyApplication.Companion.accList
import icu.nullptr.applistdetector.MyApplication.Companion.accenable
import icu.nullptr.applistdetector.MyApplication.Companion.appContext
import icu.nullptr.applistdetector.MyApplication.Companion.titletext
import icu.nullptr.applistdetector.component.CheckCard
import icu.nullptr.applistdetector.component.IconHintCard
import kotlinx.coroutines.*

val basicAppList = listOf(
    "com.topjohnwu.magisk",
    "io.github.vvb2060.magisk",
    "de.robv.android.xposed.installer",
    "org.meowcat.edxposed.manager",
    "org.lsposed.manager",
    "top.canyie.dreamland.manager",
    "me.weishu.exp",
    "com.android.vendinf",
    "moe.shizuku.redirectstorage"
)

val snapShotList = mutableStateListOf<Triple<IDetector, IDetector.Result?, Detail?>>(
    Triple(AbnormalEnvironment(appContext,false,titletext[0]), null, null),
    Triple(AbnormalEnvironment(appContext,true,"SuBusybox "+titletext[1]), null, null),

    Triple(PMCommand(appContext,titletext[2]), null, null),
    Triple(PMConventionalAPIs(appContext,titletext[3]), null, null),
    Triple(PMSundryAPIs(appContext,titletext[4]), null, null),
    Triple(PMQueryIntentActivities(appContext,titletext[5]), null, null),

    Triple(FileDetection(appContext, false,"Libc "+titletext[1]), null, null),
    Triple(FileDetection(appContext, true,"Syscall "+titletext[1]), null, null),

    Triple(XposedModules(appContext,titletext[6]), null, null),
    Triple(MagiskRandomPackageName(appContext,titletext[7]), null, null),
    Triple(Accessibility(appContext, accList = accList, accenable = accenable,titletext[8]), null, null)
)

suspend fun runDetector(id: Int, packages: Collection<String>?) {
    withContext(Dispatchers.IO) {
        val detail = mutableListOf<Pair<String, IDetector.Result>>()
        val result = snapShotList[id].first.run(packages, detail)
        snapShotList[id] = Triple(snapShotList[id].first, result, detail)
    }
}

@Composable
fun MainPage() {
    LaunchedEffect(appContext) {
        runDetector(0, null)
        runDetector(1, null)
        for (i in 2..7) runDetector(i, basicAppList)
        runDetector(8, null)
        runDetector(9, null)
        runDetector(10, null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconHintCard()
        snapShotList.forEach {
            CheckCard(
                title = it.first.name,
                result = it.second,
                detail = it.third
            )
        }
    }
}
