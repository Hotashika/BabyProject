package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sleep_records",
    indices = [
        Index(value = ["babyId"]),
        Index(value = ["sleepStart"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = Babies::class,
            parentColumns = ["babyId"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SleepRecord(
    @PrimaryKey
    val sleepId: String,  // UUID

    val babyId: String,   // FK to Baby

    val sleepStart: String,  // ISO-8601 timestamp, not null
    val sleepEnd: String?,   // ISO-8601 timestamp, nullable

    val durationMinutes: Int?,  // nullable

    val sleepQuality: String?,  // varchar(20)
    val location: String?,      // varchar(50)

    val interruptions: Int = 0, // default 0

    val notes: String?,         // text

    val createdAt: String?      // timestamp with timezone, ISO string
)
