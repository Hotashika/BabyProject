package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["username"], unique = true)
    ]
)
data class Users(
    @PrimaryKey
    val userId: String, // UUID olarak saklanır

    val username: String,
    val email: String,
    val fullName: String?,
    val phone: String?,

    val createdAt: String?, // ISO tarih formatı tavsiye edilir (örnek: "2025-06-28T10:36:12Z")
    val updatedAt: String?
)
