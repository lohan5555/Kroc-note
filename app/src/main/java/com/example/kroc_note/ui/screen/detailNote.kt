package com.example.kroc_note.ui.screen

import android.graphics.Paint.Style
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.kroc_note.ui.data.NoteState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kroc_note.ui.data.NoteEvent
import com.example.kroc_note.ui.data.bddClass.Note
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    id: Int,
    state: NoteState,
    onEvent: (NoteEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            val note = state.notes.find {it.idNote == id}

            if (note == null){
                Text("Note introuvable", style = MaterialTheme.typography.headlineMedium)
            }else{
                Note(note, onEvent, navController)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Note(note: Note, onEvent: (NoteEvent) -> Unit, navController: NavController){
    val couleurAffichage: Color = note.couleur.color
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    val dateCreation = Instant.ofEpochMilli(note.dateCreation)
        .atZone(ZoneId.systemDefault())
        .format(dateFormatter)

    val dateModification = Instant.ofEpochMilli(note.dateDerniereModification)
        .atZone(ZoneId.systemDefault())
        .format(dateFormatter)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(couleurAffichage)
            .padding(16.dp)
    ) {
        Text(text = note.titre, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onPrimary)
        Text(text = note.body, color = MaterialTheme.colorScheme.onPrimary)

        Spacer(modifier = Modifier.weight(1f)) //pour être en bas de la page
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Modifiée le : $dateModification", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
            Text(text = "Créée le : $dateCreation", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}