package com.grafitto.songsapp.data.database

@Database(entities = [SongEntity::class, VerseEntity::class], version = 1)
abstract class SongsDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao

    abstract fun verseDao(): VerseDao

    companion object {
        @Volatile
        private var INSTANCE: SongsDatabase? = null

        fun getDatabase(context: Context): SongsDatabase =
            INSTANCE ?: synchronized(this) {
                Room
                    .databaseBuilder(
                        context.applicationContext,
                        SongsDatabase::class.java,
                        "songs_database",
                    ).build()
                    .also { INSTANCE = it }
            }
    }
}
