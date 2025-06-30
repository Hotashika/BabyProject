package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.Users
import kotlinx.coroutines.flow.Flow


@Dao
interface UsersDao : BaseDao<Users>{
    @Query("SELECT * FROM users ORDER BY userId DESC")
    fun getAll(): Flow<List<Users>>
}