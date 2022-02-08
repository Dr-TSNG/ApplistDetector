package icu.nullptr.applistdetector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import icu.nullptr.applistdetector.theme.MyTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                Scaffold(
                    topBar = { MainTopBar() },
                    content = { MainPage() },
                    floatingActionButton = { MainFab() },
                )
            }
        }
    }
}

@Composable
private fun MainTopBar() {
    CenterAlignedTopAppBar(
        title = { Text("Applist Detector") }
    )
}

@Composable
private fun MainFab() {

}
