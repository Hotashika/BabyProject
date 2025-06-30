package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.DevelopmentMilestones
import kotlinx.coroutines.flow.Flow

@Dao
interface DevelopmentMilestonesDao : BaseDao<DevelopmentMilestones> {
    @Query("SELECT * FROM development_milestones WHERE milestoneId =:milestoneId ORDER BY milestoneId DESC")
    fun getMilestoneById(milestoneId: String): Flow<DevelopmentMilestones?>
}