package com.example.kroc_note.ui.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.mutableStateListOf

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    // Cette liste contiendra vos notes
    private val _notes = mutableStateListOf<Note>()
    val notes: List<Note> = _notes
    fun addNote(note: Note) {
        // Pour l'exemple, nous allons juste ajouter une note avec un ID simple
        // En production, vous voudriez une stratégie d'ID plus robuste
        val newId = (_notes.maxByOrNull { it.id }?.id ?: 0) + 1
        _notes.add(note.copy(id = newId))
    }
}


// Fabrique pour créer le NoteViewModel avec l'instance Application
class NoteViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 