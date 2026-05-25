package com.dorianbanic.heyapp.windows

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import com.dorianbanic.heyapp.App
import heyapp.composeapp.generated.resources.Res
import heyapp.composeapp.generated.resources.file
import heyapp.composeapp.generated.resources.new_window
import org.jetbrains.compose.resources.stringResource

@Composable
fun HeyappWindow(
    onCloseRequest: () -> Unit,
    onAddWindowClick: () -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    onDeepLinkListenerSetup: () -> Unit,
) {
    val windowState = rememberWindowState(
        width = 1200.dp,
        height = 800.dp
    )
    Window(
        onCloseRequest = onCloseRequest,
        state = windowState,
        title = "HeyApp",
    ) {
        FocusObserver(
            onFocusChanged = onFocusChanged
        )
        MenuBar {
            Menu(
                text = stringResource(Res.string.file),
                mnemonic = 'F'
            ) {
                Item(
                    text = stringResource(Res.string.new_window),
                    mnemonic = 'N',
                    shortcut = KeyShortcut(
                        key = Key.N,
                        ctrl = true,
                        shift = true
                    ),
                    onClick = onAddWindowClick
                )
            }
        }

        App(
            onDeepLinkListenerSetup = onDeepLinkListenerSetup
        )
    }
}