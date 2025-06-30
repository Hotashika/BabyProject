package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bebegim.room.Babies
import kotlinx.coroutines.flow.Flow

@Dao
interface BabiesDao : BaseDao<Babies> {
    @Query("SELECT * FROM babies")
    fun getAllBabies(): Flow<List<Babies>>

    @Query("SELECT * FROM Babies WHERE userId = :userId")
    fun getBabiesByUserId(userId: String): Flow<List<Babies>>
    @Query("UPDATE babies SET name = :name, birthDate = :birthDate WHERE babyId = :babyId")
    suspend fun updateBabyById(babyId: String, name: String, birthDate: String)
}