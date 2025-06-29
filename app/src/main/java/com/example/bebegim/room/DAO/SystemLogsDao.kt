package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.SystemLogs

@Dao
interface SystemLogsDao : BaseDao<SystemLogs> {
    @Query("SELECT * FROM system_logs ORDER BY logId DESC")
    suspend fun getAllSystemLogs(): List<SystemLogs>
}