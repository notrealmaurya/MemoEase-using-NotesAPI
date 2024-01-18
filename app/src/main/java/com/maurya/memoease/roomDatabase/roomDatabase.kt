package com.maurya.memoease.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maurya.memoease.models.NoteResponse
//
//@Database(entities = [NoteResponse::class], version = 1, exportSchema = false)
//abstract class roomDatabase : RoomDatabase() {
//
//    abstract fun noteDao(): noteDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: roomDatabase? = null
//
//        fun getDatabase(context: Context): roomDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    roomDatabase::class.java,
//                    "app_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
