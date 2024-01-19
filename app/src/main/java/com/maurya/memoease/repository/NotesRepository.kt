package com.maurya.memoease.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maurya.memoease.api.NotesAPI
import com.maurya.memoease.models.NoteRequest
import com.maurya.memoease.models.NoteResponse
import com.maurya.memoease.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private var notesAPI: NotesAPI
//    ,    private val noteDao: noteDao
) {

    private val _notesStateFlow =
        MutableStateFlow<NetworkResult<List<NoteResponse>>>(NetworkResult.Loading())

    val notesStateFlow: StateFlow<NetworkResult<List<NoteResponse>>> get() = _notesStateFlow


    private val _statusStateFlow = MutableStateFlow<NetworkResult<String>>(NetworkResult.Loading())
    val statusStateFlow: StateFlow<NetworkResult<String>> get() = _statusStateFlow
    suspend fun getNotes() {
        _notesStateFlow.emit(NetworkResult.Loading())
        try {
            val response = notesAPI.getNotes()
            if (response.isSuccessful) {
                val notes = response.body() ?: emptyList()
//                noteDao.insertOrUpdate(notes)
                _notesStateFlow.emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorObject = JSONObject(response.errorBody()?.charStream()?.readText() ?: "")
                _notesStateFlow.emit(NetworkResult.Error(errorObject.getString("message")))
            }
        } catch (e: Exception) {
            _notesStateFlow.emit(NetworkResult.Error("No Notes is available"))
        }
    }

    suspend fun saveNotesToLocalDatabase(notes: List<NoteResponse>): List<NoteResponse> {
//        noteDao.insertOrUpdate(notes)

        return notes
    }

//    suspend fun getLocalNotes(): List<NoteResponse> {
//        return noteDao.getAllNotes()
//    }

    suspend fun createNotes(noteRequest: NoteRequest) {
        _statusStateFlow.emit(NetworkResult.Loading())
        try {
            val response = notesAPI.createNote(noteRequest)
            handleResponse(response, "Notes Created")
        } catch (e: Exception) {
            _notesStateFlow.emit(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun updateNotes(noteId: String, noteRequest: NoteRequest) {
        _statusStateFlow.emit(NetworkResult.Loading())
        try {
            val response = notesAPI.updateNote(noteId, noteRequest)
            handleResponse(response, "Note Updated")
        } catch (e: Exception) {
            _notesStateFlow.emit(NetworkResult.Error("Something went wrong"))
        }

    }

    suspend fun deleteNotes(noteId: String) {
        _statusStateFlow.emit(NetworkResult.Loading())
        try {
            val response = notesAPI.deleteNote(noteId)
            handleResponse(response, "Note Deleted")
        } catch (e: Exception) {
            _notesStateFlow.emit(NetworkResult.Error("Something went wrong"))
        }

    }


    private suspend fun handleResponse(response: Response<NoteResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusStateFlow.emit(NetworkResult.Success(message))
        } else {
            _notesStateFlow.emit(NetworkResult.Error("Something went wrong"))
        }
    }


}