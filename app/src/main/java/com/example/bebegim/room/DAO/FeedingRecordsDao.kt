package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.FeedingRecords
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedingRecordsDao : BaseDao<FeedingRecords> {
    @Query("SELECT * FROM feeding_records WHERE feedingId = :feedingId ORDER BY feedingId DESC")
    fun getFeedingRecordById(feedingId: String): Flow<FeedingRecords?>
}