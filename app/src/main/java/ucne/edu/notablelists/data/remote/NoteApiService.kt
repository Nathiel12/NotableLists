package ucne.edu.notablelists.data.remote

import retrofit2.Response
import retrofit2.http.*
import ucne.edu.notablelists.data.remote.dto.NoteRequestDto
import ucne.edu.notablelists.data.remote.dto.NoteResponseDto

interface NoteApiService {

    @GET("api/Notes")
    suspend fun getNotes(): Response<List<NoteResponseDto>>

    @GET("api/Notes/{id}")
    suspend fun getNoteById(@Path("id") id: Int): Response<NoteResponseDto>

    @POST("api/Notes")
    suspend fun createNote(@Body request: NoteRequestDto): Response<NoteResponseDto>

    @PUT("api/Notes/{id}")
    suspend fun updateNote(@Path("id") id: Int, @Body request: NoteRequestDto): Response<Unit>

    @DELETE("api/Notes/{id}")
    suspend fun deleteNote(@Path("id") id: Int): Response<Unit>

    @GET("api/Notes/Users/{userId}/Notes")
    suspend fun getUserNotes(@Path("userId") userId: Int): Response<List<NoteResponseDto>>

    @POST("api/Notes/Users/{userId}/Notes")
    suspend fun createUserNote(
        @Path("userId") userId: Int,
        @Body request: NoteRequestDto
    ): Response<NoteResponseDto>

    @PUT("api/Notes/Users/{userId}/Notes/{id}")
    suspend fun updateUserNote(
        @Path("userId") userId: Int,
        @Path("id") id: Int,
        @Body request: NoteRequestDto
    ): Response<Unit>

    @DELETE("api/Notes/Users/{userId}/Notes/{id}")
    suspend fun deleteUserNote(
        @Path("userId") userId: Int,
        @Path("id") id: Int
    ): Response<Unit>
}