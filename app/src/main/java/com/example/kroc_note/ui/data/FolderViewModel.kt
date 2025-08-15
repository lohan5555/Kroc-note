import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kroc_note.ui.data.FolderDao
import com.example.kroc_note.ui.data.FolderState
import com.example.kroc_note.ui.data.bddClass.Folder
import com.example.kroc_note.ui.data.type.CouleurNote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class FolderViewModel(
    private val dao: FolderDao
) : ViewModel() {

    private val _state = MutableStateFlow(FolderState())
    var state: StateFlow<FolderState> = _state

    fun chargerDossiers() {
        viewModelScope.launch {
            _state.value = _state.value.copy(folders = dao.getAllDossiers())
        }
    }

    fun creerDossier() {
        viewModelScope.launch {
            dao.insert(Folder(
                name = "Dossier",
                color = CouleurNote.Violet,
                dateCreation = System.currentTimeMillis(),
                dateModification = System.currentTimeMillis(),
                path = ""))
            chargerDossiers()
        }
    }

    fun supprimerDossier(folder: Folder) {
        viewModelScope.launch {
            dao.delete(folder)
            chargerDossiers()
        }
    }
}
