package ucne.edu.notablelists.domain.session

import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getUserSession(): Flow<String?>
    suspend fun saveUserSession(username: String)
    suspend fun clearUserSession()
}