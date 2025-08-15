package com.example.kroc_note.ui.data

import com.example.kroc_note.ui.data.bddClass.File
import com.example.kroc_note.ui.data.type.CouleurNote

data class FileState(
    val files: List<File> = emptyList(),

    val fileId:Int = 0,
    val titre:String = "",
    val couleur: CouleurNote = CouleurNote.Violet,
    val dateCreation: Long = System.currentTimeMillis(),
    val dateModification: Long = System.currentTimeMillis(),
    val path: String = "home/",
)
