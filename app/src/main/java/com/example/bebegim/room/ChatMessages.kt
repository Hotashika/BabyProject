package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_messages",
    indices = [
        Index(value = ["sessionId"]),
        Index(value = ["createdAt"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = ChatSessions::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ChatMessages(
    @PrimaryKey
    val messageId: String, // UUID

    val sessionId: String, // FK to ChatSession

    val userMessage: String,
    val botResponse: String,

    val messageContext: String?, // JSON (as raw String)
    val ragSources: String?,     // JSON (as raw String)

    val responseTimeMs: Int?,    // nullable
    val feedbackRating: Int?,    // 1 to 5
    val feedbackText: String?,   // optional text

    val createdAt: String?       // ISO timestamp string
)
