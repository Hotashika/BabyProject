package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "development_milestones",
    indices = [Index(value = ["babyId"])],
    foreignKeys = [
        ForeignKey(
            entity = Babies::class,
            parentColumns = ["babyId"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DevelopmentMilestones(
    @PrimaryKey
    val milestoneId: String, // UUID

    val babyId: String,       // FK to Baby

    val milestoneType: String,  // varchar(50), NOT NULL
    val milestoneName: String,  // varchar(200), NOT NULL

    val expectedAgeMonths: Int?, // nullable
    val achievedDate: String?,   // date, ISO string "YYYY-MM-DD", nullable

    val notes: String?,          // text, nullable

    val achieved: Boolean = false, // default false

    val createdAt: String?       // timestamp with time zone, ISO string nullable
)
