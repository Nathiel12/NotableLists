package ucne.edu.notablelists.domain.notification

import javax.inject.Inject

class CancelReminderUseCase @Inject constructor(
    private val reminderScheduler: ReminderScheduler
) {
    operator fun invoke(noteId: String) {
        reminderScheduler.cancel(noteId)
    }
}