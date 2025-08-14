package com.example.kroc_note.ui.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.kroc_note.ui.data.bddClass.File
import com.example.kroc_note.ui.data.bddClass.Note

@Dao
interface FileDao {
    @Insert
    suspend fun insert(file: File)

    @Delete
    suspend fun delete(file: File)

    @Query("SELECT * FROM File")
    suspend fun getAllDossiers(): List<File>
}
