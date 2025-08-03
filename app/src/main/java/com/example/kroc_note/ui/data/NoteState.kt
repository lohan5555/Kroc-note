package com.example.kroc_note.ui.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.kroc_note.ui.data.bddClass.Note
import com.example.kroc_note.ui.theme.CouleurNote
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class NoteState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val notes: List<Note> = emptyList(),

    val titre:String = "",
    val body:String = "",
    val couleur:CouleurNote = CouleurNote.Rose,
    val dateDerniereModification: LocalDateTime = LocalDateTime.now(),
    val dateCreation: LocalDateTime = LocalDateTime.now(),

    val isAddingNote: Boolean = false,
    val sortType: SortType = SortType.TITRE
)
