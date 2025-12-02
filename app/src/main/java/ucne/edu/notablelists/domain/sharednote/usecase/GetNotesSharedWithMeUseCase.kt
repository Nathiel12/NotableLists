package ucne.edu.notablelists.domain.sharednote.usecase

import ucne.edu.notablelists.data.remote.Resource
import ucne.edu.notablelists.data.remote.dto.SharedNoteWithDetailsDto
import ucne.edu.notablelists.domain.notes.model.Note
import ucne.edu.notablelists.domain.sharednote.repository.SharedNoteRepository
import javax.inject.Inject

class GetNotesSharedWithMeUseCase @Inject constructor(
    private val repository: SharedNoteRepository
) {
    suspend operator fun invoke(userId: Int): Resource<List<Note>> {
        return when (val result = repository.getNotesSharedWithMe(userId)) {
            is Resource.Success -> {
                val notes = result.data?.map { it.toDomain() } ?: emptyList()
                Resource.Success(notes)
            }
            is Resource.Error -> Resource.Error(result.message)
            is Resource.Loading -> Resource.Loading()
        }
    }

    private fun SharedNoteWithDetailsDto.toDomain(): Note {
        return Note(
            id = this.noteId.toString(),
            remoteId = this.noteId,
            userId = this.ownerUserId,
            title = this.noteTitle,
            description = this.toString(), //esto está malo y no, este comentario no es de IA
            tag = this.tag ?: "",
            priority = this.priority,
            isFinished = this.isFinished,
            reminder = null,
            checklist = null
        )
    }
}