package com.grafitto.songsapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lyric_chords",
    foreignKeys = [
        ForeignKey(
            entity = Verse::class,
            parentColumns = ["id"],
            childColumns = ["verse_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ChordRoot::class,
            parentColumns = ["id"],
            childColumns = ["chord_root_id"],
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
        ForeignKey(
            entity = ChordVariant::class,
            parentColumns = ["id"],
            childColumns = ["variant_id"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [
        Index(value = ["verse_id"]),
        Index(value = ["chord_root_id"]),
        Index(value = ["accidental_id"]),
        Index(value = ["quality_id"]),
        Index(value = ["extension_id"]),
        Index(value = ["modifier_id"]),
        Index(value = ["variant_id"]),
    ],
)
data class LyricChord(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,
    @ColumnInfo(name = "relative_char_position")
    val relativeCharPosition: Float,
    @ColumnInfo(name = "pixel_offset")
    val pixelOffset: Float? = null,
    @ColumnInfo(name = "verse_id")
    val verseId: Long,
    @ColumnInfo(name = "chord_root_id")
    val chordRootId: Long,
    @ColumnInfo(name = "accidental_id")
    val accidentalId: Long? = null,
    @ColumnInfo(name = "quality_id")
    val qualityId: Long? = null,
    @ColumnInfo(name = "extension_id")
    val extensionId: Long? = null,
    @ColumnInfo(name = "modifier_id")
    val modifierId: Long? = null,
    @ColumnInfo(name = "variant_id")
    val variantId: Long? = null,
)
