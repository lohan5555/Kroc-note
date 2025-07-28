package com.example.kroc_note.ui.data

import androidx.room.Database
import androidx.room.RoomDatabase

//on creer la bdd
@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}