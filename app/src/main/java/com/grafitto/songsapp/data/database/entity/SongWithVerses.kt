data class SongWithVerses(
    @Embedded val song: SongEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "songId",
    )
    val verses: List<VerseEntity>,
)
