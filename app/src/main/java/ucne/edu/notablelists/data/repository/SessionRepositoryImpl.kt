package ucne.edu.notablelists.data.repository

import ucne.edu.notablelists.data.local.SessionManager
import ucne.edu.notablelists.domain.session.SessionRepository
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionManager: SessionManager
) : SessionRepository {

    override fun getUserSession() = sessionManager.getUser()

    override suspend fun saveUserSession(username: String) {
        sessionManager.saveUser(username)
    }

    override suspend fun clearUserSession() {
        sessionManager.clearUser()
    }
}