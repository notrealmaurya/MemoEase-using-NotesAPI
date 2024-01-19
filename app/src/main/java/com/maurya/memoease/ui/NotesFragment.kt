package com.maurya.memoease.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.maurya.memoease.databinding.FragmentNotesBinding
import com.maurya.memoease.models.NoteRequest
import com.maurya.memoease.models.NoteResponse
import com.maurya.memoease.utils.NetworkResult
import com.maurya.memoease.utils.saveBitmapAsImage
import com.maurya.memoease.utils.showConfirmationDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


@AndroidEntryPoint
class NotesFragment : Fragment() {

    private var note: NoteResponse? = null
    private val noteViewModel by viewModels<NoteViewModel>()
    private lateinit var fragmentNotesBinding: FragmentNotesBinding

    private val textChangesList = mutableListOf<String>()
    private var currentStateIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentNotesBinding = FragmentNotesBinding.inflate(inflater, container, false)

        return fragmentNotesBinding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                noteViewModel.statusStateFlow.collect {
                    when (it) {
                        is NetworkResult.Success -> {
                            findNavController().popBackStack()
                        }

                        is NetworkResult.Error -> {}

                        is NetworkResult.Loading -> {

                        }
                    }
                }
            }
        }


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
                val character = it.description.length
                if (character <= 1) {
                    fragmentNotesBinding.notesDetailTxtNotesItem.text =
                        "$formattedDate | ${character} character"
                } else {
                    fragmentNotesBinding.notesDetailTxtNotesItem.text =
                        "$formattedDate | ${character} characters"
                }
                fragmentNotesBinding.notesDescEditTextNotesItem.setText(it.description)
            }
        } else {
            fragmentNotesBinding.popSheet.visibility = View.GONE

        }



        listeners()
    }

    private fun listeners() {


        fragmentNotesBinding.reminderNoteFragment.setOnClickListener {
            Snackbar.make(
                fragmentNotesBinding.root,
                "Feature coming soon \uD83D\uDC7B", 1000
            ).show()
        }

        fragmentNotesBinding.themeNoteFragment.setOnClickListener {
            Snackbar.make(
                fragmentNotesBinding.root,
                "Feature coming soon \uD83D\uDC7B", 1000
            ).show()
        }

        fragmentNotesBinding.lockNoteFragment.setOnClickListener {
            Snackbar.make(
                fragmentNotesBinding.root,
                "Feature coming soon \uD83D\uDC7B", 1000
            ).show()
        }


        fragmentNotesBinding.deleteNoteFragment.setOnClickListener {
            showConfirmationDialog(
                requireContext(),
                "Delete",
                "Are you sure you want to delete this note?"
            ) {
                note?.let {
                    noteViewModel.deleteNote(it._id)
                    findNavController().navigateUp()
                    HomeFragment.adapterNotes.notifyDataSetChanged()
                    Toast.makeText(requireContext(), "Deleted!", Toast.LENGTH_SHORT).show()
                }
            }
        }


        fragmentNotesBinding.saveImageNoteFragment.setOnClickListener {
            showConfirmationDialog(
                requireContext(),
                "Save as Image",
                "Are you sure you want to save this note as an image?"
            ) {
                val scrollView = fragmentNotesBinding.scrollView
                val heightMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                scrollView.measure(0, heightMeasureSpec)
                val totalHeight = scrollView.measuredHeight
                val bitmap =
                    Bitmap.createBitmap(scrollView.width, totalHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                scrollView.draw(canvas)
                saveBitmapAsImage(bitmap, requireContext())
                Toast.makeText(requireContext(), "Note saved as image!", Toast.LENGTH_SHORT).show()
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