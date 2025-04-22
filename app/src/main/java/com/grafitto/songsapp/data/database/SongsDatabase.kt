package com.grafitto.songsapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.grafitto.songsapp.data.database.dao.CategoryDao
import com.grafitto.songsapp.data.database.dao.SongDao
import com.grafitto.songsapp.data.database.dao.VerseDao
import com.grafitto.songsapp.data.database.entity.CategoryEntity
import com.grafitto.songsapp.data.database.entity.SongEntity
import com.grafitto.songsapp.data.database.entity.VerseEntity

@Database(
    entities = [SongEntity::class, VerseEntity::class, CategoryEntity::class],
    version = 2,
)
abstract class SongsDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao

    abstract fun verseDao(): VerseDao

    abstract fun categoryDao(): CategoryDao

    companion object {
        @Suppress("ktlint:standard:property-naming")
        @Volatile
        private var INSTANCE: SongsDatabase? = null

        private val MIGRATION_1_2 =
            object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL(
                        "CREATE TABLE IF NOT EXISTS categories (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "name TEXT NOT NULL, " +
                            "description TEXT NOT NULL, " +
                            "parentId INTEGER)",
                    )
                }
            }

        fun getDatabase(context: Context): SongsDatabase =
            INSTANCE ?: synchronized(this) {
                val instance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            SongsDatabase::class.java,
                            "songs_database",
                        ).addMigrations(MIGRATION_1_2)
                        .build()
                INSTANCE = instance
                instance
            }
    }
}
