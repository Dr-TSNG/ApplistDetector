package com.tsng.applistdetector.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

object TitleBar {
    val defaultModifier = Modifier.padding(end = 12.dp).size(24.dp)
    val next: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            modifier = defaultModifier
        )
    }
    val loading: @Composable () -> Unit = {
        CircularProgressIndicator(modifier = defaultModifier)
    }
}

@Composable
fun TitleBar(
    modifier: Modifier = Modifier,
    color: Color,
    title: String,
    subtitle: String? = null,
    extraText: String? = null,
    sideIcon: (@Composable () -> Unit)? = TitleBar.next
) {
    Row(
        modifier = modifier.height(68.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val typography = MaterialTheme.typography
        Spacer(Modifier.size(4.dp, 36.dp).background(color = color))
        Spacer(Modifier.width(12.dp))
        if (subtitle != null) {
            Column(Modifier) {
                Text(text = title, style = typography.body1)
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = subtitle, style = typography.subtitle1)
                }
            }
        } else Text(text = title, style = typography.h6)
        Spacer(Modifier.weight(1f))
        if (extraText != null) {
            Text(
                text = extraText,
                style = typography.h6,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Spacer(Modifier.width(16.dp))
        sideIcon?.let {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) { it() }
        }
    }
    RallyDivider()
}

@Composable
private fun RallyDivider(modifier: Modifier = Modifier) {
    Divider(color = MaterialTheme.colors.background, thickness = 1.dp, modifier = modifier)
}

@Preview
@Composable
private fun Preview_TitleBar() {
    TitleBar(
        color = Color.Red,
        title = "This is the title",
        subtitle = "This is the subtitle",
        extraText = "Extra text"
    )
}

@Preview
@Composable
private fun Preview_TitleBar_Simple() {
    TitleBar(color = Color.Green, title = "This is the title")
}