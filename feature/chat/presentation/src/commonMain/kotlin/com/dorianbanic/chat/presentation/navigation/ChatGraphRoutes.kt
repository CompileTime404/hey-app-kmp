package com.dorianbanic.chat.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.dorianbanic.chat.presentation.chat_list_detail.ChatListDetailAdaptiveLayout
import kotlinx.serialization.Serializable

sealed interface ChatGraphRoutes {
    @Serializable
    data object Graph: ChatGraphRoutes

    @Serializable
    data class ChatListDetailRoute(val chatId: String? = null): ChatGraphRoutes

    @Serializable
    data object Detail: ChatGraphRoutes
}

fun NavGraphBuilder.chatGraph(
    navController: NavController,
    onLogout: () -> Unit
) {
    navigation<ChatGraphRoutes.Graph>(
        startDestination = ChatGraphRoutes.ChatListDetailRoute(null),
    ) {
        composable<ChatGraphRoutes.ChatListDetailRoute>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "heyapp://chat_detail/{chatId}"
                }
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<ChatGraphRoutes.ChatListDetailRoute>()
            ChatListDetailAdaptiveLayout(
                initialChatId = route.chatId,
                onLogout = onLogout
            )
        }
    }
}