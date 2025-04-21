@Entity(
    tableName = "verses",
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("songId")],
)
data class VerseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val songId: Int,
    val chords: String = "",
    val lyrics: String = "",
    val position: Int = 0, // Para mantener el orden
)
