package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "growth_measurements",
    indices = [
        Index(value = ["babyId"]) ,
        Index(value = ["measurementDate"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = Babies::class ,
            parentColumns = ["babyId"] ,
            childColumns = ["babyId"] ,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GrowthMeasurement(
    @PrimaryKey
    val measurementId: String, // UUID

    val babyId: String,              // FK to Baby

    val measurementDate: String,    // ISO date (YYYY-MM-DD)

    val weight: Double?,            // numeric(5,2)
    val height: Double?,            // numeric(5,2)
    val headCircumference: Double?,// numeric(5,2)

    val percentileWeight: Int?,     // optional
    val percentileHeight: Int?,     // optional
    val percentileHead: Int?,       // optional

    val notes: String?,             // TEXT
    val measuredBy: String?,        // varchar(100)

    val createdAt: String?          // ISO timestamp
)
