package com.maurya.memoease.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maurya.memoease.models.NoteRequest
import com.maurya.memoease.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val notesRepository: NotesRepository) :
    ViewModel() {

    val notesLiveData get() = notesRepository.notesLiveData
    val statusLiveData get() = notesRepository.statusLiveData


    fun getNotes() {

        viewModelScope.launch {
            try {
                notesRepository.getNotes()
            } catch (e: Exception) {
//                val localNotes = notesRepository.getLocalNotes()
            }
        }
    }

    fun createNote(noteRequest: NoteRequest) {
        viewModelScope.launch {
            notesRepository.createNotes(noteRequest)
        }
    }

    fun updateNote(noteId: String, noteRequest: NoteRequest) {
        viewModelScope.launch {
            notesRepository.updateNotes(noteId, noteRequest)
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            notesRepository.deleteNotes(noteId)
        }
    }


}
