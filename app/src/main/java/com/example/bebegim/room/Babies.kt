package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["userId"])],
    foreignKeys = [
        ForeignKey(
            entity = Users::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Babies(
    @PrimaryKey
    val babyId: String, // UUID

    val userId: String, // foreign key (UUID)

    val name: String,
    val birthDate: String, // ISO-8601 format: "YYYY-MM-DD"

    val gender: String?, // only "male" or "female"

    val currentWeight: Double?, // numeric(5,2)
    val currentHeight: Double?, // numeric(5,2)

    val bloodType: String?, // like "A+", "O-", etc.

    val allergies: String?, // TEXT
    val medicalNotes: String?, // TEXT

    val createdAt: String?, // timestamp with time zone
    val updatedAt: String?  // timestamp with time zone
)
