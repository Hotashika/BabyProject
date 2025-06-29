package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vaccinations",
    indices = [
        Index(value = ["babyId"]),
        Index(value = ["vaccinationDate"])
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
data class Vaccinations(
    @PrimaryKey
    val vaccinationId: String,     // UUID

    val babyId: String,            // FK to Baby

    val vaccineName: String,       // varchar(100), NOT NULL

    val vaccinationDate: String,   // date, ISO format, NOT NULL

    val doseNumber: Int? = 1,      // integer, default 1

    val batchNumber: String?,      // varchar(50)

    val healthcareProvider: String?, // varchar(200)

    val hospital: String?,         // varchar(200)

    val doctorName: String?,       // varchar(100)

    val notes: String?,            // text

    val nextDoseDate: String?,     // date, ISO format

    val sideEffects: String?,      // text

    val createdAt: String?         // timestamp with time zone, ISO string
)
