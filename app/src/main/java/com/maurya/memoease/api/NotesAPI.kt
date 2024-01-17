package com.maurya.memoease.api

import com.maurya.memoease.models.NoteRequest
import com.maurya.memoease.models.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotesAPI {

    @GET("/notes")
    suspend fun getNotes(): Response<List<NoteResponse>>

    @POST("/notes")
    suspend fun createNote(@Body noteRequest: NoteRequest): Response<NoteResponse>

    @PUT("/notes/{noteId}")
    suspend fun updateNote(
        @Path("noteId") noteId: String,
        @Body noteRequest: NoteRequest
    ): Response<NoteResponse>


    @DELETE("/notes/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId: String): Response<NoteResponse>


}