package com.dorianbanic.heyapp

sealed interface MainEvent {
    data object OnSessionExpired: MainEvent
}