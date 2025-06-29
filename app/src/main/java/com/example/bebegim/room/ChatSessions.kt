package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_sessions",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["isActive"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = Users::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Babies::class,
            parentColumns = ["babyId"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class ChatSessions(
    @PrimaryKey
    val sessionId: String, // UUID

    val userId: String,    // FK to users
    val babyId: String?,   // FK to babies (nullable)

    val sessionName: String?,    // optional session name

    val startedAt: String?,      // ISO timestamp string
    val endedAt: String?,        // ISO timestamp string

    val totalMessages: Int = 0,  // default 0

    val sessionData: String?,    // JSONB -> raw JSON string

    val isActive: Boolean = true, // default true

    val createdAt: String?,      // ISO timestamp
    val updatedAt: String?       // ISO timestamp
)
