package com.dorianbanic.heyapp

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dorianbanic.chat.database.HeyappChatDatabase
import com.dorianbanic.heyapp.navigation.ExternalUriHandler
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var shouldShowSplashScreen = true
        installSplashScreen().setKeepOnScreenCondition {
            shouldShowSplashScreen
        }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        handleChatMessageDeepLink(intent)

        setContent {
            App(
                onAuthenticationChecked = {
                    shouldShowSplashScreen = false
                }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleChatMessageDeepLink(intent)
    }

    private fun handleChatMessageDeepLink(intent: Intent) {
        val chatId = intent.getStringExtra("chatId")
            ?: intent.extras?.getString("chatId")

        if (chatId != null) {
            val deepLinkUrl = "heyapp://chat/$chatId" // Ovo može izazvat bug
            ExternalUriHandler.onNewUri(deepLinkUrl)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}