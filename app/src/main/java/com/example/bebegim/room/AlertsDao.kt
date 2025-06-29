package com.example.bebegim.room

import androidx.room.Dao
import androidx.room.Query


@Dao
interface AlertsDao {

    @Query("SELECT * FROM alerts")
    suspend fun getAllAlerts(): List<Alerts>

    @Query("SELECT * FROM alerts WHERE alert_id = :id")
    suspend fun getAlertById(id: String): Alerts?

}