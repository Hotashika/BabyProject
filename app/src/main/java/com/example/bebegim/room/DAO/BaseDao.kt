package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bebegim.room.Users

@Dao
interface BaseDao<T> {
    @Insert
    suspend fun insert(entity: T)

    @Update
    suspend fun update(entity: T)

    @Delete
    suspend fun delete(entity: T)
}