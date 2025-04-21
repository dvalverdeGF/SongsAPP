package com.grafitto.songsapp.data.repository

import SongWithVerses
import VerseEntity
import com.grafitto.songsapp.data.model.Song

class SongsRepositoryImpl(
    private val songDao: SongDao,
    private val verseDao: VerseDao,
) : SongsRepository {
    override fun getAllSongs(): Flow<List<Song>> =
        songDao.getAllSongs().map { entities ->
            entities.map { it.toModel() }
        }

    override suspend fun getSongById(id: Int): Song? = songDao.getSongWithVerses(id)?.toModel()

    override suspend fun addSong(song: Song) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSong(song: Song) {
        TODO("Not yet implemented")
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

    // Métodos de extensión para convertir entre entidades y modelos
    private fun SongEntity.toModel(): Song = Song(id, title, artist)

    private fun Song.toEntity(): SongEntity = SongEntity(id, title, artist)

    private fun Verse.toEntity(
        songId: Int,
        position: Int,
    ): VerseEntity = VerseEntity(0, songId, chords, lyrics, position)

    private fun SongWithVerses.toModel(): Song = Song(song.id, song.title, song.artist, verses.map { Verse(it.chords, it.lyrics) })
}
