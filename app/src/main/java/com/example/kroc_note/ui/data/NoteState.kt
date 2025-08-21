package com.example.kroc_note.ui.data

import com.example.kroc_note.ui.data.bddClass.Note
import com.example.kroc_note.ui.data.type.SortType
import com.example.kroc_note.ui.data.type.CouleurNote

data class NoteState(
    val notes: List<Note> = emptyList(),


    val noteId:Int = 0,
    val titre:String = "",
    val body:String = "",
    val couleur: CouleurNote = CouleurNote.Violet,
    val dateModification: Long = System.currentTimeMillis(),
    val dateCreation: Long = System.currentTimeMillis(),
    val path: String = "home",
    val oldPath: String = "home",

    val sortType: SortType = SortType.RECENTE
)
