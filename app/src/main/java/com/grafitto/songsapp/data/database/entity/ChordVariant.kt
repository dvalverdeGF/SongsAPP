package com.grafitto.songsapp.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "chord_variants",
    foreignKeys = [
        ForeignKey(
            entity = ChordRoot::class,
            parentColumns = ["id"],
            childColumns = ["rootId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ChordAccidental::class,
            parentColumns = ["id"],
            childColumns = ["accidentalId"],
            onDelete = ForeignKey.SET_NULL,
        ),
        ForeignKey(
            entity = ChordQuality::class,
            parentColumns = ["id"],
            childColumns = ["qualityId"],
            onDelete = ForeignKey.SET_NULL,
        ),
        ForeignKey(
            entity = ChordExtension::class,
            parentColumns = ["id"],
            childColumns = ["extensionId"],
            onDelete = ForeignKey.SET_NULL,
        ),
        ForeignKey(
            entity = ChordModifier::class,
            parentColumns = ["id"],
            childColumns = ["modifierId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
)
data class ChordVariant(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rootId: Long, // Clave foránea a ChordRoot
    val accidentalId: Long? = null, // Clave foránea a ChordAccidental
    val qualityId: Long? = null, // Clave foránea a ChordQuality
    val extensionId: Long? = null, // Clave foránea a ChordExtension
    val modifierId: Long? = null, // Clave foránea a ChordModifier
    val name: String, // Ejemplo: "C#m7add9" (Este campo almacenaría el nombre completo)
    val nameLatin: String, // Ejemplo: "Do# menor séptima con novena añadida" (Este campo almacenaría el nombre latino completo)
)
