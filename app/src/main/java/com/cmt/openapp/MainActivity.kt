package com.cmt.openapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.cmt.openapp.core.navigation.AppNavGraph
import com.cmt.openapp.core.network.CheckInternetScreen
import com.cmt.openapp.ui.theme.OpenAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenAppTheme {

                CheckInternetScreen {
                    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
                        val navigationController = rememberNavController()
                        AppNavGraph(Modifier.padding(innerPadding), navigationController)
                    }
                }
            }
        }
    }
}

@Preview(name = "Open App Preview", showSystemUi = true)
@Composable
fun OpenAppPreview() {
    OpenAppTheme {
    }
}