package com.cmt.openapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cmt.openapp.R
import com.cmt.openapp.ui.loading.LoadingScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
        NetworkCapabilities.TRANSPORT_CELLULAR
    ) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
}

@Composable
fun CheckInternetScreen(content: @Composable () -> Unit) {
    val context = LocalContext.current
    var isConnected by remember { mutableStateOf(isInternetAvailable(context)) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (isConnected) {
        content()
    } else {
        if (isLoading) {
            LoadingScreen()
        } else {
            NoInternetConnectionScreen(onRetry = {
                isLoading = true
                coroutineScope.launch {
                    delay(500)
                    isConnected = isInternetAvailable(context)
                    isLoading = false
                }
            })
        }
    }
}

@Composable
fun NoInternetConnectionScreen(onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.message_no_internet),
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text(
                    text = stringResource(id = R.string.button_no_internet),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}
