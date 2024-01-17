package com.maurya.memoease

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maurya.memoease.databinding.NotesItemBinding
import com.maurya.memoease.models.NoteResponse
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.reflect.KFunction1

class AdapterNotes(
    private val onNoteClicked: (NoteResponse) -> Unit
) : ListAdapter<NoteResponse, AdapterNotes.MemoEaseFileHolder>(ComparatorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoEaseFileHolder {
        val binding = NotesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemoEaseFileHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoEaseFileHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let {
            holder.bind(it)
        }
    }

    inner class MemoEaseFileHolder(private val binding: NotesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NoteResponse) {
            binding.notesNameNotesItem.text = note.title
            binding.notesDetailsNotesItem.text = note.description
            val formattedDate = formatDate(note.createdAt)
            binding.notesDateNotesItem.text = formattedDate
            binding.root.setOnClickListener {
                onNoteClicked(note)
            }
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<NoteResponse>() {
        override fun areItemsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem == newItem
        }
    }

    private fun formatDate(rawDate: String): String {
        // Parse the raw date using SimpleDateFormat
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val formatter = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())


        try {
            val date = parser.parse(rawDate)
            return formatter.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            return rawDate // Return the raw date in case of parsing error
        }
    }
}

