package com.example.bebegim.room

import androidx.room.Dao
import androidx.room.Query


@Dao
interface ChatSessionsDao {
    @Query("SELECT * FROM ChatSessions WHERE sessionId = :sessionId")
    suspend fun getSessionById(sessionId: String): ChatSessions?
}