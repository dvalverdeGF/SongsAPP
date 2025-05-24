package com.grafitto.songsapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "song_categories",
    primaryKeys = ["song_id", "category_id"],
    foreignKeys = [
        ForeignKey(
            entity = Song::class,
            parentColumns = ["id"],
            childColumns = ["song_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Category::class, // Asumiendo que tienes una entidad Category.kt
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["song_id"]), Index(value = ["category_id"])],
)
data class SongCategory(
    @ColumnInfo(name = "song_id")
    val songId: Long, // Clave foránea a Song
    @ColumnInfo(name = "category_id")
    val categoryId: Long, // Clave foránea a Category
)
