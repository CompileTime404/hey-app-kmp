package com.dorianbanic.core.desingsystem.components.dialogs

import androidx.compose.runtime.Composable
import com.dorianbanic.core.presentation.util.currentDeviceConfiguration

@Composable
fun HeyappAdaptiveDialogSheetLayout(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val configuration = currentDeviceConfiguration()
    if (configuration.isMobile) {
        HeyappBottomSheet(
            onDismiss = onDismiss,
            content = content
        )
    } else {
        HeyappDialogContent(
            onDismiss = onDismiss,
            content = content
        )
    }
}