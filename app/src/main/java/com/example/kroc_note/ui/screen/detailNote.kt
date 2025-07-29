package com.example.kroc_note.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kroc_note.ui.data.Note
import com.example.kroc_note.ui.data.NoteState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, id: Int, state: NoteState) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kroc-Note détail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    Icon(Icons.Default.MoreVert, contentDescription = "")
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            val note = state.notes.find {it.id == id}

            if (note == null){
                Text("Note introuvable", style = MaterialTheme.typography.headlineMedium)
            }else{
                Note(note)
            }
        }
    }
}

@Composable
fun Note(note: Note){
    Text(text = "Hello 👋, note n°${note.id}", style = MaterialTheme.typography.headlineMedium)
}