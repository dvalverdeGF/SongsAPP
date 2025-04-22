package com.grafitto.songsapp

import android.app.Application
import com.grafitto.songsapp.data.database.SongsDatabase
import com.grafitto.songsapp.data.repository.SongsRepository
import com.grafitto.songsapp.data.repository.SongsRepositoryImpl

class SongsApplication : Application() {
    val database by lazy { SongsDatabase.getDatabase(this) }
    val repository: SongsRepository by lazy {
        SongsRepositoryImpl(database.songDao(), database.verseDao(), database.categoryDao())
    }
}
