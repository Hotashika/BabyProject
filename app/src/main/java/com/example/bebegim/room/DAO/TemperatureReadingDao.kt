package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.TemperatureReading
import kotlinx.coroutines.flow.Flow


@Dao
interface TemperatureReadingDao : BaseDao<TemperatureReading> {
    @Query("SELECT * FROM temperature_readings ORDER BY readingId DESC LIMIT 1")
    fun getLatestTemperatureReading(): Flow<TemperatureReading?>

}