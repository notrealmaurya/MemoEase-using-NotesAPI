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

class AdapterNotes(
    private val context: Context,
    private var listener: OnItemClickListener,
    private val homeList: MutableList<NoteResponse> = mutableListOf(),
    private val deletedList: MutableList<NoteResponse> = mutableListOf(),
    private val isDeleted: Boolean
) : ListAdapter<NoteResponse, AdapterNotes.MemoEaseFileHolder>(ComparatorDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoEaseFileHolder {
        val binding = NotesItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MemoEaseFileHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoEaseFileHolder, position: Int) {

        val currentList = if (isDeleted) deletedList else homeList
        val currentItem = currentList[position]

        with(holder) {

            noteName.text = currentItem.title
            noteDescription.text = currentItem.description
            noteDate.text = currentItem.createdAt

        }


    }


    override fun getItemCount(): Int {
        return if (isDeleted) deletedList.size else homeList.size

    }


    inner class MemoEaseFileHolder(binding: NotesItemBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        val noteName = binding.notesNameNotesItem
        val noteDescription = binding.notesDetailsNotesItem
        val noteDate = binding.notesDateNotesItem
        private val root = binding.root

        init {
            root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClickListener(position, isDeleted)
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
}