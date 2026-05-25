package com.dorianbanic.heyapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.dorianbanic.auth.presentation.navigation.AuthGraphRoutes
import com.dorianbanic.chat.presentation.navigation.ChatGraphRoutes
import com.dorianbanic.core.desingsystem.theme.HeyAppTheme
import com.dorianbanic.core.presentation.util.ObserveAsEvents
import com.dorianbanic.heyapp.navigation.DeepLinkListener
import com.dorianbanic.heyapp.navigation.NavigationRoot
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    onAuthenticationChecked: () -> Unit = {},
    onDeepLinkListenerSetup: () -> Unit = {},
    viewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isCheckingAuth) {
        if (!state.isCheckingAuth) {
            onAuthenticationChecked()
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is MainEvent.OnSessionExpired -> {
                navController.navigate(AuthGraphRoutes.Graph) {
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = false
                    }
                }
            }
        }
    }

    HeyAppTheme(
    ) {
        if (!state.isCheckingAuth) {
            NavigationRoot(
                navController = navController,
                startDestination = if(state.isLogged) {
                    ChatGraphRoutes.Graph
                } else {
                    AuthGraphRoutes.Graph
                }
            )
            DeepLinkListener(navController, onDeepLinkListenerSetup)
        }
    }
}