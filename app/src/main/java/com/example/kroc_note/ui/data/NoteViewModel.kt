package com.example.kroc_note.ui.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.surfaceColorAtElevation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kroc_note.ui.data.bddClass.Note
import com.example.kroc_note.ui.data.dao.NoteDao
import com.example.kroc_note.ui.data.type.SortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private var saveJob: Job? = null
    private var currentPath: String = "home"
    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: NoteEvent){
        when(event){
            is NoteEvent.CreateNote -> {
                val idNote = state.value.noteId
                val titre = state.value.titre.ifBlank { "nouvelle note" }
                val body = state.value.body
                val couleur = state.value.couleur
                val dateModification = state.value.dateModification
                val dateCreation = state.value.dateCreation
                val path = event.path

                val note = Note(
                    idNote = idNote,
                    titre = titre,
                    body = body,
                    couleur = couleur,
                    dateModification = dateModification,
                    dateCreation = dateCreation,
                    path = path
                )
                viewModelScope.launch {
                    dao.upsert(note)
                }
            }
            is NoteEvent.UpdateNote -> {
                val note = Note(
                    idNote = event.noteId,
                    titre = event.titre,
                    body = event.body,
                    couleur = event.couleur,
                    dateModification = event.dateModification,
                    dateCreation = event.dateCreation,
                    path = event.path
                )

                viewModelScope.launch {
                    dao.upsert(note)
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
            is NoteEvent.SetColor -> {
                _state.update { it.copy(
                    couleur = event.couleur
                ) }
            }
            is NoteEvent.SetDateCreation -> {
                _state.update { it.copy(
                    dateCreation = event.dateCreation
                ) }
            }
            is NoteEvent.SetDateModification -> {
                _state.update { it.copy(
                    dateModification = event.dateModification
                ) }
            }
            is NoteEvent.SetPath -> {
                currentPath = event.path
                _state.update { it.copy(
                    path = event.path
                ) }
            }
            is NoteEvent.DeleteManyNoteById -> {
                viewModelScope.launch {
                    dao.deleteAllById(
                        ids = event.ids
                    )
                }
            }
            is NoteEvent.DebouncedSave -> {
                saveJob?.cancel()
                saveJob = viewModelScope.launch {
                    delay(500)
                    autoSave()
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun autoSave() {
        val s = state.value
        if (s.titre.isBlank()) return

        val note = Note(
            idNote = s.noteId,
            titre = s.titre,
            body = s.body,
            couleur = s.couleur,
            dateModification = System.currentTimeMillis(),
            dateCreation = if (s.dateCreation == 0L) System.currentTimeMillis() else s.dateCreation,
            path = s.path.takeIf { it.isNotBlank() } ?: currentPath
        )
        viewModelScope.launch {
            dao.upsert(note)
        }
    }

}