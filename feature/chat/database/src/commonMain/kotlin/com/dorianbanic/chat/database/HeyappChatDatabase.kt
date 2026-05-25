package com.dorianbanic.chat.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.dorianbanic.chat.database.dao.ChatDao
import com.dorianbanic.chat.database.dao.ChatMessageDao
import com.dorianbanic.chat.database.dao.ChatParticipantDao
import com.dorianbanic.chat.database.dao.ChatParticipantsCrossRefDao
import com.dorianbanic.chat.database.entities.ChatEntity
import com.dorianbanic.chat.database.entities.ChatMessageEntity
import com.dorianbanic.chat.database.entities.ChatParticipantCrossRef
import com.dorianbanic.chat.database.entities.ChatParticipantEntity
import com.dorianbanic.chat.database.view.LastMessageView

@Database(
    entities = [
        ChatEntity::class,
        ChatMessageEntity::class,
        ChatParticipantEntity::class,
        ChatParticipantCrossRef::class,
    ],
    views = [
        LastMessageView::class
    ],
    version = 1,
)
@ConstructedBy(HeyappChatDatabaseConstructor::class)
abstract class HeyappChatDatabase: RoomDatabase() {
    abstract val chatDao: ChatDao
    abstract val chatMessageDao: ChatMessageDao
    abstract val chatParticipantDao: ChatParticipantDao
    abstract val chatParticipantsCrossRefDao: ChatParticipantsCrossRefDao

    companion object {
        const val DB_NAME = "heyapp.db"
    }
}