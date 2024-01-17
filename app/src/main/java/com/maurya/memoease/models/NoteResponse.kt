package com.maurya.memoease.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "noteRecords")
data class NoteResponse(

    val __v: Int,
    val _id: String,
    val createdAt: String,
    val description: String,
    val title: String,
    val updatedAt: String,
    val userId: String

) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @Ignore
    var isChecked = false
}
