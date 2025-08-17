package com.example.kroc_note.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.kroc_note.ui.data.NoteState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kroc_note.ui.data.NoteEvent
import com.example.kroc_note.ui.data.bddClass.Note
import com.example.kroc_note.ui.data.type.CouleurNote
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
                        onEvent(NoteEvent.SetId(0))
                        onEvent(NoteEvent.SetTitre(""))
                        onEvent(NoteEvent.SetBody(""))
                        onEvent(NoteEvent.SetColor(CouleurNote.Violet))
                        onEvent(NoteEvent.SetDateCreation(System.currentTimeMillis()))
                        onEvent(NoteEvent.SetDateModification(System.currentTimeMillis()))
                        }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    Icon(Icons.Default.Share, contentDescription = "partager")
                    IconButton(onClick = {
                        if(note != null){
                            onEvent(NoteEvent.DeleteNote(note))
                        }
                        navController.popBackStack()
                        onEvent(NoteEvent.SetId(0))
                        onEvent(NoteEvent.SetTitre(""))
                        onEvent(NoteEvent.SetBody(""))
                        onEvent(NoteEvent.SetColor(CouleurNote.Violet))
                        onEvent(NoteEvent.SetDateCreation(System.currentTimeMillis()))
                        onEvent(NoteEvent.SetDateModification(System.currentTimeMillis()))

                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "supprimer")
                    }
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
            if (note != null) {
                LaunchedEffect(note.idNote) {
                    if(state.noteId == 0){
                        onEvent(NoteEvent.SetId(note.idNote))
                        onEvent(NoteEvent.SetTitre(note.titre))
                        onEvent(NoteEvent.SetBody(note.body))
                        onEvent(NoteEvent.SetColor(note.couleur))
                        onEvent(NoteEvent.SetDateCreation(note.dateCreation))
                        onEvent(NoteEvent.SetDateModification(note.dateModification))
                    }
                }
            }


            if (note == null){
                Text("Note introuvable", style = MaterialTheme.typography.headlineMedium)
            }else{
                Note(note, state, onEvent)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Note(note: Note,
         state: NoteState,
         onEvent: (NoteEvent) -> Unit){
    val couleurAffichage: Color = state.couleur.color
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    onEvent(NoteEvent.SetPath(note.path))

    val dateCreation = Instant.ofEpochMilli(note.dateCreation)
        .atZone(ZoneId.systemDefault())
        .format(dateFormatter)

    val dateModification = Instant.ofEpochMilli(note.dateModification)
        .atZone(ZoneId.systemDefault())
        .format(dateFormatter)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(couleurAffichage)
            .padding(16.dp)
    ) {
        StyledTextFielTitre(couleurAffichage, onEvent, state)
        StyledTextFieldBody(couleurAffichage, onEvent, Modifier.weight(1f), state)

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                item{
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = CenterVertically
                    ) {
                        CouleurNote.entries.forEach { couleur ->
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        onEvent(NoteEvent.SetColor(couleur))
                                        onEvent(NoteEvent.SetDateModification(System.currentTimeMillis()))
                                    },
                                verticalAlignment = CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .then(
                                            if (state.couleur == couleur) {
                                                Modifier.border(
                                                    width = 2.dp,
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    shape = CircleShape
                                                )
                                            } else Modifier
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    RadioButton(
                                        colors = RadioButtonColors(
                                            selectedColor = couleur.color,
                                            unselectedColor = couleur.color,
                                            disabledSelectedColor = couleurAffichage,
                                            disabledUnselectedColor = couleurAffichage
                                        ),
                                        selected = state.couleur == couleur,
                                        onClick = {
                                            onEvent(NoteEvent.SetColor(couleur))
                                            onEvent(
                                                NoteEvent.UpdateNote(
                                                    noteId = state.noteId,
                                                    titre = state.titre,
                                                    body = state.body,
                                                    path = state.path,
                                                    dateCreation = state.dateCreation,
                                                    dateModification = System.currentTimeMillis(),
                                                    couleur = couleur,
                                                )
                                            )
                                        }
                                    )
                                }

                            }
                        }
                    }
                }
            }
            Text(text = "Modifiée le : $dateModification", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
            Text(text = "Créée le : $dateCreation", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}


@Composable
fun StyledTextFielTitre(
    backgroundColor: Color,
    onEventNote: (NoteEvent) -> Unit,
    state: NoteState
){

    TextField(
        value = state.titre,
        onValueChange = { newValue ->
            onEventNote(NoteEvent.SetTitre(newValue))
            onEventNote(NoteEvent.DebouncedSave)
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
    backgroundColor: Color,
    onEventNote: (NoteEvent) -> Unit,
    modifier: Modifier = Modifier,
    state: NoteState
) {

    TextField(
        value = state.body,
        onValueChange = { newValue ->
            onEventNote(NoteEvent.SetBody(newValue))
            onEventNote(NoteEvent.DebouncedSave)
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
