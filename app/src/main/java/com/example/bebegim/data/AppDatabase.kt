
package com.example.bebegim.data

import android.content.Context
import androidx.room.*
import java.time.LocalDate

class NoteRepository(private val noteDao: NoteDao) {
    suspend fun getAllNotesForUser(userId: String): List<Note> =
        noteDao.getAllNotesForUser(userId)

    suspend fun deleteNoteByUserAndDate(userId: String, date: LocalDate) =
        noteDao.deleteNoteByUserAndDate(userId, date)

    suspend fun getAllNotes(): List<Note> = noteDao.getAllNotes()
    suspend fun insertNote(note: Note) = noteDao.insertNote(note)
    suspend fun deleteNoteByDate(date: LocalDate) {
        noteDao.getNoteByDate(date)?.let { noteDao.deleteNote(it) }
    }
}
class Converters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? = dateString?.let { LocalDate.parse(it) }
}
@Entity(tableName = "notes", primaryKeys = ["userId", "date"])
data class Note(
    val userId: String,
    val date: LocalDate,
    val content: String
)
@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    suspend fun getAllNotes(): List<Note>

    @Query("SELECT * FROM notes WHERE userId = :userId")
    suspend fun getAllNotesForUser(userId: String): List<Note>

    @Query("SELECT * FROM notes WHERE date = :date")
    suspend fun getNoteByDate(date: LocalDate): Note?

    @Query("DELETE FROM notes WHERE userId = :userId AND date = :date")
    suspend fun deleteNoteByUserAndDate(userId: String, date: LocalDate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}

// Room Database
@Database(entities = [Note::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notes_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}