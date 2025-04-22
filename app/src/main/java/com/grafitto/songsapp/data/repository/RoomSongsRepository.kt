package com.grafitto.songsapp.data.repository

import com.grafitto.songsapp.data.database.dao.SongDao
import com.grafitto.songsapp.data.database.dao.VerseDao
import com.grafitto.songsapp.data.database.entity.SongEntity
import com.grafitto.songsapp.data.database.entity.VerseEntity
import com.grafitto.songsapp.data.database.relation.SongWithVerses
import com.grafitto.songsapp.data.model.Song
import com.grafitto.songsapp.data.model.Verse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomSongsRepository(
    private val songDao: SongDao,
    private val verseDao: VerseDao,
) : SongsRepository {
    override fun getAllSongs(): Flow<List<Song>> =
        songDao.getAllSongs().map { entities ->
            entities.map { entity ->
                Song(entity.id, entity.title, entity.artist)
            }
        }

    override suspend fun getSongById(id: Int): Song? {
        val songWithVerses = songDao.getSongWithVerses(id) ?: return null
        return songWithVerses.toModel()
    }

    override suspend fun addSong(song: Song) {
        val songId = songDao.insertSong(song.toEntity()).toInt()

        val versesWithSongId =
            song.verses.mapIndexed { index, verse ->
                verse.toEntity(songId, index)
            }
        verseDao.insertVerses(versesWithSongId)
    }

    override suspend fun updateSong(song: Song) {
        songDao.updateSong(song.toEntity())
        verseDao.deleteVersesBySongId(song.id)

        val versesWithSongId =
            song.verses.mapIndexed { index, verse ->
                verse.toEntity(song.id, index)
            }
        verseDao.insertVerses(versesWithSongId)
    }

    override suspend fun clearSongs() {
        songDao.deleteAllSongs()
        verseDao.deleteAllVerses()
    }

    // Métodos de conversión
    private fun SongEntity.toModel(): Song = Song(id, title, artist)

    private fun Song.toEntity(): SongEntity = SongEntity(id, title, artist)

    private fun Verse.toEntity(
        songId: Int,
        position: Int,
    ): VerseEntity = VerseEntity(0, songId, chords, lyrics, position)

    private fun SongWithVerses.toModel(): Song =
        Song(
            song.id,
            song.title,
            song.artist,
            verses.map { Verse(it.chords, it.lyrics) },
        )
}
