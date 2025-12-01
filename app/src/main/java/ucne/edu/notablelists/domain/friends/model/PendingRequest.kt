package ucne.edu.notablelists.domain.friends.model

data class PendingRequest(
    val id: Int,
    val requesterId: Int,
    val requesterUsername: String
)