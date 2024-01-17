package com.maurya.memoease.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.maurya.memoease.NoteViewModel
import com.maurya.memoease.R
import com.maurya.memoease.databinding.FragmentNotesBinding
import com.maurya.memoease.databinding.FragmentSignInBinding
import com.maurya.memoease.databinding.FragmentSplashBinding
import com.maurya.memoease.models.NoteRequest
import com.maurya.memoease.models.NoteResponse
import com.maurya.memoease.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale


@AndroidEntryPoint
class NotesFragment : Fragment() {

    private lateinit var fragmentNotesBinding: FragmentNotesBinding
    private var note: NoteResponse? = null
    private val noteViewModel by viewModels<NoteViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentNotesBinding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = fragmentNotesBinding.root

        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }

                is NetworkResult.Error -> {}

                is NetworkResult.Loading -> {

                }
            }
        })

        val jsonNotes = arguments?.getString("note")
        if (jsonNotes != null) {
            note = Gson().fromJson(jsonNotes, NoteResponse::class.java)
            note?.let {
                fragmentNotesBinding.notesTitleEditTextNotesItem.setText(it.title)
                val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(it.createdAt)
                fragmentNotesBinding.notesDetailTxtNotesItem.setText(formattedDate + " | " + it.description.length)
                fragmentNotesBinding.notesDescEditTextNotesItem.setText(it.description)
            }
        } else {

        }


        listeners()
    }

    private fun listeners() {
        fragmentNotesBinding.deleteNoteFragment.setOnClickListener {
            note?.let { noteViewModel.deleteNote(it._id) }
        }

        fragmentNotesBinding.submitNoteFragment.setOnClickListener {
            val title = fragmentNotesBinding.notesTitleEditTextNotesItem.text.toString()
            val desc = fragmentNotesBinding.notesDescEditTextNotesItem.text.toString()
            val noteRequest = NoteRequest(desc, title)
            if (note == null) {
                noteViewModel.createNote(noteRequest)
            } else {
                noteViewModel.updateNote(note!!._id, noteRequest)
            }
        }
    }


}