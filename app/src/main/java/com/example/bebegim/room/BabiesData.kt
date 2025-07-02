package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "BabiesData",
    indices = [Index(value = ["babyId"])],
    foreignKeys = [
        ForeignKey(
            entity = Babies::class,
            parentColumns = ["BabyId"],
            childColumns = ["BabyId"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class BabiesData(
    @PrimaryKey
    val dataId: String = UUID.randomUUID().toString(),
    val babyId: String,

    val weight: Double?,
    val height: Double?,

    val createdAt: String?,
    val updatedAt: String?
)