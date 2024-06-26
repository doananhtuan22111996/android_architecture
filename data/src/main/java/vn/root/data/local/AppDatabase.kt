package vn.root.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import vn.root.data.local.dao.ItemDao
import vn.root.data.model.ItemRaw

@Database(
	entities = [ItemRaw::class], version = AppDatabase.DB_VERSION, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
	
	companion object {
		const val DB_VERSION = 1
		private const val DB_NAME = "root.db"
		
		@Volatile
		private var instance: AppDatabase? = null
		
		fun getInstance(context: Context): AppDatabase = instance ?: synchronized(this) {
			instance ?: build(context).also { instance = it }
		}
		
		private fun build(context: Context) =
			Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
				.addMigrations(MIGRATION_1_TO_2).build()
		
		private val MIGRATION_1_TO_2 = object : Migration(1, 2) {
			override fun migrate(database: SupportSQLiteDatabase) {
				// TODO handle migrate
			}
		}
	}
	
	abstract fun itemDao(): ItemDao
}