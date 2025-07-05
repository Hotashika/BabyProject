package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.Babies
import kotlinx.coroutines.flow.Flow

@Dao
interface BabiesDao : BaseDao<Babies> {
    @Query("SELECT * FROM babies ORDER BY createdAt DESC")
    fun getAllBabies(): Flow<List<Babies>>

    @Query("SELECT * FROM Babies WHERE userId = :userId")
    fun getBabiesByUserId(userId: String): Flow<List<Babies>>

    @Query("UPDATE babies SET name = :name, birthDate = :birthDate, gender = :gender, currentWeight = :weight, currentHeight = :height, bloodType = :bloodType, updatedAt = :updatedAt WHERE babyId = :babyId")
    suspend fun updateBabyById(
        babyId: String,
        name: String,
        birthDate: String,
        gender: String?,
        weight: Double?,
        height: Double?,
        bloodType: String?,
        updatedAt: String
    )
}