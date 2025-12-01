package ucne.edu.notablelists.data.remote.DataSource

import ucne.edu.notablelists.data.remote.Resource
import ucne.edu.notablelists.data.remote.NoteApiService
import ucne.edu.notablelists.data.remote.dto.NoteRequestDto
import ucne.edu.notablelists.data.remote.dto.NoteResponseDto
import ucne.edu.notablelists.data.remote.dto.ShareRequestDto
import ucne.edu.notablelists.data.remote.dto.ShareResponseDto
import ucne.edu.notablelists.data.remote.dto.SharedNoteByMeDto
import ucne.edu.notablelists.data.remote.dto.SharedNoteWithDetailsDto
import ucne.edu.notablelists.data.remote.dto.UpdateSharedStatusResponseDto
import javax.inject.Inject

class NoteRemoteDataSource @Inject constructor(
    private val api: NoteApiService
) {
    suspend fun getNotes(): Resource<List<NoteResponseDto>> {
        return try {
            val response = api.getNotes()
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun getNoteById(id: Int): Resource<NoteResponseDto> {
        return try {
            val response = api.getNoteById(id)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun createNote(request: NoteRequestDto): Resource<NoteResponseDto> {
        return try {
            val response = api.createNote(request)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun updateNote(id: Int, request: NoteRequestDto): Resource<Unit> {
        return try {
            val response = api.updateNote(id, request)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }

    suspend fun deleteNote(id: Int): Resource<Unit> {
        return try {
            val response = api.deleteNote(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red")
        }
    }
    suspend fun getUserNotes(userId: Int): Resource<List<NoteResponseDto>> {
        return try {
            val response = api.getUserNotes(userId)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Empty response")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun createUserNote(userId: Int, request: NoteRequestDto): Resource<NoteResponseDto> {
        return try {
            val response = api.createUserNote(userId, request)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Empty response")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun updateUserNote(userId: Int, id: Int, request: NoteRequestDto): Resource<Unit> {
        return try {
            val response = api.updateUserNote(userId, id, request)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun deleteUserNote(userId: Int, id: Int): Resource<Unit> {
        return try {
            val response = api.deleteUserNote(userId, id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }
    suspend fun shareNoteWithFriend(userId: Int, noteId: Int, friendId: Int): Resource<ShareResponseDto> {
        return try {
            val response = api.shareNoteWithFriend(userId, noteId, ShareRequestDto(friendId))
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Empty response")
            } else {
                when (response.code()) {
                    404 -> Resource.Error("Note or friend not found")
                    400 -> Resource.Error("Already shared or not friends")
                    else -> Resource.Error("HTTP ${response.code()} ${response.message()}")
                }
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun getNotesSharedWithMe(userId: Int): Resource<List<SharedNoteWithDetailsDto>> {
        return try {
            val response = api.getNotesSharedWithMe(userId)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Empty response")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun getNotesSharedByMe(userId: Int): Resource<List<SharedNoteByMeDto>> {
        return try {
            val response = api.getNotesSharedByMe(userId)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Empty response")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun updateSharedNoteStatus(userId: Int, sharedNoteId: Int): Resource<UpdateSharedStatusResponseDto> {
        return try {
            val response = api.updateSharedNoteStatus(userId, sharedNoteId)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Empty response")
            } else {
                when (response.code()) {
                    404 -> Resource.Error("Shared note not found")
                    403 -> Resource.Error("Not authorized to modify")
                    else -> Resource.Error("HTTP ${response.code()} ${response.message()}")
                }
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun getSharedNoteDetails(userId: Int, noteId: Int): Resource<SharedNoteWithDetailsDto?> {
        return try {
            val response = api.getNotesSharedWithMe(userId)
            if (response.isSuccessful) {
                val foundNote = response.body()?.find { it.noteId == noteId }
                Resource.Success(foundNote)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun canAccessNote(userId: Int, noteId: Int): Resource<Boolean> {
        return try {
            val response = api.getUserNoteById(userId, noteId)
            Resource.Success(response.isSuccessful)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }
}
