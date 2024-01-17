package com.maurya.memoease.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.maurya.memoease.utils.getBitmapFromView
import com.maurya.memoease.utils.saveBitmapAsImage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
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
                val dateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val createdAtDate = dateFormat.parse(it.createdAt)
                val formattedDate = SimpleDateFormat(
                    "EEEE, dd MMMM yyyy",
                    Locale.getDefault()
                ).format(createdAtDate)
                fragmentNotesBinding.notesDetailTxtNotesItem.text =
                    "$formattedDate | ${it.description.length} characters"
                fragmentNotesBinding.notesDescEditTextNotesItem.setText(it.description)
            }
        } else {

        }



        listeners()
    }

    private fun listeners() {
        fragmentNotesBinding.deleteNoteFragment.setOnClickListener {
            note?.let {
                noteViewModel.deleteNote(it._id)
                findNavController().navigateUp()
            }
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


        fragmentNotesBinding.backNoteFragment.setOnClickListener {
            findNavController().navigateUp()
        }

        fragmentNotesBinding.saveImageNoteFragment.setOnClickListener {
            val scrollView = fragmentNotesBinding.scrollView
            val heightMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            scrollView.measure(0, heightMeasureSpec)
            val totalHeight = scrollView.measuredHeight
            val bitmap = Bitmap.createBitmap(scrollView.width, totalHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            scrollView.draw(canvas)
            saveBitmapAsImage(bitmap, requireContext())

            Toast.makeText(requireContext(), "Note saved as image!", Toast.LENGTH_SHORT).show()
        }

        fragmentNotesBinding.shareNoteFragment.setOnClickListener {
            val title = fragmentNotesBinding.notesTitleEditTextNotesItem.text.toString()
            val desc = fragmentNotesBinding.notesDescEditTextNotesItem.text.toString()
            val noteText = "Title : $title\n\nDescription:\n\n$desc"
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, noteText)
            }

            startActivity(Intent.createChooser(shareIntent, "Share Note"))
        }


    }


}