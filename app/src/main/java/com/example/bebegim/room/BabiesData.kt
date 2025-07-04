package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.UUID

@Entity(
    tableName = "babiesdata",
    indices = [Index(value = ["babyId"])],
    foreignKeys = [
        ForeignKey(
            entity = Babies::class,
            parentColumns = ["babyId"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
@TypeConverters(BabiesDataConverters::class)
data class BabiesData(
    @PrimaryKey
    val dataId: String = UUID.randomUUID().toString(),
    val babyId: String,

    val weightHistory: List<Double>,
    val heightHistory: List<Double>,

    val createdAt: String?,
    val updatedAt: String?
)

class BabiesDataConverters {
    @TypeConverter
    fun fromDoubleList(list: List<Double>?): String =
        list?.joinToString(",") ?: ""

    @TypeConverter
    fun toDoubleList(data: String?): List<Double> =
        data?.split(",")?.filter { it.isNotBlank() }?.map { it.toDoubleOrNull() ?: 0.0 } ?: emptyList()
}
