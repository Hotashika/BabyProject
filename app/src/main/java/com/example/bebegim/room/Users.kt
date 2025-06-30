package com.example.bebegim.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Users"
)
data class Users(
    @PrimaryKey val userId: String,
    val email: String,
    val password: String,
    val fullName: String,
    val createdAt: String,
    val updatedAt: String?
)

data class UsersRegister(
    val email: String,
    val password: String,
    val fullName: String,
)

data class UsersLogin(
    val email: String,
    val password: String,
)