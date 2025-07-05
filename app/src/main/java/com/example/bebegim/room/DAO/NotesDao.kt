package com.example.bebegim.room.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bebegim.room.Notes
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao: BaseDao<Notes> {
    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<Notes>>

    @Query("SELECT * FROM notes WHERE userId = :userId ORDER BY createdAt DESC")
    fun getNotesByUserId(userId: String): Flow<List<Notes>>

    @Insert
    suspend fun insertNote(note: Notes)

    @Update
    suspend fun updateNote(note: Notes)
}