package com.cmt.openapp.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cmt.openapp.R


@Composable
fun HeaderSection(
    modifier: Modifier = Modifier, isInfo: Boolean, onInfoClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        if (isInfo) IconInfo(onInfoClick, Modifier.align(Alignment.TopStart)) else IconBack(
            onBackClick,
            Modifier.align(Alignment.TopStart)
        )

        Image(
            painter = painterResource(id = R.drawable.open_logo_small),
            contentDescription = "Logo CMT",
            Modifier
                .align(Alignment.Center)
                .height(80.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun IconInfo(onClick: () -> Unit, modifier: Modifier) {
    Icon(
        imageVector = Icons.Default.Info,
        contentDescription = "Información sobre incidentes",
        modifier = modifier
            .padding(24.dp)
            .clickable { onClick() },
        tint = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
fun IconBack(onBackClick: () -> Unit, modifier: Modifier) {
    Icon(
        imageVector = Icons.Default.ArrowBackIosNew,
        contentDescription = "Retroceso",
        modifier = modifier
            .padding(24.dp)
            .clickable { onBackClick() },
        tint = MaterialTheme.colorScheme.tertiary
    )
}
