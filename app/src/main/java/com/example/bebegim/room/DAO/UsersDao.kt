package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bebegim.room.Users
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {
    @Query("SELECT * FROM Users ORDER BY createdAt DESC")
    fun getAllUsers(): Flow<List<Users>>

    @Query("SELECT * FROM Users WHERE userId = :userId LIMIT 1")
    suspend fun getUserById(userId: String): Users?

    @Query("SELECT * FROM Users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): Users?

    @Insert
    suspend fun insertUser(user: Users)

    @Update
    suspend fun updateUser(user: Users)
}