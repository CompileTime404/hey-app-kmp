package com.dorianbanic.core.desingsystem.components.brand

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun HeyappHorizontalDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outline,
        modifier = modifier
    )
}