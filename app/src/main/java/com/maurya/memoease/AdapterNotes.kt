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
import com.maurya.memoease.utils.formatDate
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.reflect.KFunction1
class AdapterNotes(
    private val notesList: List<NoteResponse>,
    private val onNoteClicked: (NoteResponse) -> Unit
) : RecyclerView.Adapter<AdapterNotes.MemoEaseFileHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoEaseFileHolder {
        val binding = NotesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemoEaseFileHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoEaseFileHolder, position: Int) {
        val currentItem = notesList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return notesList.size
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
}
