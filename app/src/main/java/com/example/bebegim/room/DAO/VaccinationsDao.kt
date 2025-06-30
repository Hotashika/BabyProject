package com.example.bebegim.room.DAO

import androidx.room.Query
import com.example.bebegim.room.Vaccinations
import kotlinx.coroutines.flow.Flow

interface VaccinationsDao : BaseDao<Vaccinations> {
    @Query("SELECT * FROM vaccinations ORDER BY vaccinationId DESC")
    fun getAll(): Flow<List<Vaccinations>>
}