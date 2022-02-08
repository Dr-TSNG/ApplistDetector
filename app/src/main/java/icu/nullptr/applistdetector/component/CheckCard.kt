package icu.nullptr.applistdetector.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import icu.nullptr.applistdetector.Detail
import icu.nullptr.applistdetector.IDetector

@Composable
fun CheckCard(
    title: String,
    result: IDetector.Result?,
    detail: Detail?
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = result != null) { expanded = !expanded },
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            Modifier
                .padding(all = 16.dp)
                .animateContentSize(spring(stiffness = Spring.StiffnessLow))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val map = resultMap[result]!!
                Icon(map.first, map.second)
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                val rotateAngel by animateFloatAsState(if (expanded) 180f else 0f)
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Expand",
                    modifier = Modifier
                        .rotate(rotateAngel)
                        .alpha(if (result == null) 0f else 1f)
                )
            }

            if (expanded) {
                Column(Modifier.padding(top = 16.dp)) {
                    detail!!.forEachIndexed { index, item ->
                        val map = resultMap[item.second]!!
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(map.first, map.second, Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(text = item.first, style = MaterialTheme.typography.bodyMedium)
                        }
                        if (index != detail.size) {
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CheckCardPreview() {
    CheckCard(
        title = "Abnormal Environment",
        result = IDetector.Result.FOUND,
        detail = mutableListOf(
            "Xposed Hooks" to IDetector.Result.FOUND,
            "Dual / Work profile" to IDetector.Result.SUSPICIOUS,
            "XprivacyLua" to IDetector.Result.NOT_FOUND
        )
    )
}
