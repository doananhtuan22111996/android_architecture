package vn.geekup.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import vn.geekup.app.data.dao.RemoteKeyDao
import vn.geekup.app.data.dao.TravelDao
import vn.geekup.app.data.dao.converter.MomentTypeConverter
import vn.geekup.app.domain.model.general.RemoteKey
import vn.geekup.app.domain.model.moment.MomentModel

@Database(
    entities = [MomentModel::class, RemoteKey::class],
    version = AppDatabase.DB_VERSION,
    exportSchema = false
)
//@TypeConverters(MomentTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_VERSION = 1
        private const val DB_NAME = "Skeleton.DB"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = instance ?: synchronized(this) {
            instance ?: build(context).also { instance = it }
        }

        private fun build(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                .addMigrations(MIGRATION_1_TO_2)
                .build()

        private val MIGRATION_1_TO_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
    }

    abstract fun remoteKeyDao(): RemoteKeyDao
    abstract fun travelFeedDao(): TravelDao
}