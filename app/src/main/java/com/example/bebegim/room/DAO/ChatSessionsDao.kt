package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.ChatSessions


@Dao
interface ChatSessionsDao : BaseDao<ChatSessions> {
    @Query("SELECT * FROM chat_sessions WHERE sessionId = :sessionId ORDER BY sessionId DESC")
    suspend fun getSessionById(sessionId: String): ChatSessions?
}