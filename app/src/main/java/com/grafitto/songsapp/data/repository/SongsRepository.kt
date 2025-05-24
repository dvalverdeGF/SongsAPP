package com.grafitto.songsapp.data.repository

import com.grafitto.songsapp.data.model.Song
import kotlinx.coroutines.flow.Flow

interface SongsRepository {
    fun getAllSongs(): Flow<List<Song>>

    suspend fun getSongById(songId: Int): Song?

    suspend fun addSong(song: Song)

    suspend fun updateSong(song: Song)

    suspend fun deleteSong(song: Song)
}
