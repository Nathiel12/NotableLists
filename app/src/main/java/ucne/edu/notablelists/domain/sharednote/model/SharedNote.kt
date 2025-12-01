package ucne.edu.notablelists.domain.sharednote.model

data class SharedNote(
    val sharedNoteId: Int = 0,
    val noteId: Int = 0,
    val targetUserId: Int = 0,
    val ownerUserId: Int = 0,
    val status: String = "active"
)