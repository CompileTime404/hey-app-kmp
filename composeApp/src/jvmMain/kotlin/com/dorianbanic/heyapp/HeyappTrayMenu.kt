package com.dorianbanic.heyapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.TrayState
import heyapp.composeapp.generated.resources.Res
import heyapp.composeapp.generated.resources.tray_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun ApplicationScope.HeyappTray(
    state: TrayState
) {
    Tray(
        icon = painterResource(Res.drawable.tray_icon),
        state = state,
    ) {

    }
}