package com.grafitto.songsapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.grafitto.songsapp.data.database.dao.*
import com.grafitto.songsapp.data.database.entity.*

@Database(
    entities = [
        Song::class,
        Verse::class,
        Category::class,
        Author::class,
        LyricAnnotation::class,
        AnnotationSymbol::class,
        ChordAccidental::class,
        ChordExtension::class,
        ChordModifier::class,
        ChordQuality::class,
        ChordRoot::class,
        ChordVariant::class,
        Lyric::class,
        LyricChord::class,
        SongCategory::class,
    ],
    version = 2,
)
abstract class SongsDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao

    abstract fun verseDao(): VerseDao

    abstract fun categoryDao(): CategoryDao

    abstract fun authorDao(): AuthorDao

    abstract fun lyricAnnotationDao(): LyricAnnotationDao

    abstract fun annotationSymbolDao(): AnnotationSymbolDao

    abstract fun chordAccidentalDao(): ChordAccidentalDao

    abstract fun chordExtensionDao(): ChordExtensionDao

    abstract fun chordModifierDao(): ChordModifierDao

    abstract fun chordQualityDao(): ChordQualityDao

    abstract fun chordRootDao(): ChordRootDao

    abstract fun chordVariantDao(): ChordVariantDao

    abstract fun lyricDao(): LyricDao

    abstract fun lyricChordDao(): LyricChordDao

    abstract fun songCategoryDao(): SongCategoryDao

    companion object {
        @Volatile
        private var INSTANCE: SongsDatabase? = null

        private val MIGRATION_1_2 =
            object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    // ...migraciones existentes...
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
