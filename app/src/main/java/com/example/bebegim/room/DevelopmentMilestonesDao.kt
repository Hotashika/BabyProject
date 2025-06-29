package com.example.bebegim.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DevelopmentMilestonesDao {
    @Query("SELECT * FROM DevelopmentMilestones WHERE milestoneId =:milestoneId")
    suspend fun getMilestoneById(milestoneId: String): DevelopmentMilestones?
}