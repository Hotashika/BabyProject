package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "notes",
    primaryKeys = ["noteId", "userId"]
)
data class Notes(
    val noteId: String,
    val userId: String,

    var content: String? = null,

    var createdAt: String? = null,
    var updatedAt: String? = null
)