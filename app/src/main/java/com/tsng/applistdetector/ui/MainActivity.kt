package com.tsng.applistdetector.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tsng.applistdetector.R
import com.tsng.applistdetector.detections.IDetector
import com.tsng.applistdetector.ui.theme.AppTheme

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                DefaultPreview()
            }
        }
    }
}

@Composable
fun FoldLayout(list: List<Pair<String, IDetector.Results>>) {
    LazyColumn {
        items(list) {
            StatusLayout(name = it.first, status = it.second)
        }
    }
}

@Composable
fun StatusLayout(name: String, status: IDetector.Results) {
    val color = when (status) {
        IDetector.Results.FOUND -> Color.Red
        IDetector.Results.NOT_FOUND -> colorResource(id = android.R.color.holo_green_dark)
        IDetector.Results.PERMISSION_DENIED -> Color.Black
        IDetector.Results.SUSPICIOUS -> colorResource(id = android.R.color.holo_orange_dark)
    }
    val result = when (status) {
        IDetector.Results.FOUND -> "🔴 " + stringResource(id = R.string.found)
        IDetector.Results.NOT_FOUND -> "🟢 " + stringResource(id = R.string.not_found)
        IDetector.Results.PERMISSION_DENIED -> "⚫ " + stringResource(id = R.string.permission_denied)
        IDetector.Results.SUSPICIOUS -> "🟡 " + stringResource(id = R.string.suspicious)
    }
    Row {
        Text(text = name, modifier = Modifier.width(200.dp), maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(text = result, color = color)
    }
}

@Preview(showBackground = true)
@ExperimentalAnimationApi
@Composable
fun DefaultPreview() {
    AppTheme {
        Column {
            DropDown("test1", modifier = Modifier.padding(horizontal = 20.dp)) {
                FoldLayout(
                    listOf(
                        "com.topjohnwu.magisk" to IDetector.Results.FOUND,
                        "com.test.1" to IDetector.Results.NOT_FOUND,
                        "com.test.2" to IDetector.Results.PERMISSION_DENIED,
                        "com.test.3" to IDetector.Results.SUSPICIOUS
                    )
                )
            }
            DropDown("test2", modifier = Modifier.padding(horizontal = 20.dp)) {
                FoldLayout(
                    listOf(
                        "com.topjohnwu.magisk" to IDetector.Results.FOUND,
                        "com.test.1" to IDetector.Results.NOT_FOUND,
                        "com.test.2" to IDetector.Results.PERMISSION_DENIED,
                        "com.test.3" to IDetector.Results.SUSPICIOUS
                    )
                )
            }
        }
    }
}