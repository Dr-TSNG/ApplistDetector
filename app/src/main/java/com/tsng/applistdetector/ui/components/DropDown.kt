package com.tsng.applistdetector.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

object DropDown {
    enum class Status {
        Disabled, Loading, InitiallyOpened, InitiallyClosed
    }
}

@ExperimentalAnimationApi
@Composable
fun DropDown(
    modifier: Modifier = Modifier,
    status: DropDown.Status = DropDown.Status.InitiallyOpened,
    color: Color,
    title: String,
    subtitle: String? = null,
    extraText: String? = null,
    content: @Composable () -> Unit
) {
    val isAvailable = status == DropDown.Status.InitiallyOpened || status == DropDown.Status.InitiallyClosed
    var isOpen by remember {
        mutableStateOf(status == DropDown.Status.InitiallyOpened)
    }
    val alpha = animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(durationMillis = 300)
    )
    val rotateX = animateFloatAsState(
        targetValue = if (isOpen) 0f else -90f,
        animationSpec = tween(durationMillis = 300)
    )
    Column(modifier = modifier.fillMaxWidth()) {
        TitleBar(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = isAvailable) { isOpen = !isOpen },
            color = color,
            title = title,
            subtitle = subtitle,
            extraText = extraText,
            sideIcon = when (status) {
                DropDown.Status.Disabled -> null
                DropDown.Status.Loading -> TitleBar.loading
                else -> {
                    {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = TitleBar.defaultModifier.scale(1f, if (isOpen) -1f else 1f)
                        )
                    }
                }
            }
        )
        AnimatedVisibility(visible = isOpen) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(0.5f, 0f)
                        rotationX = rotateX.value
                    }
                    .alpha(alpha.value)
            ) {
                content()
            }
        }
    }
}