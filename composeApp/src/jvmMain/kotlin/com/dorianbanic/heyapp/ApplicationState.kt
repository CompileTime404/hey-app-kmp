package com.dorianbanic.heyapp

import androidx.compose.ui.window.TrayState
import com.dorianbanic.heyapp.windows.WindowState

data class ApplicationState(
    val windows: List<WindowState> = listOf(WindowState()),
    val trayState: TrayState = TrayState()
)