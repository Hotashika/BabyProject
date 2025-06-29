package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index(value = ["babyId"]),
        Index(value = ["feedingTime"])
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
data class FeedingRecords(
    @PrimaryKey
    val feedingId: String,     // UUID

    val babyId: String,        // FK to Baby

    val feedingTime: String,   // timestamp with time zone, ISO 8601 format, NOT NULL

    val feedingType: String,   // varchar(20), NOT NULL

    val amountMl: Int?,        // integer, nullable

    val durationMinutes: Int?, // integer, nullable

    val foodType: String?,     // varchar(100), nullable

    val notes: String?,        // text, nullable

    val createdAt: String?     // timestamp with time zone, ISO string
)
