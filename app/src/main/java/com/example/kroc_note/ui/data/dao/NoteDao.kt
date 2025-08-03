package com.example.kroc_note.ui.data.dao

import androidx.room.*
import com.example.kroc_note.ui.data.bddClass.Note
import kotlinx.coroutines.flow.Flow

//toute les fonctions qui vont venir modifier notre bdd
@Dao
interface NoteDao {
    @Upsert  //Insert ou Update en fonction de si l'id existe déjà ou pas
    suspend fun upsert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM note ORDER BY titre ASC")
    fun getAllNotesByTitre(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY body ASC")
    fun getAllNotesByBody(): Flow<List<Note>>
}


