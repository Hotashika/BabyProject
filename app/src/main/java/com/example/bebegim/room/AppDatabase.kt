package com.example.bebegim.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bebegim.room.DAO.AlertsDao
import com.example.bebegim.room.DAO.BabiesDao
import com.example.bebegim.room.DAO.ChatMessagesDao
import com.example.bebegim.room.DAO.ChatSessionsDao
import com.example.bebegim.room.DAO.DevelopmentMilestonesDao
import com.example.bebegim.room.DAO.DocumentEmbeddingsDao
import com.example.bebegim.room.DAO.FeedingRecordsDao
import com.example.bebegim.room.DAO.GrowthMeasurementDao
import com.example.bebegim.room.DAO.MedicalHistoryDao
import com.example.bebegim.room.DAO.SleepRecordDao
import com.example.bebegim.room.DAO.SystemLogsDao
import com.example.bebegim.room.DAO.TemperatureReadingDao
import com.example.bebegim.room.DAO.UsersDao
import com.example.bebegim.room.DAO.VaccinationsDao

@Database(
    entities = [
        Alerts::class,
        Babies::class,
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
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

/*    abstract fun AlertsDao (): AlertsDao*/
    abstract fun BabiesDao(): BabiesDao
/*    abstract fun ChatMessagesDao(): ChatMessagesDao
    abstract fun ChatSessionsDao(): ChatSessionsDao
    abstract fun FeedingRecordsDao(): FeedingRecordsDao*/
    abstract fun UsersDao(): UsersDao
/*    abstract fun DevelopmentMilestonesDao(): DevelopmentMilestonesDao
    abstract fun DocumentEmbeddingsDao(): DocumentEmbeddingsDao
    abstract fun GrowthMeasurementDao(): GrowthMeasurementDao
    abstract fun MedicalHistoryDao(): MedicalHistoryDao
    abstract fun SleepRecordDao(): SleepRecordDao
    abstract fun SystemLogsDao(): SystemLogsDao
    abstract fun TemperatureReadingDao(): TemperatureReadingDao
    abstract fun VaccinationsDao(): VaccinationsDao*/

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