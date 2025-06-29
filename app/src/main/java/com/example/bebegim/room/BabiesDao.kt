package com.example.bebegim.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BabiesDao {
    @Query("SELECT * FROM Babies WHERE userId = :userId ORDER BY birthDate DESC")
    fun getBabiesByUserId(userId: String): List<Babies>
}