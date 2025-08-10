package com.example.kroc_note.ui.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kroc_note.ui.data.bddClass.Note
import com.example.kroc_note.ui.data.type.SortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteViewModel(
    private val dao: NoteDao
): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.RECENTE)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _notes = _sortType
        .flatMapLatest { sortType ->
            when(sortType){
                SortType.A_Z -> dao.getAllNotesAlphabetiqueASC()
                SortType.Z_A -> dao.getAllNotesAlphabetiqueDESC()
                SortType.RECENTE -> dao.getAllNotesByDateCreationDESC()
                SortType.ANCIENNE -> dao.getAllNotesByDateCreationASC()
                SortType.MODIF_RECENTE -> dao.getAllNotesByDateModif()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    @RequiresApi(Build.VERSION_CODES.O)
    private val _state = MutableStateFlow(NoteState())
    @RequiresApi(Build.VERSION_CODES.O)
    val state = combine(_state, _sortType, _notes){ state, sortType, notes ->
        state.copy(
            notes = notes,
            sortType = sortType,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: NoteEvent){
        when(event){
            NoteEvent.SaveNote -> {
                val idNote = state.value.noteId
                val titre = state.value.titre
                val body = state.value.body
                val couleur = state.value.couleur
                val dateDerniereModification = state.value.dateDerniereModification
                val dateCreation = state.value.dateCreation

                if(titre.isBlank()){  //une note doit avoir au moins un titre
                    return
                }

                val note = Note(
                    idNote = idNote,
                    titre = titre,
                    body = body,
                    couleur = couleur,
                    dateDerniereModification = dateDerniereModification,
                    dateCreation = dateCreation
                )
                viewModelScope.launch {
                    dao.upsert(note)
                }
                if(idNote == 0){
                    _state.update { it.copy(
                        titre = "",
                        body = ""
                    ) }
                }
            }
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.delete(event.note)
                }
            }
            is NoteEvent.SortNote -> {
                _sortType.value = event.sortType
            }
            is NoteEvent.SetId -> {
                _state.update { it.copy(
                    noteId = event.idNote
                ) }
            }
            is NoteEvent.SetTitre -> {
                _state.update { it.copy(
                    titre = event.titre
                ) }
            }
            is NoteEvent.SetBody -> {
                _state.update { it.copy(
                    body = event.body
                ) }
            }
            is NoteEvent.SetDateCreation -> {
                _state.update { it.copy(
                    dateCreation = event.dateCreation
                ) }
            }
            is NoteEvent.SetDateModification -> {
                _state.update { it.copy(
                    dateCreation = event.dateModification
                ) }
            }
            is NoteEvent.DeleteManyNoteById -> {
                viewModelScope.launch {
                    dao.deleteAllById(
                        ids = event.ids
                    )
                }
            }
        }
    }
}