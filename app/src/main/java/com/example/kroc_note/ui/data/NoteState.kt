package com.example.kroc_note.ui.data

data class NoteState(
    val notes: List<Note> = emptyList(),
    val titre:String = "",
    val body:String = "",
    val isAddingNote: Boolean = false,
    val sortType: SortType = SortType.TITRE
)
