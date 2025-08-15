import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kroc_note.ui.data.FileDao
import com.example.kroc_note.ui.data.FileState
import com.example.kroc_note.ui.data.bddClass.File
import com.example.kroc_note.ui.data.type.CouleurNote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class FileViewModel(
    private val dao: FileDao
) : ViewModel() {

    private val _state = MutableStateFlow(FileState())
    var state: StateFlow<FileState> = _state

    fun chargerDossiers() {
        viewModelScope.launch {
            _state.value = _state.value.copy(files = dao.getAllDossiers())
        }
    }

    fun creerDossier() {
        viewModelScope.launch {
            dao.insert(File(
                name = "Dossier",
                color = CouleurNote.Violet,
                dateCreation = System.currentTimeMillis(),
                dateModification = System.currentTimeMillis(),
                path = ""))
            chargerDossiers()
        }
    }

    fun supprimerDossier(file: File) {
        viewModelScope.launch {
            dao.delete(file)
            chargerDossiers()
        }
    }
}
