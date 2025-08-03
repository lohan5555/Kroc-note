package com.example.kroc_note.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val preferences: ThemePreferences
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    init {
        preferences.darkThemeFlow
            .onEach { _isDarkTheme.value = it }
            .launchIn(viewModelScope)
    }

    fun toggleTheme() {
        viewModelScope.launch {
            preferences.saveDarkTheme(!_isDarkTheme.value)
        }
    }
}

class ThemeViewModelFactory(
    private val preferences: ThemePreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ThemeViewModel(preferences) as T
    }
}
