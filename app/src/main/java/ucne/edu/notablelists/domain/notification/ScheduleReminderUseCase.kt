package ucne.edu.notablelists.domain.notification

import java.time.LocalDateTime
import javax.inject.Inject

class ScheduleReminderUseCase @Inject constructor(
    private val reminderScheduler: ReminderScheduler
) {
    operator fun invoke(noteId: String, title: String, time: LocalDateTime) {
        reminderScheduler.schedule(noteId, title, time)
    }
}