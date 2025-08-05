package com.example.kroc_note.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kroc_note.ui.data.NoteEvent
import com.example.kroc_note.ui.data.NoteState
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.ui.unit.dp


@Composable
fun AddNoteDialog(
    state: NoteState,
    onEvent: (NoteEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(NoteEvent.HideDialog)
        },
        title = { Text(text = "Add note") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = "",//state.titre,
                    onValueChange = { onEvent(NoteEvent.SetTitre(it)) },
                    placeholder = { Text("Titre") }
                )
                TextField(
                    value = "",// state.body,
                    onValueChange = { onEvent(NoteEvent.SetBody(it)) },
                    placeholder = { Text("Contenu") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onEvent(NoteEvent.SaveNote) }) {
                Text("Save note")
            }
        }
    )
}
