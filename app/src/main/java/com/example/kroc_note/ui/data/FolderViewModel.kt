import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kroc_note.ui.data.FolderDao
import com.example.kroc_note.ui.data.FolderEvent
import com.example.kroc_note.ui.data.FolderState
import com.example.kroc_note.ui.data.NoteEvent
import com.example.kroc_note.ui.data.bddClass.Folder
import kotlinx.coroutines.flow.combine
import com.example.kroc_note.ui.data.bddClass.Note
import com.example.kroc_note.ui.data.type.CouleurNote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class FolderViewModel(
    private val dao: FolderDao
) : ViewModel() {
    private val _folders = dao.getAllDossiers() // doit renvoyer Flow<List<Folder>>
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(FolderState())
    val state = combine(_state, _folders) { state, folders ->
        state.copy(folders = folders)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FolderState())

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: FolderEvent){
        when(event){
            FolderEvent.SaveFolder -> {
                if(state.value.name.isBlank() || state.value.path.isBlank()){
                    return
                }

                viewModelScope.launch {
                    val folder = Folder(
                        idFolder = state.value.folderId, // ⚡ important pour savoir si on update
                        name = state.value.name,
                        path = state.value.path,
                        color = state.value.couleur,
                        dateCreation = state.value.dateCreation,
                        dateModification = state.value.dateModification
                    )
                    dao.upsert(folder)
                }
                /*if(idFolder == 0){
        _state.update { it.copy(
            name = "Dossier",
            path = path,
            isEditFolder = false
        ) }
    }*/
            }
            is FolderEvent.DeleteFolder -> {
                viewModelScope.launch {
                    dao.delete(event.folder)
                }
            }

            is FolderEvent.SetId -> {
                _state.update { it.copy(
                    folderId = event.idFolder
                ) }
            }
            is FolderEvent.SetName -> {
                _state.update { it.copy(
                    name = event.name
                ) }
            }
            is FolderEvent.SetPath -> {
                _state.update { it.copy(
                    path = event.path
                ) }
            }
            is FolderEvent.SetColor -> {
                _state.update { it.copy(
                    couleur = event.couleur
                ) }
            }
            is FolderEvent.SetDateCreation -> {
                _state.update { it.copy(
                    dateCreation = event.dateCreation
                ) }
            }
            is FolderEvent.SetDateModification -> {
                _state.update { it.copy(
                    dateModification = event.dateModification
                ) }
            }
            FolderEvent.ShowDialog -> {
                _state.update { it.copy(
                    isEditFolder = true
                ) }
            }
            FolderEvent.HideDialog -> {
                _state.update { it.copy(
                    isEditFolder = false
                ) }
            }
        }
    }
}
