package com.grafitto.songsapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chord_variants",
    foreignKeys = [
        ForeignKey(
            entity = ChordRoot::class,
            parentColumns = ["id"],
            childColumns = ["root_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ChordAccidental::class,
            parentColumns = ["id"],
            childColumns = ["accidental_id"],
            onDelete = ForeignKey.SET_NULL,
        ),
        ForeignKey(
            entity = ChordQuality::class,
            parentColumns = ["id"],
            childColumns = ["quality_id"],
            onDelete = ForeignKey.SET_NULL,
        ),
        ForeignKey(
            entity = ChordExtension::class,
            parentColumns = ["id"],
            childColumns = ["extension_id"],
            onDelete = ForeignKey.SET_NULL,
        ),
        ForeignKey(
            entity = ChordModifier::class,
            parentColumns = ["id"],
            childColumns = ["modifier_id"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [
        Index(value = ["root_id"]),
        Index(value = ["accidental_id"]),
        Index(value = ["quality_id"]),
        Index(value = ["extension_id"]),
        Index(value = ["modifier_id"]),
    ],
)
data class ChordVariant(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "root_id")
    val rootId: Long,
    @ColumnInfo(name = "accidental_id")
    val accidentalId: Long? = null,
    @ColumnInfo(name = "quality_id")
    val qualityId: Long? = null,
    @ColumnInfo(name = "extension_id")
    val extensionId: Long? = null,
    @ColumnInfo(name = "modifier_id")
    val modifierId: Long? = null,
    val name: String,
    val nameLatin: String,
)
