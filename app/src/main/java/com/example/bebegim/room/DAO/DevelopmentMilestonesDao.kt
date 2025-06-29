package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.DevelopmentMilestones

@Dao
interface DevelopmentMilestonesDao : BaseDao<DevelopmentMilestones> {
    @Query("SELECT * FROM development_milestones WHERE milestoneId =:milestoneId ORDER BY milestoneId DESC")
    suspend fun getMilestoneById(milestoneId: String): DevelopmentMilestones?
}