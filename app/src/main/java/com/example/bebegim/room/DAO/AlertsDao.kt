package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.Alerts


@Dao
interface AlertsDao : BaseDao<Alerts> {

    @Query("SELECT * FROM alerts")
    fun getAllAlerts(): kotlinx.coroutines.flow.Flow<List<Alerts>>

    @Query("SELECT * FROM alerts WHERE alert_id = :id")
    fun getAlertById(id: String): kotlinx.coroutines.flow.Flow<Alerts?>
}