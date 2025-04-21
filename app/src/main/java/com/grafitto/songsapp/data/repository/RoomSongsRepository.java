package com.grafitto.songsapp.data.repository;

import com.grafitto.songsapp.data.database.dao.SongDao;
import com.grafitto.songsapp.data.database.dao.VerseDao;

public class RoomSongsRepository(
        private val songDao:SongDao,
        private val verseDao:VerseDao
)
