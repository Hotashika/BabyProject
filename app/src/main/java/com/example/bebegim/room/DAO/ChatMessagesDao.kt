package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.ChatMessages
@Dao
interface ChatMessagesDao : BaseDao<ChatMessages> {
    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId ORDER BY sessionId ASC")
    fun getMessagesBySessionId(sessionId: String): List<ChatMessages>
}