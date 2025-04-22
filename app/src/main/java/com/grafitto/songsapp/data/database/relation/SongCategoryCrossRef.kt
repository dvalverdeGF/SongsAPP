package com.grafitto.songsapp.data.database.relation

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.grafitto.songsapp.data.database.entity.CategoryEntity
import com.grafitto.songsapp.data.database.entity.SongEntity

/**
 * Represents a many-to-many relationship between songs and categories.
 *
 * @property songId The ID of the song.
 * @property categoryId The ID of the category.
 */
@Entity(
    tableName = "song_category_cross_ref",
    primaryKeys = ["songId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("songId"),
        Index("categoryId"),
    ],
)
data class SongCategoryCrossRef(
    val songId: Int,
    val categoryId: Int,
)
