package com.dorianbanic.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.dorianbanic.chat.database.entities.ChatParticipantCrossRef
import com.dorianbanic.chat.database.entities.ChatParticipantEntity

@Dao
interface ChatParticipantsCrossRefDao {

    @Upsert
    suspend fun upsertCrossRef(crossRef: List<ChatParticipantCrossRef>)

    @Query("SELECT userId FROM chatparticipantcrossref WHERE chatId = :chatId")
    suspend fun getActiveParticipantIdsByChat(chatId: String): List<String>

    @Query("SELECT userId FROM chatparticipantcrossref WHERE chatId = :chatId")
    suspend fun getAllParticipantIdsByChat(chatId: String): List<String>

    @Query("UPDATE chatparticipantcrossref SET isActive = 0 WHERE chatId = :chatId AND userId IN (:userIds)")
    suspend fun markParticipantsAsInactive(chatId: String, userIds: List<String>)

    @Query("UPDATE chatparticipantcrossref SET isActive = 1 WHERE chatId = :chatId AND userId IN (:userIds)")
    suspend fun reactivateParticipants(chatId: String, userIds: List<String>)

    @Transaction
    suspend fun syncChatParticipants(chatId: String, participants: List<ChatParticipantEntity>) {
        if (participants.isEmpty()) {
            return
        }

        val serverParticipantIds = participants.map { it.userId }.toSet()
        val allLocalParticipants = getAllParticipantIdsByChat(chatId).toSet()
        val activeLocalParticipantIds = getActiveParticipantIdsByChat(chatId).toSet()
        val inactiveLocalParticipantIds = allLocalParticipants - activeLocalParticipantIds


        val participantsToReactive = serverParticipantIds.intersect(inactiveLocalParticipantIds)
        val participantsToDeactivate = activeLocalParticipantIds - serverParticipantIds

        reactivateParticipants(chatId, participantsToReactive.toList())
        markParticipantsAsInactive(chatId, participantsToDeactivate.toList())

        val completelyNewParticipantIds = serverParticipantIds - allLocalParticipants
        val newCrossRefs = completelyNewParticipantIds.map {
            ChatParticipantCrossRef(chatId, it, true)
        }
        upsertCrossRef(newCrossRefs)
    }
}