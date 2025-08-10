package com.example.kroc_note.ui.data

import androidx.room.*
import com.example.kroc_note.ui.data.bddClass.Note
import kotlinx.coroutines.flow.Flow

//dao: data acces object
//toute les fonctions qui vont venir modifier notre bdd
@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(note: Note): Long

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM note WHERE idNote IN (:ids)")
    suspend fun deleteAllById(ids: List<Int>)

    @Query("SELECT * FROM note ORDER BY titre ASC")
    fun getAllNotesByTitre(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY body ASC")
    fun getAllNotesByBody(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY dateCreation DESC")
    fun getAllNotesByDateCreation(): Flow<List<Note>>
}


