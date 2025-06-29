package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.GrowthMeasurement


@Dao
interface GrowthMeasurementDao : BaseDao<GrowthMeasurement> {
    @Query("SELECT * FROM growth_measurements WHERE measurementId = :measurementId ORDER BY measurementId DESC")
    suspend fun getGrowthMeasurementById(measurementId: String): GrowthMeasurement?
}