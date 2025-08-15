package com.example.kroc_note.ui.data.bddClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kroc_note.ui.data.type.CouleurNote

@Entity
data class Folder(
    @PrimaryKey(autoGenerate = true) val idFolder: Int = 0,
    val name: String,
    val path: String,
    val color: CouleurNote,
    val dateCreation: Long,
    val dateModification: Long,
)



