package icu.nullptr.applistdetector.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import icu.nullptr.applistdetector.IDetector
import icu.nullptr.applistdetector.MyApplication.Companion.checktext


val resultMap = mapOf(
    null to (Icons.Filled.HourglassEmpty to "Pending"),
    IDetector.Result.NOT_FOUND to (Icons.Filled.Done to checktext.get(0)),
    IDetector.Result.METHOD_UNAVAILABLE to (Icons.Filled.CodeOff to checktext.get(1)),
    IDetector.Result.SUSPICIOUS to (Icons.Filled.Visibility to checktext.get(2)),
    IDetector.Result.FOUND to (Icons.Filled.Coronavirus to checktext.get(3))
)

@Preview
@Composable
fun IconHintCard() {
    Row(
        modifier = Modifier.padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(modifier = Modifier.wrapContentWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val notFound = resultMap[IDetector.Result.NOT_FOUND]!!
                Icon(notFound.first, null)
                Spacer(Modifier.width(4.dp))
                Text(notFound.second)
            }
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                val suspicious = resultMap[IDetector.Result.SUSPICIOUS]!!
                Icon(suspicious.first, null)
                Spacer(Modifier.width(4.dp))
                Text(suspicious.second)
            }
        }
        Spacer(Modifier.width(24.dp))
        Column(modifier = Modifier.wrapContentWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val notAvailable = resultMap[IDetector.Result.METHOD_UNAVAILABLE]!!
                Icon(notAvailable.first, null)
                Spacer(Modifier.width(4.dp))
                Text(notAvailable.second)
            }
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                val found = resultMap[IDetector.Result.FOUND]!!
                Icon(found.first, null)
                Spacer(Modifier.width(4.dp))
                Text(found.second)
            }
        }
    }
}
