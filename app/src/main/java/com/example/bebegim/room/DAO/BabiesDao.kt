package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bebegim.room.Babies
import kotlinx.coroutines.flow.Flow

@Dao
interface BabiesDao {
    @Insert
    suspend fun insertBaby(baby: Babies)

    @Query("SELECT * FROM babies")
    fun getAllBabies(): Flow<List<Babies>>
}