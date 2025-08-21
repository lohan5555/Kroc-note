package com.example.kroc_note.ui.data.dao

import androidx.room.*
import com.example.kroc_note.ui.data.bddClass.Note
import kotlinx.coroutines.flow.Flow

//dao: data acces object
//toute les fonctions qui vont venir modifier notre table Note
@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(note: Note): Long

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM note WHERE idNote IN (:ids)")
    suspend fun deleteAllById(ids: List<Int>)

    @Query("UPDATE note SET path = :path WHERE idNote IN (:ids)")
    suspend fun setAllPathById(ids: List<Int>, path: String)

    @Query("UPDATE note SET oldPath = path WHERE idNote IN (:ids)")
    suspend fun setAllOldPathById(ids: List<Int>)

    @Query("UPDATE note SET path = oldPath WHERE idNote IN (:ids)")
    suspend fun setAllPathRestaure(ids: List<Int>)

    @Query("UPDATE note SET path = 'corbeille' WHERE idNote IN (:ids)")
    suspend fun setAllPathCorbeille(ids: List<Int>)

    @Query("UPDATE note SET path = :path WHERE idNote = :id")
    suspend fun setOnePathById(id: Int, path: String)


    @Query("SELECT * FROM note ORDER BY titre ASC")
    fun getAllNotesAlphabetiqueASC(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY titre DESC")
    fun getAllNotesAlphabetiqueDESC(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY dateCreation DESC")
    fun getAllNotesByDateCreationDESC(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY dateCreation ASC")
    fun getAllNotesByDateCreationASC(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY dateModification DESC")
    fun getAllNotesByDateModif(): Flow<List<Note>>
}


