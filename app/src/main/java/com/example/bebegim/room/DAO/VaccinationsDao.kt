package com.example.bebegim.room.DAO

import androidx.room.Query
import com.example.bebegim.room.Vaccinations

interface VaccinationsDao : BaseDao<Vaccinations> {
    @Query("SELECT * FROM vaccinations ORDER BY vaccinationId DESC")
    suspend fun getAll(): List<Vaccinations>
}