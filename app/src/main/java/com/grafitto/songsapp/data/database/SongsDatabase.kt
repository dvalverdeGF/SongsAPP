package com.grafitto.songsapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.grafitto.songsapp.data.database.dao.SongDao
import com.grafitto.songsapp.data.database.dao.VerseDao
import com.grafitto.songsapp.data.database.entity.SongEntity
import com.grafitto.songsapp.data.database.entity.VerseEntity

@Database(
    entities = [SongEntity::class, VerseEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class SongsDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao

    abstract fun verseDao(): VerseDao

    companion object {
        @Suppress("ktlint:standard:property-naming")
        @Volatile
        private var INSTANCE: SongsDatabase? = null

        fun getDatabase(context: Context): SongsDatabase =
            INSTANCE ?: synchronized(this) {
                val instance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            SongsDatabase::class.java,
                            "songs_database",
                        ).build()
                INSTANCE = instance
                instance
            }
    }
}
