package com.example.kroc_note.ui.data

sealed interface NoteEvent {
    object SaveNote: NoteEvent
    data class SetTitre(val titre: String): NoteEvent
    data class SetBody(val body: String): NoteEvent
    object ShowDialog: NoteEvent
    object HideDialog: NoteEvent
    data class SortNote(val sortType: SortType): NoteEvent
    data class DeleteNote(val note: Note): NoteEvent
}