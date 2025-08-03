package com.example.kroc_note.ui.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kroc_note.ui.data.bddClass.Note
import androidx.room.TypeConverters
import com.example.kroc_note.ui.data.bddClass.Converters
import com.example.kroc_note.ui.data.dao.NoteDao

//on creer la bdd
@Database(entities = [Note::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}