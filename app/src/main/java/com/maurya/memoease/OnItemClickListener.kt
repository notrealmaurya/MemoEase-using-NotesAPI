package com.maurya.memoease

import com.maurya.memoease.models.NoteResponse


interface OnItemClickListener {
    fun onItemCheckedChange(position: Int, isChecked: Boolean, isCompleteList: Boolean)
    fun onItemClickListener(noteResponse: NoteResponse, position: Int, isDeleted: Boolean)
}