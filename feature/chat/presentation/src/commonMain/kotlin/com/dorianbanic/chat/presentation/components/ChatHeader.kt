package com.dorianbanic.chat.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dorianbanic.core.desingsystem.components.brand.HeyappHorizontalDivider
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChatHeader(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 80.dp)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
        HeyappHorizontalDivider()
    }
}