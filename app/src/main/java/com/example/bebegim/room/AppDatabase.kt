package com.example.bebegim.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bebegim.room.DAO.*
import com.example.bebegim.room.DAO.AlertsDao
import com.example.bebegim.room.DAO.BabiesDao
import com.example.bebegim.room.DAO.BabiesDataDao
import com.example.bebegim.room.DAO.ChatMessagesDao
import com.example.bebegim.room.DAO.ChatSessionsDao
import com.example.bebegim.room.DAO.DevelopmentMilestonesDao
import com.example.bebegim.room.DAO.DocumentEmbeddingsDao
import com.example.bebegim.room.DAO.FeedingRecordsDao
import com.example.bebegim.room.DAO.GrowthMeasurementDao
import com.example.bebegim.room.DAO.MedicalHistoryDao
import com.example.bebegim.room.DAO.SleepRecordDao
import com.example.bebegim.roomEnhance.SystemLogs

@Database(
    entities = [
        Alerts::class,
        Babies::class,
        ChatMessages::class,
        BabiesData::class,
        ChatMessages::class ,
        ChatSessions::class,
        DevelopmentMilestones::class,
        DocumentEmbeddings::class,
        FeedingRecords::class,
        GrowthMeasurement::class,
        MedicalHistory::class,
        SleepRecord::class,
        SystemLogs::class,
        TemperatureReading::class,
        Users::class,
        Vaccinations::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun BabiesDao(): BabiesDao
    abstract fun BabiesDataDao(): BabiesDataDao
/*    abstract fun ChatMessagesDao(): ChatMessagesDao
    abstract fun ChatSessionsDao(): ChatSessionsDao
    abstract fun FeedingRecordsDao(): FeedingRecordsDao*/
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
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}