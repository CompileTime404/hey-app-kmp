package com.dorianbanic.heyapp

import androidx.compose.ui.window.Notification
import com.dorianbanic.chat.data.notification.DesktopNotifier
import com.dorianbanic.heyapp.windows.WindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ApplicationStateHolder(
    private val applicationScope: CoroutineScope,
    private val desktopNotifier: DesktopNotifier
) {

    private val _state = MutableStateFlow(ApplicationState())
    val state = _state
        .onStart {
            observeNewMessages()
        }
        .stateIn(
            applicationScope,
            SharingStarted.Lazily,
            _state.value
        )

    fun observeNewMessages() {
        desktopNotifier
            .observeNewNotifications()
            .onEach { notificationPayload ->
                val isAppInBackground = state.value.windows.none { it.isFocused }

                if(isAppInBackground) {
                    state.value.trayState.sendNotification(
                        notification = Notification(
                            title = notificationPayload.title,
                            message = notificationPayload.message,
                            type = Notification.Type.Info
                        )
                    )
                }
            }
            .launchIn(applicationScope)
    }

    fun onWindowFocusChanged(id: String, isFocused: Boolean) {
        _state.update { it.copy(
            windows = it.windows.map { currentWindow ->
                if(currentWindow.id == id) {
                    currentWindow.copy(isFocused = isFocused)
                } else currentWindow
            }
        ) }
    }

    fun onAddWindowClick() {
        _state.update { it.copy(
            windows = it.windows + WindowState()
        ) }
    }

    fun onWindowCloseRequest(id: String) {
        _state.update { it.copy(
            windows = it.windows.filter { it.id != id }
        ) }
    }
}