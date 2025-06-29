package com.example.bebegim.room

import androidx.room.Dao
import androidx.room.Query


@Dao
interface ChatMessagesDao {
    @Query("SELECT * FROM ChatMessages WHERE sessionId = :sessionId ORDER BY createdAt ASC")
    fun getMessagesBySessionId(sessionId: String): List<ChatMessages>
}