package com.grafitto.songsapp.data.database.entity

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val artist: String,
)
