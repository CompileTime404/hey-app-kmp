package com.dorianbanic.heyapp.windows

import java.util.UUID

data class WindowState(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "HeyApp",
    val isFocused: Boolean = false
)