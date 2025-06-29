package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "medical_history",
    indices = [
        Index(value = ["babyId"])
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
    data class MedicalHistory(
    @PrimaryKey
    val historyId: String,         // UUID

    val babyId: String,            // FK to Baby

    val condition: String,         // varchar(200), NOT NULL

    val diagnosisDate: String?,    // date, nullable, ISO "YYYY-MM-DD"

    val description: String?,      // text, nullable

    val treatment: String?,        // text, nullable

    val doctorName: String?,       // varchar(100), nullable

    val hospital: String?,         // varchar(200), nullable

    val medication: String?,       // text, nullable

    val followUpDate: String?,     // date, nullable, ISO "YYYY-MM-DD"

    val status: String? = "active",// varchar(20), default "active"

    val severity: String?,         // varchar(20), nullable

    val createdAt: String?         // timestamp with timezone ISO string
)
