package com.grafitto.songsapp

// SongsApplication.kt
class SongsApplication : Application() {
    val database by lazy { SongsDatabase.getDatabase(this) }
    val repository by lazy { SongsRepositoryImpl(database.songDao(), database.verseDao()) }
}
