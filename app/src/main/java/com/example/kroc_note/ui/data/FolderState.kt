package com.example.kroc_note.ui.data

import com.example.kroc_note.ui.data.bddClass.Folder
import com.example.kroc_note.ui.data.type.CouleurNote

data class FolderState(
    val folders: List<Folder> = emptyList(),

    val folderId:Int = 0,
    val name:String = "",
    val couleur: CouleurNote = CouleurNote.Violet,
    val dateCreation: Long = System.currentTimeMillis(),
    val dateModification: Long = System.currentTimeMillis(),
    val path: String = "home",
)
