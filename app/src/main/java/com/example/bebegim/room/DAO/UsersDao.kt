package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.Users


@Dao
interface UsersDao : BaseDao<Users>{
    @Query("SELECT * FROM users ORDER BY userId DESC")
    suspend fun getAll(): List<Users>
}