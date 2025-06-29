package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.Alerts


@Dao
interface AlertsDao : BaseDao<Alerts> {

    @Query("SELECT * FROM alerts")
    suspend fun getAllAlerts(): List<Alerts>

    @Query("SELECT * FROM alerts WHERE alert_id = :id")
    suspend fun getAlertById(id: String): Alerts?

}