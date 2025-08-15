package com.example.kroc_note.ui.data

import com.example.kroc_note.ui.data.bddClass.Folder
import com.example.kroc_note.ui.data.bddClass.Note
import com.example.kroc_note.ui.data.type.CouleurNote
import com.example.kroc_note.ui.data.type.SortType

interface FolderEvent {
    //setteur
    data class SetId(val idFolder: Int): FolderEvent
    data class SetName(val name: String): FolderEvent
    data class SetPath(val path: String): FolderEvent
    data class SetColor(val couleur: CouleurNote): FolderEvent
    data class SetDateCreation(val dateCreation: Long): FolderEvent
    data class SetDateModification(val dateModification: Long): FolderEvent

    object SaveFolder: FolderEvent
    data class DeleteFolder(val folder: Folder): FolderEvent
}