package com.example.kroc_note.ui.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kroc_note.ui.data.bddClass.Converters
import com.example.kroc_note.ui.data.bddClass.Note
import com.example.kroc_note.ui.data.bddClass.File
import com.example.kroc_note.ui.data.dao.NoteDao

//définit la configuration de la base de données
@Database(
    entities = [Note::class, File::class],
    version = 7
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun fileDao(): FileDao
}