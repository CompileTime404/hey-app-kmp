package com.dorianbanic.chat.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object HeyappChatDatabaseConstructor: RoomDatabaseConstructor<HeyappChatDatabase> {
    override fun initialize(): HeyappChatDatabase
}