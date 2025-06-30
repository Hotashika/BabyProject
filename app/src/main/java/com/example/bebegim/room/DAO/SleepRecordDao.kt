package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.SleepRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepRecordDao : BaseDao<SleepRecord> {
    @Query("SELECT * FROM sleep_records ORDER BY sleepId DESC")
    fun getAllSleepRecords(): Flow<List<SleepRecord>>
}