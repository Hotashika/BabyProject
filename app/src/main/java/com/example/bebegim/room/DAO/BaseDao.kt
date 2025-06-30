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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<T>): List<Long>

    @Update
    suspend fun update(item: T)

    @Delete
    suspend fun delete(item: T)
    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    fun getUserById(userId: String): kotlinx.coroutines.flow.Flow<Users?>
}
