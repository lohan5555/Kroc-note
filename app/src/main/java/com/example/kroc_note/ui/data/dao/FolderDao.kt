package com.example.kroc_note.ui.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.kroc_note.ui.data.bddClass.Folder

@Dao
interface FolderDao {
    @Insert
    suspend fun insert(folder: Folder)

    @Delete
    suspend fun delete(folder: Folder)

    @Query("SELECT * FROM Folder")
    suspend fun getAllDossiers(): List<Folder>
}
