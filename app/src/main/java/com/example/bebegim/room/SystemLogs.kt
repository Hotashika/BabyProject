package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "system_logs",
    indices = [
        Index(value = ["userId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = Users::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class SystemLogs(
    @PrimaryKey
    val logId: String,         // UUID

    val userId: String?,       // nullable FK to User

    val action: String,        // varchar(100), not null

    val tableName: String?,    // varchar(50), nullable

    val recordId: String?,     // UUID of affected record, nullable

    val oldValues: String?,    // JSONB as raw JSON string

    val newValues: String?,    // JSONB as raw JSON string

    val ipAddress: String?,    // inet, can be stored as plain string

    val userAgent: String?,    // text

    val createdAt: String?     // ISO 8601 timestamp
)
