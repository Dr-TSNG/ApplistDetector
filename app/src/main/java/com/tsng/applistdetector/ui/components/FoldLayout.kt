package com.tsng.applistdetector.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    val subtitle = when (status) {
        FoldLayout.Status.NotStarted -> stringResource(id = R.string.waiting)
        FoldLayout.Status.Loading -> stringResource(id = R.string.running)
        FoldLayout.Status.Completed -> stringResource(id = R.string.completed)
    }
    val dropBarStatus = when (status) {
        FoldLayout.Status.NotStarted -> DropDown.Status.Disabled
        FoldLayout.Status.Loading -> DropDown.Status.Loading
        FoldLayout.Status.Completed -> DropDown.Status.InitiallyOpened
    }
    var color =
        if (status == FoldLayout.Status.Completed)
            colorResource(id = android.R.color.holo_green_dark)
        else Color.Black
    for (item in list) {
        if (item.second == IDetector.Results.FOUND)
            color = Color.Red
        if (color != Color.Red && item.second == IDetector.Results.SUSPICIOUS)
            color = colorResource(id = android.R.color.holo_orange_dark)
    }

    DropDown(
        color = color,
        title = title,
        subtitle = subtitle,
        status = dropBarStatus
    ) {
        LazyColumn {
            items(list) {
                StatusLayout(name = it.first, status = it.second)
            }
        }
    }
}

@Composable
private fun StatusLayout(name: String, status: IDetector.Results) {
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