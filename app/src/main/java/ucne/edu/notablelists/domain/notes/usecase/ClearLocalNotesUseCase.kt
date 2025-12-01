package ucne.edu.notablelists.domain.notes.usecase

import ucne.edu.notablelists.domain.notes.repository.NoteRepository
import javax.inject.Inject

class ClearLocalNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke() {
        repository.clearLocalNotes()
    }
}