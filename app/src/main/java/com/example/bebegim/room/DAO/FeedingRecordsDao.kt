package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.FeedingRecords

@Dao
interface FeedingRecordsDao : BaseDao<FeedingRecords> {
    @Query("SELECT * FROM feeding_records WHERE feedingId = :feedingId ORDER BY feedingId DESC")
    suspend fun getFeedingRecordById(feedingId: String): FeedingRecords?
}