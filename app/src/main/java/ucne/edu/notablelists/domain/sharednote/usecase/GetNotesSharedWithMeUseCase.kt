package ucne.edu.notablelists.domain.sharednote.usecase

import ucne.edu.notablelists.data.mappers.toDomainNote
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
                val notes = result.data?.map { it.toDomainNote() } ?: emptyList()
                Resource.Success(notes)
            }
            is Resource.Error -> Resource.Error(result.message)
            is Resource.Loading -> Resource.Loading()
        }
    }
}