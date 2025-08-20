package com.example.kroc_note.ui.data

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import com.example.kroc_note.ui.data.type.CouleurNote


@Composable
fun EditFolderDialog(
    path: String,
    state: FolderState,
    onEvent: (FolderEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val folder = state.folders.find { it.name == path.substringAfterLast('/')}
    LaunchedEffect(folder) { if (folder != null) {
        onEvent(FolderEvent.SetId(folder.idFolder))
        onEvent(FolderEvent.SetName(folder.name))
        onEvent(FolderEvent.SetPath(folder.path))
        onEvent(FolderEvent.SetColor(folder.color))
        onEvent(FolderEvent.SetDateCreation(folder.dateCreation))
        onEvent(FolderEvent.SetDateModification(System.currentTimeMillis()))
    }  }




    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(FolderEvent.HideDialog)
        },
        title = { Text(text = "Modifier le dossier") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.name,
                    onValueChange = { onEvent(FolderEvent.SetName(it)) },
                    placeholder = { Text("Nom") }
                )
                /*
                TextField(
                    value = state.body,
                    onValueChange = { onEvent(NoteEvent.SetBody(it)) },
                    placeholder = { Text("Contenu") }
                )*/
            }
        },
        confirmButton = {
            Button(onClick = {
                onEvent(FolderEvent.SetDateModification(System.currentTimeMillis()))
                onEvent(FolderEvent.SaveFolder)
                onEvent(FolderEvent.HideDialog)

                onEvent(FolderEvent.SetId(0))
                onEvent(FolderEvent.SetName(""))
                onEvent(FolderEvent.SetPath("home"))
                onEvent(FolderEvent.SetColor(CouleurNote.Violet))
                onEvent(FolderEvent.SetDateCreation(System.currentTimeMillis()))
                onEvent(FolderEvent.SetDateModification(System.currentTimeMillis()))
            }) {
                Text("Sauvegarder")
            }

        }
    )
}