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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val note = state.notes.find {it.idNote == id}
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                        onEvent(NoteEvent.SaveNote)
                        onEvent(NoteEvent.SetId(0))
                        onEvent(NoteEvent.SetTitre(""))
                        onEvent(NoteEvent.SetBody(""))
                        onEvent(NoteEvent.SetDateCreation(System.currentTimeMillis()))
                        onEvent(NoteEvent.SetDateModification(System.currentTimeMillis()))
                        }) {
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
            if (note != null) {
                LaunchedEffect(note.idNote) {
                    if(state.noteId == 0){
                        onEvent(NoteEvent.SetId(note.idNote))
                        onEvent(NoteEvent.SetTitre(note.titre))
                        onEvent(NoteEvent.SetBody(note.body))
                        onEvent(NoteEvent.SetDateCreation(note.dateCreation))
                        onEvent(NoteEvent.SetDateModification(note.dateDerniereModification))
                    }
                }
            }


            if (note == null){
                Text("Note introuvable", style = MaterialTheme.typography.headlineMedium)
            }else{
                Note(note, state, onEvent, navController)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Note(note: Note, state: NoteState, onEvent: (NoteEvent) -> Unit, navController: NavController){
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
        StyledTextFielTitre(state.titre, couleurAffichage, onEvent)
        StyledTextFieldBody(state.body, couleurAffichage, onEvent, Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Modifiée le : $dateModification", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
            Text(text = "Créée le : $dateCreation", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}


@Composable
fun StyledTextFielTitre(
    titre: String,
    backgroundColor: Color,
    onEvent: (NoteEvent) -> Unit
){

    TextField(
        value = titre,
        onValueChange = {
            onEvent(NoteEvent.SetTitre(it))
            onEvent(NoteEvent.SetDateModification(System.currentTimeMillis()))
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            cursorColor = MaterialTheme.colorScheme.onPrimary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
    )
}

@Composable
fun StyledTextFieldBody(
    body: String,
    backgroundColor: Color,
    onEvent: (NoteEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    TextField(
        value = body,
        onValueChange = {
            onEvent(NoteEvent.SetBody(it))
            onEvent(NoteEvent.SetDateModification(System.currentTimeMillis()))
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            cursorColor = MaterialTheme.colorScheme.onPrimary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            ),
        textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp),
        modifier = modifier
            .fillMaxWidth(0.95f)
            .padding(bottom = 16.dp)
            .verticalScroll(rememberScrollState())
    )
}
