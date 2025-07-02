package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.roomEnhance.SystemLogs
import kotlinx.coroutines.flow.Flow

@Dao
interface SystemLogsDao : BaseDao<SystemLogs> {
    @Query("SELECT * FROM system_logs ORDER BY logId DESC")
    fun getAllSystemLogs(): Flow<List<SystemLogs>>
}