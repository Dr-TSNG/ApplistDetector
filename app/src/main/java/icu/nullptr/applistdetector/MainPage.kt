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
import icu.nullptr.applistdetector.MyApplication.Companion.appContext
import icu.nullptr.applistdetector.component.CheckCard
import icu.nullptr.applistdetector.component.IconHintCard
import kotlinx.coroutines.*

val basicAppList = listOf(
    "com.topjohnwu.magisk",
    "de.robv.android.xposed.installer",
    "org.meowcat.edxposed.manager",
    "org.lsposed.manager",
    "top.canyie.dreamland.manager",
    "me.weishu.exp",
    "com.android.vendinf",
    "moe.shizuku.redirectstorage"
)

val snapShotList = mutableStateListOf<Triple<IDetector, IDetector.Result?, Detail?>>(
    Triple(AbnormalEnvironment(appContext), null, null),

    Triple(PMCommand(appContext), null, null),
    Triple(PMConventionalAPIs(appContext), null, null),
    Triple(PMSundryAPIs(appContext), null, null),
    Triple(PMQueryIntentActivities(appContext), null, null),

    Triple(FileDetection(appContext, false), null, null),
    Triple(FileDetection(appContext, true), null, null),

    Triple(XposedModules(appContext), null, null),
    Triple(MagiskRandomPackageName(appContext), null, null)
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
        for (i in 1..6) runDetector(i, basicAppList)
        runDetector(7, null)
        runDetector(8, null)
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
