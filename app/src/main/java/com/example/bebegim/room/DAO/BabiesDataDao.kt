package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bebegim.room.Babies
import com.example.bebegim.room.BabiesData
import kotlinx.coroutines.flow.Flow

@Dao
interface BabiesDataDao : BaseDao<BabiesData> {
    @Query("SELECT * FROM babiesdata ORDER BY createdAt DESC")
    fun getAllBabies(): Flow<List<Babies>>

    @Query("SELECT * FROM babiesdata WHERE babyId = :babyId")
    fun getBabiesByUserId(babyId: String): Flow<List<BabiesData>>

    @Query("INSERT INTO babiesdata(babyId, weight, height, createdAt, updatedAt) VALUES (:babyId, :weight, :height, :createdAt, :updatedAt)")
    suspend fun insertBabiesData(
        babyId: String,
        weight: Double?,
        height: Double?,
        createdAt: String?,
        updatedAt: String?
    )
}