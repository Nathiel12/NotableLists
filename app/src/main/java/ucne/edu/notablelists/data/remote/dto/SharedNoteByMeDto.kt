package ucne.edu.notablelists.data.remote.dto

data class SharedNoteByMeDto(
    val sharedNoteId: Int,
    val noteId: Int,
    val noteTitle: String,
    val targetUserId: Int,
    val targetUsername: String,
    val status: String
)