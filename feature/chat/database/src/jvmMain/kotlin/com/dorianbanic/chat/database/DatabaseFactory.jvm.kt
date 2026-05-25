package com.dorianbanic.chat.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.dorianbanic.core.data.util.appDataDirectory
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<HeyappChatDatabase> {
        val directory = appDataDirectory

        if (!directory.exists()) {
            directory.mkdirs()
        }
        val dbFile = File(directory, HeyappChatDatabase.DB_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}