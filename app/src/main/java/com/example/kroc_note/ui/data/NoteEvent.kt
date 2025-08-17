package com.example.kroc_note.ui.data

import com.example.kroc_note.ui.data.bddClass.Note
import com.example.kroc_note.ui.data.type.CouleurNote
import com.example.kroc_note.ui.data.type.SortType

sealed interface NoteEvent {
    //setteur
    data class SetId(val idNote: Int): NoteEvent
    data class SetTitre(val titre: String): NoteEvent
    data class SetBody(val body: String): NoteEvent
    data class SetColor(val couleur: CouleurNote): NoteEvent
    data class SetDateCreation(val dateCreation: Long): NoteEvent
    data class SetDateModification(val dateModification: Long): NoteEvent
    data class SetPath(val path: String): NoteEvent

    data class CreateNote(val path: String): NoteEvent

    object DebouncedSave : NoteEvent
    data class UpdateNote(
        val noteId:Int,
        val titre:String,
        val body:String,
        val couleur: CouleurNote,
        val dateModification: Long,
        val dateCreation: Long,
        val path: String,
    ): NoteEvent

    data class SortNote(val sortType: SortType): NoteEvent
    data class DeleteNote(val note: Note): NoteEvent
    data class DeleteManyNoteById(val ids: List<Int>): NoteEvent
}