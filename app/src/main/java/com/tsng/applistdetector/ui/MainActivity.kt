package com.tsng.applistdetector.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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


@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {

    private val categories = listOf(
        "API Requests" to listOf(
            PMCommand,
            PMGetInstalledPackages,
            PMGetInstalledApplications,
            PMGetPackagesHoldingPermissions
        ),
        "Suspicious Apps" to listOf()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detectionAppList =
            getPreferences(MODE_PRIVATE).getStringSet("appList", null)?.toList() ?: IDetector.basicAppList
        setContent {
            AppTheme {
                val data by remember {
                    val tmp = mutableListOf<MutableList<Pair<String, IDetector.Results>>>()
                    for (i in categories.indices) {
                        tmp.add(mutableListOf())
                        for (detector in categories[i].second) {
                            tmp[i].add(Pair(detector.name, IDetector.Results.NOT_FOUND))
                        }
                    }
                    mutableStateOf(tmp)
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
                    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                        for (i in categories.indices) {
                            FoldLayout(
                                status = FoldLayout.Status.NotStarted,
                                title = categories[i].first,
                                list = data[i]
                            )
                        }
                    }
                }
            }
        }
    }
}