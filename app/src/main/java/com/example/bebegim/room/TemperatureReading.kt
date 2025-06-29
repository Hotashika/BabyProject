package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "temperature_readings",
    indices = [
        Index(value = ["babyId"]),
        Index(value = ["measuredAt"])
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
data class TemperatureReading(
    @PrimaryKey
    val readingId: String,  // UUID

    val babyId: String,     // FK to Baby

    val temperature: Double,  // numeric(4,1), NOT NULL

    val measuredAt: String,   // ISO timestamp, NOT NULL

    val measurementMethod: String? = "digital",  // varchar(20), default "digital"

    val deviceId: String?,    // varchar(50)

    val location: String?,    // varchar(50)

    val notes: String?,       // TEXT

    val roomTemperature: Double?,  // numeric(4,1)

    val humidity: Double?,         // numeric(5,2)

    val isFever: Boolean = false,  // boolean, default false

    val alertSent: Boolean = false,// boolean, default false

    val createdAt: String?         // ISO timestamp
)
