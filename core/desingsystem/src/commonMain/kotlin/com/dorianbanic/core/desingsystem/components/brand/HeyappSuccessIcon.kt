package com.dorianbanic.core.desingsystem.components.brand

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dorianbanic.core.desingsystem.theme.extended
import heyapp.core.desingsystem.generated.resources.Res
import heyapp.core.desingsystem.generated.resources.success_checkmark
import org.jetbrains.compose.resources.vectorResource

@Composable
fun HeyappSuccessIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = vectorResource(Res.drawable.success_checkmark),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.extended.success,
        modifier = modifier
    )
}