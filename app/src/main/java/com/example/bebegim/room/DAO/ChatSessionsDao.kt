package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.ChatSessions
import kotlinx.coroutines.flow.Flow


@Dao
interface ChatSessionsDao : BaseDao<ChatSessions> {
    @Query("SELECT * FROM chat_sessions WHERE sessionId = :sessionId ORDER BY sessionId DESC")
    fun getSessionById(sessionId: String): Flow<ChatSessions?>
}