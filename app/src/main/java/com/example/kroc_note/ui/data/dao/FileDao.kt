package com.example.kroc_note.ui.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kroc_note.ui.data.bddClass.File

@Dao
interface FileDao {
    @Insert
    suspend fun insert(file: File)

    @Query("SELECT * FROM File")
    suspend fun getAll(): List<File>
}
