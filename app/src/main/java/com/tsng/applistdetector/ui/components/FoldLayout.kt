package com.tsng.applistdetector.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tsng.applistdetector.R
import com.tsng.applistdetector.detections.IDetector

object FoldLayout {
    enum class Status {
        NotStarted, Loading, Completed
    }
}

@ExperimentalAnimationApi
@Composable
fun FoldLayout(status: FoldLayout.Status, title: String, list: List<Pair<String, IDetector.Results>>) {
    var result = IDetector.Results.NOT_FOUND
    list.forEach { if (result < it.second) result = it.second }

    val color =
        if (status == FoldLayout.Status.Completed) resultColor(result)
        else Color.Black

    val subtitle = when (status) {
        FoldLayout.Status.NotStarted -> stringResource(id = R.string.waiting)
        FoldLayout.Status.Loading -> stringResource(id = R.string.running)
        FoldLayout.Status.Completed -> when (result) {
            IDetector.Results.FOUND -> stringResource(id = R.string.found)
            IDetector.Results.NOT_FOUND -> stringResource(id = R.string.not_found)
            IDetector.Results.PERMISSION_DENIED -> stringResource(id = R.string.permission_denied)
            IDetector.Results.SUSPICIOUS -> stringResource(id = R.string.suspicious)
        }
    }

    val dropBarStatus = when (status) {
        FoldLayout.Status.NotStarted -> DropDown.Status.Disabled
        FoldLayout.Status.Loading -> DropDown.Status.Loading
        FoldLayout.Status.Completed -> DropDown.Status.InitiallyClosed
    }

    DropDown(
        color = color,
        title = title,
        subtitle = subtitle,
        status = dropBarStatus
    ) {
        Column {
            for (item in list) {
                StatusLayout(name = item.first, result = item.second)
            }
        }
    }
}

@Composable
private fun StatusLayout(name: String, result: IDetector.Results) {
    val color = resultColor(result)
    val resultText = when (result) {
        IDetector.Results.FOUND -> "🔴 " + stringResource(id = R.string.found)
        IDetector.Results.NOT_FOUND -> "🟢 " + stringResource(id = R.string.not_found)
        IDetector.Results.PERMISSION_DENIED -> "⚫ " + stringResource(id = R.string.permission_denied)
        IDetector.Results.SUSPICIOUS -> "🟡 " + stringResource(id = R.string.suspicious)
    }
    Surface {
    Row {
            Text(text = name, modifier = Modifier.width(200.dp), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = resultText, color = color)
        }
    }
}

@Composable
private fun resultColor(result: IDetector.Results): Color {
    return when (result) {
        IDetector.Results.NOT_FOUND -> colorResource(id = android.R.color.holo_green_dark)
        IDetector.Results.PERMISSION_DENIED -> Color.Black
        IDetector.Results.SUSPICIOUS -> colorResource(id = android.R.color.holo_orange_dark)
        IDetector.Results.FOUND -> Color.Red
    }
}