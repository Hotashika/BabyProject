package com.example.bebegim.model

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val babyName: String?,
    val babyAge: String?
)
