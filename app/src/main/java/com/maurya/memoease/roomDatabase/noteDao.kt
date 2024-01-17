package com.maurya.memoease.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maurya.memoease.models.NoteResponse


@Dao
interface noteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(note: List<NoteResponse>)

    @Query("SELECT * FROM noteRecords")
    suspend fun getAllNotes(): List<NoteResponse>


}