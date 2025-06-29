package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.bebegim.room.MedicalHistory

@Dao
interface MedicalHistoryDao : BaseDao<MedicalHistory> {
    @Query("SELECT * FROM medical_history WHERE historyId = :historyId ORDER BY historyId DESC")
    suspend fun getMedicalHistoryById(historyId: String): MedicalHistory?
}