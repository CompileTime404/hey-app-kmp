package com.dorianbanic.chat.presentation.chat_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dorianbanic.chat.presentation.chat_detail.components.ChatDetailHeader
import com.dorianbanic.chat.presentation.chat_detail.components.DateChip
import com.dorianbanic.chat.presentation.chat_detail.components.MessageBox
import com.dorianbanic.chat.presentation.chat_detail.components.MessageList
import com.dorianbanic.chat.presentation.chat_detail.components.MessageBannerListener
import com.dorianbanic.chat.presentation.chat_detail.components.PaginationScrollListener
import com.dorianbanic.chat.presentation.components.ChatHeader
import com.dorianbanic.chat.presentation.components.EmptySection
import com.dorianbanic.chat.presentation.model.MessageUi
import com.dorianbanic.core.desingsystem.components.dialogs.DestructiveConfirmationDialog
import com.dorianbanic.core.desingsystem.theme.extended
import com.dorianbanic.core.presentation.util.ObserveAsEvents
import com.dorianbanic.core.presentation.util.clearFocusOnTap
import com.dorianbanic.core.presentation.util.currentDeviceConfiguration
import heyapp.feature.chat.presentation.generated.resources.Res
import heyapp.feature.chat.presentation.generated.resources.cancel
import heyapp.feature.chat.presentation.generated.resources.do_you_want_to_leave_chat
import heyapp.feature.chat.presentation.generated.resources.do_you_want_to_leave_chat_desc
import heyapp.feature.chat.presentation.generated.resources.leave_chat
import heyapp.feature.chat.presentation.generated.resources.no_chat_selected
import heyapp.feature.chat.presentation.generated.resources.select_a_chat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatDetailRoot(
    chatId: String?,
    isDetailPresent: Boolean,
    onBack: () -> Unit,
    onChatMembersClick: () -> Unit,
    viewModel: ChatDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarState = remember { SnackbarHostState() }
    val messageListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.events) {event ->
        when(event) {
            ChatDetailEvent.OnChatLeft -> onBack()
            is ChatDetailEvent.OnNewMessage -> {
                scope.launch {
                    messageListState.animateScrollToItem(0)
                }
            }

            is ChatDetailEvent.OnError -> {
                snackbarState.showSnackbar(event.error.asStringAsync())
            }
        }
    }

    LaunchedEffect(chatId) {
        viewModel.onAction(ChatDetailAction.OnSelectChat(chatId))
    }

    LaunchedEffect(chatId) {
        if(chatId != null) {
            messageListState.scrollToItem(0)
        }
    }

    BackHandler(enabled = !isDetailPresent) {
        scope.launch {
            delay(300)
            viewModel.onAction(ChatDetailAction.OnSelectChat(null))
        }
        onBack()
    }

    ChatDetailScreen(
        state = state,
        messageListState = messageListState,
        isDetailPresent = isDetailPresent,
        snackbarState = snackbarState,
        onAction = { action ->
            when(action) {
                is ChatDetailAction.OnChatMembersClick -> onChatMembersClick()
                ChatDetailAction.OnBackClick -> onBack()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun ChatDetailScreen(
    state: ChatDetailState,
    messageListState: LazyListState,
    isDetailPresent: Boolean,
    snackbarState: SnackbarHostState,
    onAction: (ChatDetailAction) -> Unit,
) {
    val configuration = currentDeviceConfiguration()

    val realMessageItemCount = remember(state.messages) {
        state.messages.filter {
            it is MessageUi.LocalUserMessage || it is MessageUi.OtherUserMessage
        }.size
    }

    LaunchedEffect(messageListState) {
        snapshotFlow {
            messageListState.firstVisibleItemIndex to messageListState.layoutInfo.totalItemsCount
        }.filter { (firstVisibleIndex, totalItemsCount) ->
            firstVisibleIndex >= 0 && totalItemsCount > 0
        }.collect { (firstVisibleIndex, _) ->
            onAction(ChatDetailAction.OnFirstVisibleIndexChanged(firstVisibleIndex))
        }
    }

    MessageBannerListener(
        lazyListState = messageListState,
        messages = state.messages,
        isBannerVisible = state.bannerState.isVisible,
        onShowBanner = { topVisibleItemIndex ->
            onAction(ChatDetailAction.OnTopVisibleIndexChanged(topVisibleItemIndex))
        },
        onHide = {
            onAction(ChatDetailAction.OnHideBanner)
        }
    )


    PaginationScrollListener(
        lazyListState = messageListState,
        itemCount = realMessageItemCount,
        isPaginationLoading = state.isPaginationLoading,
        isEndReached = state.endReached,
        onNearTop = {
            onAction(ChatDetailAction.OnScrollToTop)
        }
    )

    var headerHeight by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = if (!configuration.isWideScreen) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.extended.surfaceLower
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarState
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .clearFocusOnTap()
                .padding(innerPadding)
                .then(
                    if (configuration.isWideScreen) {
                        Modifier.padding(horizontal = 8.dp)
                    } else Modifier
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DynamicRoundedCornerColumn(
                    isCornersRounded = configuration.isWideScreen,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (state.chatUi == null) {
                        EmptySection(
                            title = stringResource(Res.string.no_chat_selected),
                            description = stringResource(Res.string.select_a_chat),
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    } else {
                        ChatHeader(
                            modifier = Modifier
                                .onSizeChanged {
                                    headerHeight = with(density) {
                                        it.height.toDp()
                                    }
                                }
                        ) {
                            ChatDetailHeader(
                                chatUi = state.chatUi,
                                isDetailPresent = isDetailPresent,
                                isChatOptionsDropDownOpen = state.isChatOptionsOpen,
                                onChatOptionsClick = {
                                    onAction(ChatDetailAction.OnChatOptionClick)
                                },
                                onDismissChatOptions = {
                                    onAction(ChatDetailAction.OnDismissChatOptions)
                                },
                                onManageChatClick = {
                                    onAction(ChatDetailAction.OnChatMembersClick)
                                },
                                onLeaveChatClick = {
                                    onAction(ChatDetailAction.OnLeaveChatClick)
                                },
                                onBackClick = {
                                    onAction(ChatDetailAction.OnBackClick)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        MessageList(
                            messages = state.messages,
                            messageWithOpenMenu = state.messageWithOpenMenu,
                            listState = messageListState,
                            paginationError = state.paginationError?.asString(),
                            isPaginationLoading = state.isPaginationLoading,
                            onMessageLongClick = {
                                onAction(ChatDetailAction.OnMessageLongClick(it))
                            },
                            onMessageRetryClick = {
                                onAction(ChatDetailAction.OnRetryClick(it))
                            },
                            onDismissMessageMenu = {
                                onAction(ChatDetailAction.OnDismissMessageMenu)
                            },
                            onDeleteMessageClick = {
                                onAction(ChatDetailAction.OnDeleteMessageClick(it))
                            },
                            onRetryPaginationClick = {
                                onAction(ChatDetailAction.OnRetryPaginationClick)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                        AnimatedVisibility(
                            visible = !configuration.isWideScreen
                        ) {
                            MessageBox(
                                messageTextFieldState = state.messageTextFieldState,
                                isSendButtonEnabled = state.canSendMessage,
                                connectionState = state.connectionState,
                                onSendClick = {
                                    onAction(ChatDetailAction.OnSendMessageClick)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .imePadding()
                                    .padding(
                                        vertical = 8.dp,
                                        horizontal = 16.dp
                                    )
                            )
                        }
                    }
                }

                if (configuration.isWideScreen) {
                    Spacer(Modifier.height(8.dp))
                }

                AnimatedVisibility(
                    visible = configuration.isWideScreen && state.chatUi != null,
                ) {
                    DynamicRoundedCornerColumn(
                        isCornersRounded = configuration.isWideScreen,
                    ) {
                        MessageBox(
                            messageTextFieldState = state.messageTextFieldState,
                            isSendButtonEnabled = state.canSendMessage,
                            connectionState = state.connectionState,
                            onSendClick = {
                                onAction(ChatDetailAction.OnSendMessageClick)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .imePadding()
                                .padding(8.dp)
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = state.bannerState.isVisible,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = headerHeight + 16.dp),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                if (state.bannerState.formattedDate != null) {
                    DateChip(
                        date = state.bannerState.formattedDate.asString()
                    )
                }
            }
        }
    }
    if (state.showLeaveChatConfirmationDialog) {
        DestructiveConfirmationDialog(
            title = stringResource(Res.string.do_you_want_to_leave_chat),
            description = stringResource(Res.string.do_you_want_to_leave_chat_desc),
            confirmButtonText = stringResource(Res.string.leave_chat),
            cancelButtonText = stringResource(Res.string.cancel),
            onConfirmClick = {
                onAction(ChatDetailAction.OnConfirmLeaveChatClick)
            },
            onCancelClick = {
                onAction(ChatDetailAction.OnDismissLeaveChatDialog)
            },
            onDismiss = {
                onAction(ChatDetailAction.OnDismissLeaveChatDialog)
            }
        )
    }
}

@Composable
private fun DynamicRoundedCornerColumn(
    isCornersRounded: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = if (isCornersRounded) 8.dp else 0.dp,
                shape = if (isCornersRounded) RoundedCornerShape(24.dp) else RectangleShape,
                spotColor = Color.Black.copy(0.2f)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = if (isCornersRounded) RoundedCornerShape(24.dp) else RectangleShape
            )
    ) {
        content()
    }
}