package com.example.kroc_note.ui.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.kroc_note.ui.data.bddClass.Folder
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Upsert
    suspend fun upsert(folder: Folder)

    @Delete
    suspend fun delete(folder: Folder)

    @Query("SELECT * FROM Folder")
    fun getAllDossiers(): Flow<List<Folder>>
}
