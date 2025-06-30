package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "babies",
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
    val babyId: String,
    val userId: String,

    val name: String,
    val birthDate: String,

    val gender: String?,

    val currentWeight: Double?,
    val currentHeight: Double?,

    val bloodType: String?,

    val createdAt: String?,
    val updatedAt: String?
)