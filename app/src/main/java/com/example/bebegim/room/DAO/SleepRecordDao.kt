package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.SleepRecord

@Dao
interface SleepRecordDao : BaseDao<SleepRecord> {
    @Query("SELECT * FROM sleep_records ORDER BY sleepId DESC")
    suspend fun getAllSleepRecords(): List<SleepRecord>
}