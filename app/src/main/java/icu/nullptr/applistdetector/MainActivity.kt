package icu.nullptr.applistdetector

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiObjects
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import icu.nullptr.applistdetector.MyApplication.Companion.accList
import icu.nullptr.applistdetector.MyApplication.Companion.accenable
import icu.nullptr.applistdetector.MyApplication.Companion.appContext
import icu.nullptr.applistdetector.MyApplication.Companion.checktext
import icu.nullptr.applistdetector.MyApplication.Companion.titletext
import icu.nullptr.applistdetector.theme.MyTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkDisabled()
        init()
        setContent {
            MyTheme {
                var showDialog by remember { mutableStateOf(false) }
                if (showDialog) AboutDialog { showDialog = false }
                Scaffold(
                    topBar = { MainTopBar() },
                    content = { MainPage() },
                    floatingActionButton = { MainFab { showDialog = true } },
                )
            }
        }
    }
}

@Composable
private fun MainTopBar() {
    CenterAlignedTopAppBar(
        title = { Text(init()[0]) }
    )
}

@Composable
private fun MainFab(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        icon = { Icon(Icons.Outlined.EmojiObjects, (init()[1]))},
        text = { Text(init()[1]) },
        onClick = onClick
    )
}

@Composable
private fun AboutDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(android.R.string.ok))
            }
        },
        title = { Text(init()[1]) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyLarge) {
                    Text(init()[0])
                    Text(init()[2]+": Nullptr")
                }
                Spacer(Modifier.height(10.dp))
                val annotatedString = buildAnnotatedString {
                    pushStringAnnotation("GitHub", "https://github.com/Dr-TSNG/ApplistDetector")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(init()[3])
                    }
                    pop()
                    append("    ")
                    pushStringAnnotation("Telegram", "https://t.me/HideMyApplist")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(init()[4])
                    }
                    pop()
                }
                ClickableText(annotatedString, style = MaterialTheme.typography.bodyLarge) { offset ->
                    annotatedString.getStringAnnotations("GitHub", offset, offset).firstOrNull()?.let {
                        startActivity(context, Intent(Intent.ACTION_VIEW, Uri.parse(it.item)), null)
                    }
                    annotatedString.getStringAnnotations("Telegram", offset, offset).firstOrNull()?.let {
                        startActivity(context, Intent(Intent.ACTION_VIEW, Uri.parse(it.item)), null)
                    }
                }
            }
        },
    )
}
fun init(): Array<String> {
    var app_name:String=appContext.getString(R.string.app_name)+"V${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    var about:String=appContext.getString(R.string.about)
    var authored:String=appContext.getString(R.string.authored)
    var source:String=appContext.getString(R.string.source)
    var telegram:String=appContext.getString(R.string.telegram)
    var Stringlist= arrayOf(app_name,about,authored,source,telegram)

    var not_found:String=appContext.getString(R.string.not_found)
    var method:String=appContext.getString(R.string.method)
    var suspicious:String=appContext.getString(R.string.suspicious)
    var found:String=appContext.getString(R.string.found)
    checktext= arrayListOf(not_found,method,suspicious,found)
    var abnormal:String=appContext.getString(R.string.abnormal)
    var filedet:String=appContext.getString(R.string.filedet)
    var pmc:String=appContext.getString(R.string.pmc)
    var pmca:String=appContext.getString(R.string.pmca)
    var pmsa:String=appContext.getString(R.string.pmsa)
    var pmiq:String=appContext.getString(R.string.pmiq)
    var xposed:String=appContext.getString(R.string.xposed)
    var magisk:String=appContext.getString(R.string.magisk)
    var accessibility:String=appContext.getString(R.string.accessibility)
    titletext= arrayListOf(abnormal,filedet,pmc,pmca,pmsa,pmiq,xposed,magisk,accessibility)
    return Stringlist;
}
private fun checkDisabled() {
    accList = getFromAccessibilityManager()+getFromSettingsSecure()
}

private fun getFromAccessibilityManager(): List<String> {
    val accessibilityManager =
        ContextCompat.getSystemService(appContext, AccessibilityManager::class.java)
            ?: error("unreachable")
    val serviceList: List<AccessibilityServiceInfo> =
        accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            ?: emptyList()
    val nameList = serviceList.map {
        appContext.packageManager.getApplicationLabel(it.resolveInfo.serviceInfo.applicationInfo)
            .toString()
    }.toMutableList()
    if (accessibilityManager.isEnabled) {
        nameList.add("AccessibilityManager.isEnabled")
    }
    if (accessibilityManager.isTouchExplorationEnabled) {
        nameList.add("AccessibilityManager.isTouchExplorationEnabled")
    }
    return nameList
}

private fun getFromSettingsSecure():List<String> {
    val settingValue= Settings.Secure.getString(
        appContext.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    )
    val nameList=if (settingValue.isNullOrEmpty()){
        emptyList()
    }else{
        settingValue.split(':')
    }.toMutableList()
    val enabled = Settings.Secure.getInt(appContext.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
    if (enabled != 0) {
        accenable=true
    }
    return nameList
}
