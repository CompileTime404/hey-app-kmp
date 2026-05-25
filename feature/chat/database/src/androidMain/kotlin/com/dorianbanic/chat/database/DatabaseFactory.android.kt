package com.dorianbanic.chat.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<HeyappChatDatabase> {
        val dbFile = context.applicationContext.getDatabasePath(HeyappChatDatabase.DB_NAME)

        return Room.databaseBuilder(
            context = context.applicationContext,
            name = dbFile.absolutePath
        )
    }
}