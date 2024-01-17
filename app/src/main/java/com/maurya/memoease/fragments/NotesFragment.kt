package com.maurya.memoease.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.maurya.memoease.R
import com.maurya.memoease.databinding.FragmentNotesBinding
import com.maurya.memoease.databinding.FragmentSignInBinding
import com.maurya.memoease.databinding.FragmentSplashBinding
import com.maurya.memoease.models.NoteResponse


class NotesFragment : Fragment() {

    private lateinit var fragmentNotesBinding: FragmentNotesBinding
    private var note: NoteResponse? = null

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

        val jsonNotes = arguments?.getString("note")
        if (jsonNotes != null) {
            note = Gson().fromJson(jsonNotes, NoteResponse::class.java)
            note?.let {
                fragmentNotesBinding.notesTitleEditTextNotesItem.setText(it.title)
                fragmentNotesBinding.notesDetailTxtNotesItem.setText(it.createdAt + it.description.count() )
                fragmentNotesBinding.notesDescEditTextNotesItem.setText(it.description)
            }
        } else {

        }
    }


}