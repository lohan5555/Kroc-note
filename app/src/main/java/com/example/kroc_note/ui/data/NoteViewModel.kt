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
import java.time.Instant
import java.time.ZoneId

class NoteViewModel(
    private val dao: NoteDao
): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.TITRE)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _notes = _sortType
        .flatMapLatest { sortType ->
            when(sortType){
                SortType.TITRE -> dao.getAllNotesByTitre()
                SortType.BODY -> dao.getAllNotesByBody()
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
                val titre = state.value.titre
                val body = state.value.body
                val couleur = state.value.couleur
                val dateDerniereModification = state.value.dateDerniereModification
                val dateCreation = state.value.dateCreation

                if(titre.isBlank() || body.isBlank()){
                    return
                }

                val note = Note(
                    titre = titre,
                    body = body,
                    couleur = couleur,
                    dateDerniereModification = dateDerniereModification,
                    dateCreation = dateCreation
                )
                viewModelScope.launch {
                    dao.upsert(note)
                }
                _state.update { it.copy(
                    isAddingNote = false,
                    titre = "",
                    body = ""
                ) }
            }
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.delete(event.note)
                }
            }
            NoteEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingNote = true
                ) }
            }
            NoteEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingNote = false
                ) }
            }
            is NoteEvent.SortNote -> {
                _sortType.value = event.sortType
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