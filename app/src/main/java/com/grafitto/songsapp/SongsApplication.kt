package com.grafitto.songsapp

import android.app.Application
import com.grafitto.songsapp.data.database.SongsDatabase

class SongsApplication : Application() {
    val database by lazy { SongsDatabase.getDatabase(this) }
    val repository: SongsRepository by lazy {
        SongsRepositoryImpl(database.songDao(), database.verseDao(), database.categoryDao())
    }
}
