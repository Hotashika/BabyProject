package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.Babies

@Dao
interface BabiesDao : BaseDao<Babies> {
    @Query("SELECT * FROM Babies WHERE userId = :userId ORDER BY babyId DESC")
    fun getBabiesByUserId(userId: String): List<Babies>
}