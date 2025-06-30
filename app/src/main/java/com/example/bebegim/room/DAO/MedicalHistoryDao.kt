package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.MedicalHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicalHistoryDao : BaseDao<MedicalHistory> {
    @Query("SELECT * FROM medical_history WHERE historyId = :historyId ORDER BY historyId DESC")
    fun getMedicalHistoryById(historyId: String): Flow<MedicalHistory?>
}