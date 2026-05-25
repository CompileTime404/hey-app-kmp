package com.dorianbanic.chat.presentation.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dorianbanic.chat.domain.chat.ChatRepository
import com.dorianbanic.chat.domain.notification.CurrentDeviceTokenStore
import com.dorianbanic.chat.domain.notification.DeviceTokenService
import com.dorianbanic.chat.domain.participant.ChatParticipantRepository
import com.dorianbanic.chat.presentation.mappers.toUi
import com.dorianbanic.core.domain.auth.AuthService
import com.dorianbanic.core.domain.auth.SessionStorage
import com.dorianbanic.core.domain.util.onFailure
import com.dorianbanic.core.domain.util.onSuccess
import com.dorianbanic.core.presentation.util.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val chatRepository: ChatRepository,
    private val sessionStorage: SessionStorage,
    private val deviceTokenService: DeviceTokenService,
    private val authService: AuthService,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val currentDeviceTokenStore: CurrentDeviceTokenStore
) : ViewModel() {

    private val eventChannel = Channel<ChatListEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ChatListState())
    val state = combine(
        _state,
        chatRepository.getChats(),
        sessionStorage.observeAuthInfo()
    ) { currentState, chats, authInfo ->
        if (authInfo == null) {
            return@combine currentState
        }
        currentState.copy(
            chats = chats.map { it.toUi(authInfo.user.id) },
            localParticipant = authInfo.user.toUi()
        )
    }
        .onStart {
            if (!hasLoadedInitialData) {
                loadChats()
                fetchLocalUserProfile()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatListState()
        )

    fun onAction(action: ChatListAction) {
        when (action) {
            is ChatListAction.OnSelectChat -> {
                _state.value = _state.value.copy(
                    selectedChatId = action.chatId
                )
            }

            ChatListAction.OnUserAvatarClick -> {
                _state.update {
                    it.copy(
                        isUserMenuOpen = true
                    )
                }
            }

            ChatListAction.OnLogoutClick -> showLogoutConfirmation()
            ChatListAction.OnConfirmLogout -> logout()
            ChatListAction.OnDismissLogoutDialog -> {
                _state.update {
                    it.copy(
                        showLogoutConfirmation = false
                    )
                }

            }

            ChatListAction.OnProfileSettingsClick,
            ChatListAction.OnDismissUserMenu -> {
                _state.update {
                    it.copy(
                        isUserMenuOpen = false
                    )
                }
            }

            else -> Unit
        }
    }

    private fun fetchLocalUserProfile() {
        viewModelScope.launch {
            chatParticipantRepository
                .fetchLocalParticipant()
        }
    }

    private fun logout() {
        _state.update {
            it.copy(
                showLogoutConfirmation = false
            )
        }

        viewModelScope.launch {
            delay(100)
            val authInfo = sessionStorage.observeAuthInfo().first()
            val refreshToken = authInfo?.refreshToken ?: return@launch
            val deviceToken = currentDeviceTokenStore.get()

            if (deviceToken != null) {
                deviceTokenService.unregisterToken(deviceToken)
            }

            authService
                .logout(refreshToken)
                .onSuccess {
                    currentDeviceTokenStore.set(null)
                    sessionStorage.set(null)
                    chatRepository.deleteAllChats()
                    eventChannel.send(ChatListEvent.OnLogoutSuccess)
                }
                .onFailure { error ->
                    eventChannel.send(ChatListEvent.OnLogoutError(error.toUiText()))
                }
        }
    }

    private fun showLogoutConfirmation() {
        _state.update {
            it.copy(
                showLogoutConfirmation = true,
                isUserMenuOpen = false
            )
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            chatRepository.fetchChats()
        }
    }
}