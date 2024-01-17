package com.maurya.memoease.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maurya.memoease.api.NotesAPI
import com.maurya.memoease.models.NoteRequest
import com.maurya.memoease.models.NoteResponse
import com.maurya.memoease.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NotesRepository @Inject constructor(private var notesAPI: NotesAPI) {

    private val _notesLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()

    val notesLiveData: LiveData<NetworkResult<List<NoteResponse>>> get() = _notesLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>> get() = _statusLiveData
    suspend fun getNotes() {
        _notesLiveData.postValue(NetworkResult.Loading())
        try {
            val response = notesAPI.getNotes()
            if (response.isSuccessful) {
                _notesLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else {
                val errorObject = JSONObject(response.errorBody()?.charStream()?.readText() ?: "")
                _notesLiveData.postValue(NetworkResult.Error(errorObject.getString("message")))
            }
        } catch (e: Exception) {
            _notesLiveData.postValue(NetworkResult.Error("No Notes is available"))
        }
    }



    suspend fun createNotes(noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        try {
            val response = notesAPI.createNote(noteRequest)
            handleResponse(response, "Notes Created")
        } catch (e: Exception) {
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun updateNotes(noteId: String, noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        try {
            val response = notesAPI.updateNote(noteId, noteRequest)
            handleResponse(response, "Note Updated")
        } catch (e: Exception) {
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }

    }

    suspend fun deleteNotes(noteId: String) {
        _statusLiveData.postValue(NetworkResult.Loading())
        try {
            val response = notesAPI.deleteNote(noteId)
            handleResponse(response, "Note Deleted")
        } catch (e: Exception) {
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }

    }


    private fun handleResponse(response: Response<NoteResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        } else {
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }


}