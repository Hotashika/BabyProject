package com.example.bebegim.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bebegim.room.DAO.BabiesDao
import com.example.bebegim.room.DAO.BabiesDataDao
import com.example.bebegim.room.DAO.NotesDao
import com.example.bebegim.room.DAO.UsersDao
import com.example.bebegim.roomEnhance.SystemLogs


@Database(
    entities = [
        Alerts::class,
        Babies::class,
        BabiesData::class,
        ChatMessages::class,
        ChatSessions::class,
        DevelopmentMilestones::class,
        DocumentEmbeddings::class,
        FeedingRecords::class,
        GrowthMeasurement::class,
        MedicalHistory::class,
        Notes::class,
        SleepRecord::class,
        SystemLogs::class,
        TemperatureReading::class,
        Users::class,
        Vaccinations::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun BabiesDao(): BabiesDao
    abstract fun BabiesDataDao(): BabiesDataDao
    abstract fun NotesDao(): NotesDao
    abstract fun UsersDao(): UsersDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bebegim_db"
                )
                    .fallbackToDestructiveMigration()  // ← Geliştirme aşamasında ekleyin
                    .build().also { INSTANCE = it }
            }
        }
    }
}