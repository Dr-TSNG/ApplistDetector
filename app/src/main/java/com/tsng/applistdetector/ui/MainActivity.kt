package com.tsng.applistdetector.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tsng.applistdetector.MyApplication.Companion.detectionAppList
import com.tsng.applistdetector.R
import com.tsng.applistdetector.detections.*
import com.tsng.applistdetector.ui.components.FoldLayout
import com.tsng.applistdetector.ui.theme.AppTheme
import kotlin.concurrent.thread


@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {

    class TagData(
        var status: FoldLayout.Status,
        var targets: MutableList<Pair<String, IDetector.Results>>
    )

    private val detectors = listOf(
        PMCommand,
        PMGetInstalledPackages,
        PMGetInstalledApplications,
        PMGetPackagesHoldingPermissions,
        PMQueryIntentActivities,
        PMGetPackageUid
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detectionAppList =
            getPreferences(MODE_PRIVATE).getStringSet("appList", null)?.toList() ?: IDetector.basicAppList
        setContent {
            AppTheme {
                val data = remember {
                    mutableStateListOf<TagData>()
                }
                for (detector in detectors) {
                    val tagData = TagData(FoldLayout.Status.NotStarted, mutableListOf())
                    data.add(tagData)
                }

                Column {
                    TopAppBar(
                        modifier = Modifier.padding(bottom = 4.dp),
                        title = { Text(stringResource(id = R.string.app_name)) },
                        navigationIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = null
                            )
                        }
                    )
                    LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
                        for (i in detectors.indices) {
                            item {
                                FoldLayout(
                                    status = data[i].status,
                                    title = detectors[i].name,
                                    list = data[i].targets
                                )
                            }
                        }
                    }
                }

                thread {
                    for (i in detectors.indices) {
                        runOnUiThread {
                            data[i] = TagData(FoldLayout.Status.Loading, mutableListOf())
                        }

                        detectors[i].execute()

                        runOnUiThread {
                            data[i] = TagData(FoldLayout.Status.Completed, detectors[i].results)
                        }
                    }
                }
            }
        }
    }
}