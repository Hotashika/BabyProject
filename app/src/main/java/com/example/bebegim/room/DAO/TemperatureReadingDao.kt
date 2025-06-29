package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.TemperatureReading


@Dao
interface TemperatureReadingDao : BaseDao<TemperatureReading> {
    @Query("SELECT * FROM temperature_readings ORDER BY readingId DESC LIMIT 1")
    suspend fun getLatestTemperatureReading(): TemperatureReading?

}