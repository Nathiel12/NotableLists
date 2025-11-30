package ucne.edu.notablelists.data.worker

import ucne.edu.notablelists.domain.session.usecase.GetUserIdUseCase
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import ucne.edu.notablelists.data.remote.Resource
import ucne.edu.notablelists.domain.notes.repository.NoteRepository

@HiltWorker

class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val noteRepository: NoteRepository,
    private val getUserIdUseCase: GetUserIdUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val userId = getUserIdUseCase().first()

        return if (userId != null) {
            when (noteRepository.postPendingNotes(userId)) {
                is Resource.Success -> Result.success()
                is Resource.Error -> Result.retry()
                else -> Result.failure()
            }
        } else {
            Result.success()
        }
    }
}