package com.dorianbanic.heyapp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dorianbanic.heyapp.windows.HeyappWindow
import androidx.compose.ui.window.application
import com.dorianbanic.heyapp.deeplink.DesktopDeepLinkHandler
import com.dorianbanic.heyapp.di.desktopModule
import com.dorianbanic.heyapp.di.initKoin
import com.dorianbanic.heyapp.navigation.ExternalUriHandler
import org.koin.compose.koinInject

fun main(args: Array<String>) {
    initKoin {
        modules(desktopModule)
    }

    DesktopDeepLinkHandler.setup()

    val initialDeepLink = args.firstOrNull {
        val cleanedDeepLink = it.trim('"')

        DesktopDeepLinkHandler.supportedUriPatterns.any { it.matches(cleanedDeepLink) }
    }?.trim('"')

    application {
        val applicationStateHolder = koinInject<ApplicationStateHolder>()
        val applicationState by applicationStateHolder.state.collectAsState()
        val windows = applicationState.windows

        var canReceiveDeepLink by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(canReceiveDeepLink) {
            if (canReceiveDeepLink && initialDeepLink != null) {
                ExternalUriHandler.onNewUri(initialDeepLink)
            }
        }

        LaunchedEffect(windows) {
            if (windows.isEmpty()) {
                exitApplication()
            }
        }

        HeyappTray(
            state = applicationState.trayState
        )

        for (window in windows) {
            key(window.id) {
                HeyappWindow(
                    onCloseRequest = {
                        applicationStateHolder.onWindowCloseRequest(window.id)
                    },
                    onAddWindowClick = applicationStateHolder::onAddWindowClick,
                    onFocusChanged = { focused ->
                        applicationStateHolder.onWindowFocusChanged(window.id, focused)
                    },
                    onDeepLinkListenerSetup = {
                        canReceiveDeepLink = true
                    }
                )
            }
        }
    }
}